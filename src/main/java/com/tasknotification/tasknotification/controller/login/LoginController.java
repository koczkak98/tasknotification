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
            if (isLoginSuccess(s)) {
                saveLoginBase(r);
                i = getSession(request);
                s.setSessionId(i.getId());
                validateSession(i, Security.encrypt(emailAddress));
            }
        } catch (Exception e) {
            s.setResult(Results.ERR.toString());
            s.setCode(CodesSet.getCode(Codes.UNKNOWN_ERROR));
            s.setMessage("[ " + Codes.UNKNOWN_ERROR.toString() + " ]: " + e.getMessage());
        }
        return s;
    }

    protected void saveLoginBase(LogInRequest r)
    {
        LoginBase        l;
        UserAccountsBase a;

        l = new LoginBase();
        a = getUserAccountsBase(Security.encrypt(r.getEmailAddress()));

        this.loginRepo.save(l);
        a.addLoginId(l.getId());
        this.userAccountsRepo.save(a);
    }

    protected UserAccountsBase getUserAccountsBase(String e) { return userAccountsRepo.findByEmailAddress(e).get(0);}

    protected void validateSession(HttpSession s, String e) { s.setAttribute("user", e); }

    protected HttpSession getSession (HttpServletRequest r) { return createSession(r)     ;}
    protected HttpSession createSession (HttpServletRequest r) {
        HttpSession  s;
        s = r.getSession();
        s.setMaxInactiveInterval(30);

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
        String e;

        s = new LogInResponse();
        e = Security.encrypt(r.getEmailAddress());
        s.setReqId(r.getId());

        if (isRequestValid(r)) {
            if (!isLoginDataSuccess(e)) {
                s.setResult(Results.ERR.toString());
                s.setCode(CodesSet.getCode(Codes.LOGIN_FAILED));
                s.setMessage(Codes.LOGIN_FAILED.toString());
            } else {
                s.setResult(Results.OK.toString());
                s.setCode(CodesSet.getCode(Codes.SUCCESS));
                s.setMessage(Codes.SUCCESS.toString());
            }
        } else {
            s.setResult(Results.ERR.toString());
            s.setCode(CodesSet.getCode(Codes.REQUEST_PARAMS_EMPTY));
            s.setMessage(Codes.REQUEST_PARAMS_EMPTY.toString());
        }

        return s;
    }

    protected boolean isLoginSuccess(LogInResponse c) { return c.getCode()==0;}
    protected boolean isRequestValid(LogInRequest  r) { return r.getEmailAddress()!= null;}
    protected boolean isLoginDataSuccess(String e) {
        return this.userAccountsRepo.findByEmailAddress(e)!= null
                && this.userAccountsRepo.findByEmailAddress(e).size() == 1;}

}
