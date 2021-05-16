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
                                   @RequestParam("password") String password,
                                   HttpServletRequest request)
    {
        LogInRequest           r;
        LogInResponse          s;
        HttpSession            i;

        r = getLoginRequest(emailAddress, password);
        s = getLoginResponse(r);

        if (isLoginSuccess(s)) {
            saveLoginBase(r);
            i = getSession(request);
            s.setSessionId(Security.encrypt(i.getId()));
            validateSession(i, Security.encrypt(emailAddress));
        }
        return s;
    }

    protected void saveLoginBase(LogInRequest r)
    {
        LoginBase        l;
        UserAccountsBase a;

        l = new LoginBase();
        a = getUserAccountsBase(Security.encrypt(r.getEmailAddress()), Security.encrypt(r.getPassword()));

        this.loginRepo.save(l);
        a.addLoginId(l.getId());
        this.userAccountsRepo.save(a);
    }

    protected UserAccountsBase getUserAccountsBase(String e, String p) { return userAccountsRepo.findByEmailAddressAndPwd(e, p).get(0);}

    protected void validateSession(HttpSession s, String e) { s.setAttribute("user", e); }

    protected HttpSession getSession (HttpServletRequest r) { return createSession(r)     ;}
    protected HttpSession createSession (HttpServletRequest r) {
        HttpSession  s;
        s = r.getSession();
        s.setMaxInactiveInterval(30);

        return s;
    }

    protected LogInRequest getLoginRequest(String e, String p) { return createLoginRequest(e, p);}

    protected LogInRequest createLoginRequest(String e, String p)
    {
        LogInRequest r;

        r = new LogInRequest();
        r.setEmailAddress(e);
        r.setPassword(p);

        return r;
    }

    protected LogInResponse getLoginResponse(LogInRequest r) { return createLoginResponse(r);}

    protected LogInResponse createLoginResponse(LogInRequest r)
    {
        LogInResponse s;
        String e;
        String p;

        s = new LogInResponse();
        e = Security.encrypt(r.getEmailAddress());
        p = Security.encrypt(r.getPassword());

        s.setReqId(r.getId());

        if (isRequestValid(r)) {
            if (!isLoginDataSuccess(e, p)) {
                s.setResult("ERR");
                s.setCode(CodesSet.getCode(Codes.LOGIN_FAILED));
                s.setMessage(Codes.LOGIN_FAILED.toString());
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

    protected boolean isLoginSuccess(LogInResponse c) { return c.getCode()==0;}
    protected boolean isRequestValid(LogInRequest  r) { return r.getEmailAddress()!= null && r.getPassword() != null;}
    protected boolean isLoginDataSuccess(String e, String p) {
        return this.userAccountsRepo.findByEmailAddressAndPwd(e, p) != null
                && this.userAccountsRepo.findByEmailAddressAndPwd(e, p).size() == 1;}

}
