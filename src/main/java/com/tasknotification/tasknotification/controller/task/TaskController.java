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
                                 @RequestParam("gracetimeperiod") String graceTimePeriod,
                                 @RequestParam("addresseeemail" ) String addresseeEmail ,
                                 @CookieValue(value = "JSESSIONID", defaultValue = "INVALID_USER") String sessionId,
                                 HttpServletRequest request)
            throws ParseException
    {
        TaskRequest  r;
        HttpSession  i;
        TaskResponse s;

        r = getTaskRequest(sessionId, object, message, term, graceTimePeriod, addresseeEmail);
        i = request.getSession(false);
        s = getTaskResponse(r, i);
        try {
            if (isUserValid(s)) {
                saveTask(r, i);
            }
        } catch (Exception e) {}
        return s;
    }

    protected void saveTask(TaskRequest r, HttpSession s)
    {
        TaskBase         t;
        UserAccountsBase a;

        t = getTask(r);
        a = getUserAccountsBase(s);

        taskRepo.save(t);
        a.addTaskId(t.getId());
        userAccountsRepo.save(a);
    }

    protected TaskBase getTask(TaskRequest r) { return createTask(r);}

    protected TaskBase createTask(TaskRequest r)
    {
        TaskBase         t;

        t = new TaskBase();

        t.setObject(r.getObject());
        t.setMessage(r.getMessage());
        t.setTerm(r.getTerm());
        t.setGraceTimePeriod(r.getGraceTimePeriod());
        t.setAddresseeMail(r.getAddresseeEmail());

        return t;
    }

    protected TaskRequest getTaskRequest(String JSessionId, String object, String message, String term,
                                            String graceTimePeriod, String addresseeEmail)
    {
        return createTaskRequest(JSessionId, object, message, term, graceTimePeriod, addresseeEmail);
    }

    protected TaskRequest createTaskRequest(String JSessionId, String object, String message, String term,
                                            String graceTimePeriod, String addresseeEmail)
    {
        TaskRequest  r;

        r = new TaskRequest();

        r.setUserSessionId(JSessionId);
        r.setObject(object);
        r.setMessage(message);
        r.setTerm(term);
        r.setGraceTimePeriod(graceTimePeriod);
        r.setAddresseeEmail(addresseeEmail);

        return r;
    }

    protected TaskResponse getTaskResponse(TaskRequest r, HttpSession session)
            throws ParseException { return createTaskResponse(r, session);}

    protected TaskResponse createTaskResponse(TaskRequest r, HttpSession session)
            throws ParseException
    {
        TaskResponse s;

        s = new TaskResponse();

        s.setReqId(r.getId());

        if (isRequestValid(r)) {
            if (!isTermValid(r.getTerm())) {
                s.setResult("ERR");
                s.setCode(CodesSet.getCode(Codes.TERM_IS_NOT_VALID));
                s.setMessage(Codes.TERM_IS_NOT_VALID.toString());
            } else if (isSessionValid(Security.decrypt(r.getUserSessionId().getBytes()), session)) {
                s.setResult("ERR");
                s.setCode(CodesSet.getCode(Codes.INACTIVE_SESSION_ID));
                s.setMessage(Codes.INACTIVE_SESSION_ID.toString());
            } else {
                s.setResult("OK");
                s.setCode(CodesSet.getCode(Codes.SUCCESS));
                s.setMessage(Codes.SUCCESS.toString());
            }
        } else {
            s.setResult("ERR");
            s.setCode(CodesSet.getCode(Codes.REQUEST_PARAMS_EMPTY));
            s.setMessage(Codes.REQUEST_PARAMS_EMPTY.toString());
        }

        return s;
    }

    protected UserAccountsBase getUserAccountsBase(HttpSession s) { return userAccountsRepo.findByEmailAddress(s.getAttribute("user").toString()).get(0);}
    protected boolean isUserValid (TaskResponse s) { return s.getCode()==0;}
    protected boolean isSessionValid(String sessionId, HttpSession  session)
    {
        if (session == null) {
            return false;
        } else {
            return (!sessionId.equals("INVALID_USER")) && (sessionId.equals(session.getId()));
        }
    }
    protected boolean isRequestValid(TaskRequest  r)
    {
        return r.getObject() != null && r.getMessage() != null && r.getTerm() != null
                    && r.getGraceTimePeriod() != null && r.getAddresseeEmail() != null;
    }
    protected boolean isTermValid(String term)
            throws ParseException
    {
        long d;

        d = 1000 * 60 * 60 * 24;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(term).getTime() > new Date().getTime() + d;
    }
}
