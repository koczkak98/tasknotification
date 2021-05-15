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
    UserAccountsRepository userAccountsRepo;
    @Autowired
    LoginRepository        loginRepo       ;

    @GetMapping("/login")
    public LogInResponse sendLogin(@RequestParam("emailaddress") String emailAddress,
                                   @RequestParam("password") String password,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
    {
        LogInRequest           r;
        LogInResponse          s;
        HttpSession            i;

        r = getLoginRequest(emailAddress, password);
        s = getLoginResponse(r);
        try {
            if (isLoginSuccess(s.getCode())) {
                saveLoginBase(Security.encrypt(emailAddress), Security.encrypt(password));
                i = getSession(request);
                s.setSessionId(Security.encrypt(i.getId()));
                validateSession(i, Security.encrypt(emailAddress));
            } else {
                throw new RuntimeException("LoginError");
            }
        } catch (Exception e) { }

        return s;
    }

    protected void saveLoginBase(String e, String p)
    {
        LoginBase        l;
        UserAccountsBase a;

        l = new LoginBase();
        a = getUserAccountsBase(e, p);

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

        s = new LogInResponse();
        s.setReqId(r.getId());

        s.setResult(getResult(r));
        s.setCode(getCode(r));
        s.setMessage("");

        return s;
    }


    protected String getResult (LogInRequest r) { return generateResult(r);}

    protected synchronized String generateResult (LogInRequest r)
    {
        String e;
        String p;

        e = Security.encrypt(r.getEmailAddress());
        p = Security.encrypt(r.getPassword());

        if (isRequestValid(r)) {
            if (!isLoginDataSuccess(e, p)) {
                return "ERR";
            }
        } else {
            return "ERR";
        }
        return "OK";
    }
    protected int getCode(LogInRequest r) { return generateCode(r);}

    protected synchronized int generateCode(LogInRequest r)
    {
        String e;
        String p;

        e = Security.encrypt(r.getEmailAddress());
        p = Security.encrypt(r.getPassword());

        if(isRequestValid(r)) {
            if (isResultOk(getResult(r))) {
                if(!isLoginDataSuccess(e,p)){
                    return CodesSet.getCode(Codes.LOGIN_FAILED);
                }
            }
        } else {
            return CodesSet.getCode(Codes.REQUEST_PARAMS_EMPTY);
        }
        return CodesSet.getCode(Codes.SUCCESS);
    }

    protected boolean isLoginSuccess(int c) { return c==0;}
    protected boolean isRequestValid(LogInRequest r     ) { return r.getEmailAddress()!= null && r.getPassword() != null;}
    protected boolean isResultOk    (String       result) { return result.equals("OK");}
    protected boolean isLoginDataSuccess(String e, String p) {
        return this.userAccountsRepo.findByEmailAddressAndPwd(e, p) != null
                && this.userAccountsRepo.findByEmailAddressAndPwd(e, p).size() == 1;}

}
