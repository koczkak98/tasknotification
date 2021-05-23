package com.tasknotification.tasknotification.controller.task;

import com.tasknotification.tasknotification.controller.*;
import com.tasknotification.tasknotification.db.*;
import com.tasknotification.tasknotification.model.base.TaskBase;
import com.tasknotification.tasknotification.model.base.UserAccountsBase;
import com.tasknotification.tasknotification.model.task.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.text.*;
import java.util.*;

@RestController
public class TaskController {
    @Autowired
    protected UserAccountsDao userAccountsRepo;
    @Autowired
    protected TaskDao         taskRepo        ;

    @GetMapping("/addtask")
    public TaskResponse sendTask(@RequestParam("object"         ) String object         ,
                                 @RequestParam("message"        ) String message        ,
                                 @RequestParam("term"           ) String term           ,
                                 @RequestParam("graceperiod"    ) int    gracePeriod    ,
                                 @RequestParam("addresseeemail" ) String addresseeEmail ,
                                 @CookieValue(value = "JSESSIONID", defaultValue = "INVALID_USER") String sessionId,
                                 HttpServletRequest request)
    {
        HttpSession  i;
        TaskRequest  r;
        TaskResponse s;

        i = request.getSession(false);
        r = getTaskRequest(i, sessionId, object, message, term, gracePeriod, addresseeEmail);
        s = getTaskResponse(r, i);
        try {
            if (isUserValid(s)) {
                saveTask(r);
            }
        } catch (Exception e) {
            s = createTaskResponse(s, Results.ERR, Codes.REQUEST_PARAMS_EMPTY, "[ " + Codes.UNKNOWN_ERROR.toString() + " ]: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return s;
    }

    protected TaskRequest getTaskRequest(HttpSession session, String JSessionId, String object, String message, String term,
                                            int graceTimePeriod, String addresseeEmail)
    {
        return createTaskRequest(session, JSessionId, object, message, term, graceTimePeriod, addresseeEmail);
    }
    protected TaskRequest createTaskRequest(HttpSession session, String JSessionId, String object, String message, String term,
                                            int graceTimePeriod, String addresseeEmail)
    {
        TaskRequest  r;

        r = new TaskRequest();


        r.setEmailAddress(session.getAttribute("user").toString());
        r.setUserSessionId(JSessionId);
        r.setObject(object);
        r.setMessage(message);
        r.setTerm(term);
        r.setGracePeriod(graceTimePeriod);
        r.setAddresseeEmail(addresseeEmail);

        return r;
    }

    protected TaskResponse getTaskResponse(TaskRequest r, HttpSession session) { return createTaskResponse(r, session);}
    protected TaskResponse createTaskResponse(TaskRequest r, HttpSession session)
    {
        TaskResponse s;

        s = new TaskResponse();

        s.setReqId(r.getId());
        s = getTaskResponse(r, session, s);
        return s;
    }
    protected TaskResponse getTaskResponse(TaskRequest r, HttpSession session, TaskResponse s) { return checkTaskResponse(r, session, s); }
    protected TaskResponse checkTaskResponse(TaskRequest r, HttpSession session, TaskResponse s)
    {
        if (isRequestValid(r)) {
            if (!isSessionValid(r.getUserSessionId(), session)) {
                s = createTaskResponse(s, Results.ERR, Codes.INACTIVE_SESSION_ID, "");
            } else if (!isObjectValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.OBJECT_CHAR_INCORRECT, "");
            } else if (objectExistAtAddressee(r)) {
                s = createTaskResponse(s, Results.WARNING, Codes.EXISTING_OBJECT_AT_ADDRESSEE, "");
            } else if (!isMessageValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.MESSAGE_CHAR_INCORRECT, "");
            } else if (!isTermValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.TERM_IS_NOT_VALID, "");
            } else if (!isGracePeriodValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.GRACE_PERIOD_INCORRECT, "");
            } else if (!isAddresseeEmailValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.VALIDATION_ERR, "");
            } else {
                s = createTaskResponse(s, Results.OK, Codes.SUCCESS, "");
            }
        } else {
            s = createTaskResponse(s, Results.ERR, Codes.REQUEST_PARAMS_EMPTY, "");
        }

        return s;
    }
    protected TaskResponse createTaskResponse(TaskResponse s, Results r, Codes c, String m)
    {
        s.setResult(r.toString());
        s.setCode(CodesSet.getCode(c));
        s.setMessage(m);

        return s;
    }

    protected boolean isSessionValid(String sessionId, HttpSession  session)
    {
        if (session == null) {
            System.out.println("NULL");
            return false;
        } else {
            return (!sessionId.equals("INVALID_USER")) && (sessionId.equals(session.getId()));
        }
    }

    protected boolean isRequestValid(TaskRequest  r)
    {
        return r.getObject() != null && r.getMessage() != null && r.getTerm() != null
                && r.getGracePeriod() != 0 && r.getAddresseeEmail() != null;
    }

    protected boolean isObjectValid(TaskRequest  r)
    {
        return r.getObject().length() < Cfg.getObjectMaxCharLength() && r.getObject().length() > Cfg.getObjectMinCharLength();
    }

    protected boolean objectExistAtAddressee(TaskRequest r)
    {
        List<TaskBase> tasksList;

        tasksList = taskRepo.findByObject(r.getObject());
        if (tasksList.size() >= 1) {
            for (int i = 0; i < tasksList.size(); i++) {
                if (tasksList.get(i).getAddresseeMail().equals(r.getAddresseeEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isMessageValid(TaskRequest r)
    {
        return r.getMessage().length() < Cfg.getMessageMaxCharLength() && r.getMessage().length() > Cfg.getMessageMinCharLength();
    }

    protected Date getTermInDate(String term) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(term);
        } catch (ParseException p) {
            return new Date();
        }
    }

    protected boolean isTermValid(TaskRequest  r)
    {
        long d;

        d = 1000 * 60 * 60 * 24;
        return getTermInDate(r.getTerm()).getTime() > new Date().getTime() + d;
    }

    protected boolean isGracePeriodValid(TaskRequest  r)
    {
        long h;
        long d;

        h = 1000 * 60 * 60;
        d = getTermInDate(r.getTerm()).getTime() - new Date().getTime();

        if (r.getGracePeriod() == 0) {
            r.setGracePeriod((int)((d - h)/h));
        }

        return r.getGracePeriod() * h < d;
    }

    protected boolean isAddresseeEmailValid(TaskRequest  r)
    {
        return !(r.getAddresseeEmail().equals("validationErr"));
    }

    protected boolean isUserValid (TaskResponse s) { return s.getCode()==0;}

    protected UserAccountsBase getUserAccountsBase(String s) { return userAccountsRepo.findByEmailAddress(s).get(0);}

    protected TaskBase getTask(TaskRequest r) { return createTask(r);}
    protected TaskBase createTask(TaskRequest r)
    {
        TaskBase         t;

        t = new TaskBase();

        t.setObject(r.getObject());
        t.setMessage(r.getMessage());
        t.setTerm(r.getTerm());
        t.setGracePeriod(r.getGracePeriod());
        t.setAddresseeMail(r.getAddresseeEmail());

        return t;
    }

    protected void saveTask(TaskRequest r)
    {
        TaskBase         t;
        UserAccountsBase a;

        t = getTask(r);
        a = getUserAccountsBase(r.getEmailAddress());

        taskRepo.save(t);
        a.addTaskId(t.getId());
        userAccountsRepo.save(a);
    }
}