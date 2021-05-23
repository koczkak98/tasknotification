package com.tasknotification.tasknotification.controller.login;

import javax.servlet.http.*;

import com.tasknotification.tasknotification.controller.*;
import com.tasknotification.tasknotification.db.*;
import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.login.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    UserAccountsDao userAccountsRepo;
    @Autowired
    LoginDao        loginRepo       ;

    @GetMapping("/login")
    public LogInResponse sendLogin(@RequestParam("emailaddress") String emailAddress,
                                   HttpServletRequest request)
    {
        LogInRequest           r;
        LogInResponse          s;
        HttpSession            i;

        r = getLoginRequest(emailAddress);
        s = getLoginResponse(r);

        try {
            if (findUserAccount(s)) {
                saveLoginBase(r);
                i = getSession(request);
                s.setSessionId(i.getId());
                validateSession(i, emailAddress);
            }
        } catch (Exception e) {
            s = createLogInResponse(s, Results.ERR, Codes.UNKNOWN_ERROR, "[ " + Codes.UNKNOWN_ERROR.toString() + " ]: " + e.getMessage());
        }
        return s;
    }

    protected LogInRequest getLoginRequest(String e) { return createLoginRequest(e);}

    protected LogInRequest createLoginRequest(String e)
    {
        LogInRequest r;

        r = new LogInRequest();
        r.setEmailAddress(e);

        return r;
    }

    protected LogInResponse getLoginResponse(LogInRequest r) { return createLoginResponse(r);}
    protected LogInResponse createLoginResponse(LogInRequest r)
    {
        LogInResponse s;

        s = new LogInResponse();
        s.setReqId(r.getId());

        s = getLogInResponse(r, s);

        return s;
    }
    protected LogInResponse getLogInResponse(LogInRequest r, LogInResponse s) { return checkLogInResponse(r, s);}
    protected LogInResponse checkLogInResponse(LogInRequest r, LogInResponse s)
    {
        String e;

        e = r.getEmailAddress();

        if (isRequestValid(r)) {
            if (!isLoginDataSuccess(e)) {
                s = createLogInResponse(s, Results.ERR, Codes.LOGIN_FAILED,"");
            } else {
                s = createLogInResponse(s, Results.OK, Codes.SUCCESS,"");
            }
        } else {
            s = createLogInResponse(s, Results.ERR, Codes.REQUEST_PARAMS_EMPTY,"");
        }
        return s;
    }
    protected LogInResponse createLogInResponse(LogInResponse s, Results r, Codes c, String m)
    {
        s.setResult(r.toString());
        s.setCode(CodesSet.getCode(c));
        s.setMessage(m);

        return s;
    }

    protected boolean isRequestValid(LogInRequest  r) { return r.getEmailAddress()!= null;}
    protected boolean isLoginDataSuccess(String e) {
        return this.userAccountsRepo.findByEmailAddress(e)!= null
                && this.userAccountsRepo.findByEmailAddress(e).size() == 1;}

    protected boolean findUserAccount(LogInResponse c) { return c.getCode()==0;}

    protected void saveLoginBase(LogInRequest r)
    {
        LoginBase        l;
        UserAccountsBase a;

        l = new LoginBase();
        a = getUserAccountsBase(r.getEmailAddress());

        this.loginRepo.save(l);
        a.addLoginId(l.getId());
        this.userAccountsRepo.save(a);
    }
    protected UserAccountsBase getUserAccountsBase(String e) { return userAccountsRepo.findByEmailAddress(e).get(0);}

    protected HttpSession getSession (HttpServletRequest r) { return createSession(r)     ;}
    protected HttpSession createSession (HttpServletRequest r) {
        HttpSession  s;
        s = r.getSession();

        return s;
    }

    protected void validateSession(HttpSession s, String e) { s.setAttribute("user", e); }

}
