package com.tasknotification.tasknotification.controller.singup;

import com.tasknotification.tasknotification.controller.Codes;
import com.tasknotification.tasknotification.controller.CodesSet;
import com.tasknotification.tasknotification.controller.Security;
import com.tasknotification.tasknotification.db.SingUpDao;
import com.tasknotification.tasknotification.db.UserAccountsDao;
import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.singup.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SingUpController {
    @Autowired
    protected UserAccountsDao userAccountsRepo;
    @Autowired
    protected SingUpDao singUpRepo      ;

    @GetMapping("/sendsingup")
    public SingUpResponse getSingUp(@RequestParam("emailaddress") String emailAddress,
                                    @RequestParam("password") String password,
                                    @RequestParam("name") String name)
    {
        SingUpRequest    r;
        SingUpResponse   s;
        SingUpBase       i;
        UserAccountsBase a;

        r      = getSingUpRequest(emailAddress, password, name);
        s      = getSingUpResponse(r);
        i      = new SingUpBase();

        if (isSingUpSuccess(s.getCode())) {
            saveSingUpBase(i);
            a = getUserAccountsBase(r,i);
            saveUserAccountsBase(a);
        }
        return s;
    }


    public void saveUserAccountsBase(UserAccountsBase a) { userAccountsRepo.save(a);}
    public void saveSingUpBase      (SingUpBase       s) { singUpRepo      .save(s);}

    public UserAccountsBase getUserAccountsBase(SingUpRequest r, SingUpBase s) { return createUserAccountsBase(r,s);}

    protected UserAccountsBase createUserAccountsBase(SingUpRequest r, SingUpBase s)
    {
        UserAccountsBase a;

        a = new UserAccountsBase();

        a.setEmailAddress(Security.encrypt(r.getEmailAddress()));
        a.setPassWord(Security.encrypt(r.getPassword()));
        a.setName(r.getName());
        a.setSingUpId(s);
        a.addLoginId(null);

        return a;
    }

    public SingUpRequest getSingUpRequest(String e, String pwd, String n) { return createSingUpRequest(e, pwd, n);}

    protected SingUpRequest createSingUpRequest(String e, String pwd, String n)
    {
        SingUpRequest r;
        r = new SingUpRequest();
        r.setEmailAddress(e);
        r.setPassword(pwd);
        r.setName(n);

        return  r;
    }

    public SingUpResponse getSingUpResponse(SingUpRequest r) { return createSingUpResponse(r);}

    protected SingUpResponse createSingUpResponse(SingUpRequest r)
    {
        SingUpResponse s;
        String e;
        String p;

        s = new SingUpResponse();
        e = r.getEmailAddress();
        p = r.getPassword();

        s.setReqId(r.getId());

        if (isRequestValid(r)) {
            if (existsEmailAddress(e)) {
                s.setResult("ERR");
                s.setCode(CodesSet.getCode(Codes.EXISTING_EMAIL));
                s.setMessage(Codes.EXISTING_EMAIL.toString());
            } else if (emailAddressIsNotValid(e)) {
                s.setResult("ERR");
                s.setCode(CodesSet.getCode(Codes.VALIDATION_ERR));
                s.setMessage(Codes.VALIDATION_ERR.toString());
            } else if (!isPasswordCharsEnough(p)) {
                s.setResult("ERR");
                s.setCode(CodesSet.getCode(Codes.PASSWORD_INAPT));
                s.setMessage(Codes.PASSWORD_INAPT.toString());
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

    protected boolean isSingUpSuccess       (int           c) {return c==0;}
    protected boolean isRequestValid        (SingUpRequest r) { return r.getEmailAddress() != null && r.getPassword() != null && r.getName() != null;}
    protected boolean existsEmailAddress    (String s       ) { return userAccountsRepo.findByEmailAddress(Security.encrypt(s)).size() > 0;}
    protected boolean emailAddressIsNotValid(String s       ) { return s.equals("validationErr")                     ;}
    protected boolean isPasswordCharsEnough (String s       ) { return s.length()>=8                                 ;}
}