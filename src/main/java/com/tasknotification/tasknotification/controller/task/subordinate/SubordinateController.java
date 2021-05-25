package com.tasknotification.tasknotification.controller.task.subordinate;

import com.tasknotification.tasknotification.controller.*;
import com.tasknotification.tasknotification.db.*;
import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.task.subordinate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;

@RestController
public class SubordinateController {
    @Autowired
    protected SubordinateDao subordinateRepo;

    @GetMapping("/addsubordinate")
    public SubordinateResponse addSubordinate(@RequestParam("name") String name,
                                              @RequestParam("emailaddress") String emailAddress,
                                              @CookieValue(value = "JSESSIONID", defaultValue = "INVALID_USER") String sessionId,
                                              HttpServletRequest request)
    {
        HttpSession         i;
        SubordinateRequest  r;
        SubordinateResponse s;

        i = request.getSession(false);
        r = getSubordinateRequest(sessionId, name, emailAddress);
        s = getSubordinateResponse(r, i);
        try {
            if (isSubordinateRequestCheckSuccess(s)) {
                savingSubordinate(r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return s;
    }

    protected SubordinateRequest getSubordinateRequest(String jSessionId, String name, String emailAddress) { return createSubordinateRequest(jSessionId, name, emailAddress);}
    protected SubordinateRequest createSubordinateRequest(String jSessionId, String name, String emailAddress)
    {
        SubordinateRequest r;

        r = new SubordinateRequest();

        r.setEmailAddress(emailAddress);
        r.setUserSessionId(jSessionId);
        r.setName(name);

        return r;
    }

    protected SubordinateResponse getSubordinateResponse(SubordinateRequest r, HttpSession session) { return createSubordinateResponse(r, session);}
    protected SubordinateResponse createSubordinateResponse(SubordinateRequest r, HttpSession session)
    {
        SubordinateResponse s;

        s = new SubordinateResponse();

        s.setReqId(r.getId());
        s = getSubordinateResponse(r, session, s);
        return s;
    }
    protected SubordinateResponse getSubordinateResponse(SubordinateRequest r, HttpSession session, SubordinateResponse s) { return checkSubordinateResponse(r, session, s);}
    protected SubordinateResponse checkSubordinateResponse(SubordinateRequest r, HttpSession session, SubordinateResponse s)
    {
        if (isRequestValid(r)) {
            if (!isSessionValid(r.getUserSessionId(), session)) {
                s = createSubordinateResponse(s, Results.ERR, Codes.INACTIVE_SESSION_ID, "");
            } else if (existsEmailAddress(r.getEmailAddress())) {
                s = createSubordinateResponse(s, Results.ERR, Codes.EXISTING_EMAIL, "");
            } else if (emailAddressIsNotValid(r.getEmailAddress())) {
                s = createSubordinateResponse(s, Results.ERR, Codes.VALIDATION_ERR, "");
            } else {
                s = createSubordinateResponse(s, Results.OK, Codes.SUCCESS, "");
            }
        }
        return s;
    }
    protected SubordinateResponse createSubordinateResponse(SubordinateResponse s, Results r, Codes c, String m)
    {
        s.setResult(r.toString());
        s.setCode(CodesSet.getCode(c));
        s.setMessage(m);

        return s;
    }

    protected boolean isSessionValid(String sessionId, HttpSession  session)
    {
        if (session == null) {
            return false;
        } else {
            return (!sessionId.equals("INVALID_USER")) && (sessionId.equals(session.getId()));
        }
    }
    protected boolean isRequestValid        (SubordinateRequest r) { return r.getEmailAddress() != null && r.getName() != null;}
    protected boolean existsEmailAddress    (String             s) { return subordinateRepo.findByEmailAddress(s).size() > 0;}
    protected boolean emailAddressIsNotValid(String             s) { return s.equals("validationErr");}

    protected boolean isSubordinateRequestCheckSuccess(SubordinateResponse s) { return s.getCode()==0;}

    protected SubordinateBase getSubordinate(SubordinateRequest r) { return createSubordinate(r);}
    protected SubordinateBase createSubordinate(SubordinateRequest r)
    {
        SubordinateBase s;

        s = new SubordinateBase();

        s.setId(generateSubordinateId(s.getId()));
        s.setName(r.getName());
        s.setEmailAddress(r.getEmailAddress());

        return s;
    }
    protected synchronized String generateSubordinateId(String id)
    {
        if (subordinateRepo.findById(id) != null) {
            return String.valueOf(Integer.parseInt(id) + 1);
        }
        return id;
    }

    protected void savingSubordinate(SubordinateRequest r) { subordinateRepo.save(getSubordinate(r));}
}
