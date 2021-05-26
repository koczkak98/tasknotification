package com.tasknotification.tasknotification.controller.task;

import com.tasknotification.tasknotification.controller.*;
import com.tasknotification.tasknotification.controller.sendemail.SendEmailBc;
import com.tasknotification.tasknotification.db.*;
import com.tasknotification.tasknotification.model.base.TaskBase;
import com.tasknotification.tasknotification.model.base.UserAccountsBase;
import com.tasknotification.tasknotification.model.sendemail.SendEmailRequest;
import com.tasknotification.tasknotification.model.sendemail.SendEmailResponse;
import com.tasknotification.tasknotification.model.task.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.text.*;
import java.util.*;

@RestController
public class TaskController {
    @Autowired
    protected UserAccountsDao userAccountsRepo;
    @Autowired
    protected TaskDao          taskRepo        ;
    @Autowired
    protected SubordinateDao   subordinateRepo ;
    protected UserAccountsBase user            ;

    @GetMapping("/addtask")
    public TaskResponse sendTask(@RequestParam("object"         ) String object         ,
                                      @RequestParam("message"        ) String message        ,
                                      @RequestParam("term"           ) String term           ,
                                      @RequestParam("graceperiod"    ) int    gracePeriod    ,
                                      @RequestParam("subordinate"    ) String id             ,
                                      @CookieValue(value = "JSESSIONID", defaultValue = "INVALID_USER") String sessionId,
                                      HttpServletRequest request)
    {
        HttpSession        i;
        TaskRequest        r;
        TaskResponse       s;

        i    = request.getSession(false);
        r    = getTaskRequest(i, sessionId, object, message, term, gracePeriod, id);
        user = findUserAccountsBase(i.getAttribute("user").toString());
        s    = getTaskResponse(r, i);

        try {
            if (isRequestSuccess(s)) {
                saveTask(r);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return s;
    }

    @GetMapping("/feedback")
    public String feedbackOfSubordinateForTask(@RequestParam("subordinateid") String subordinateId,
                                               @RequestParam("taskid"       ) String taskId       ,
                                               @RequestParam("feedback"     ) String done)
    {
        return "";
    }

    @GetMapping("/confirmation")
    public String confirmationOfUserForTask(@RequestParam("userid"       ) String userId,
                                            @RequestParam("taskid"       ) String taskId,
                                            @RequestParam("subordinateid") String subordinateId)
    {
        return "";
    }

    /** Saving task */

    protected TaskRequest getTaskRequest(HttpSession session, String JSessionId, String object, String message, String term,
                                            int graceTimePeriod, String subordinateId)
    {
        return createTaskRequest(session, JSessionId, object, message, term, graceTimePeriod, subordinateId);
    }
    protected TaskRequest createTaskRequest(HttpSession session, String JSessionId, String object, String message, String term,
                                            int graceTimePeriod, String subordinateId)
    {
        TaskRequest  r;

        r = new TaskRequest();


        r.setEmailAddress(session.getAttribute("user").toString());
        r.setUserSessionId(JSessionId);
        r.setObject(object);
        r.setMessage(message);
        r.setTerm(term);
        r.setGracePeriod(graceTimePeriod);
        r.setSubordinateId(subordinateId);

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
            } else if (alreadyAssignedObjectToSubordinate(r)) {
                s = createTaskResponse(s, Results.WARNING, Codes.EXISTING_OBJECT_AT_SUBORDINATE, "");
            } else if (!isMessageValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.MESSAGE_CHAR_INCORRECT, "");
            } else if (!isTermValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.TERM_IS_NOT_VALID, "");
            } else if (!isGracePeriodValid(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.GRACE_PERIOD_INCORRECT, "");
            } else if (!isSubordinateIdExisting(r)) {
                s = createTaskResponse(s, Results.ERR, Codes.NOT_EXIST_SUBORDINATE_ID, "");
            } else if (!isSubordinateIdFormatValid(r)) {
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
                && r.getGracePeriod() != 0 && r.getSubordinateId() != null;
    }

    protected boolean isObjectValid(TaskRequest  r)
    {
        return r.getObject().length() < Cfg.getObjectMaxCharLength() && r.getObject().length() > Cfg.getObjectMinCharLength();
    }

    protected boolean alreadyAssignedObjectToSubordinate(TaskRequest r) { return (findTaskOfSubordinateIdByObject(r.getEmailAddress(), r.getObject(), r.getSubordinateId()).size() >= 1);}

    protected List<TaskBase> findTaskOfSubordinateIdByObject(String u, String object, String id) {
        List<String>   s;
        TaskBase       t;
        List<TaskBase> l;

        s = user.getTaskIds();
        l = new ArrayList<>();

        for (int i = 0; i < s.size(); i++) {
            t = taskRepo.findByIdAndObject(s.get(i), object);
            if(t != null) {
                if (t.getSubordinate_ids().contains(id)) {
                    l.add(t);
                }
            }
        }

        return l;
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
        return getTermInDate(r.getTerm()).getTime() > System.currentTimeMillis() + d;
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

    protected boolean isSubordinateIdFormatValid(TaskRequest r)
    {
        int i;
        try {
            i = Integer.parseInt(r.getSubordinateId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isSubordinateIdExisting(TaskRequest  r) { return subordinateRepo.findById(r.getSubordinateId()) != null;}

    protected boolean isRequestSuccess(TaskResponse s) { return s.getCode()==0;}

    protected UserAccountsBase findUserAccountsBase(String s) { return userAccountsRepo.findByEmailAddress(s).get(0);}

    protected TaskBase getTask(TaskRequest r) { return createTask(r);}
    protected TaskBase createTask(TaskRequest r)
    {
        TaskBase         t;

        t = new TaskBase();

        t.setObject(r.getObject());
        t.setMessage(r.getMessage());
        t.setTerm(r.getTerm());
        t.setGracePeriod(r.getGracePeriod());
        t.addSubordinate_id(r.getSubordinateId());

        return t;
    }

    protected void saveTask(TaskRequest r)
    {
        TaskBase t;

        t = getTask(r);

        taskRepo.save(t);
        user.addTaskId(t.getId());
        userAccountsRepo.save(user);
    }
}