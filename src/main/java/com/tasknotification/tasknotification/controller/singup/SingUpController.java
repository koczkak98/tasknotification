package com.tasknotification.tasknotification.controller.singup;

import com.tasknotification.tasknotification.controller.Security;
import com.tasknotification.tasknotification.db.SingUpRepository;
import com.tasknotification.tasknotification.db.UserAccountsRepository;
import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.singup.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SingUpController {
    @Autowired
    protected UserAccountsRepository userAccountsRepo        ;
    @Autowired
    protected SingUpRepository singUpRepo              ;

    protected static final int       VALIDATION_ERR       = 1;
    protected static final int       EXISTING_EMAIL       = 2;
    protected static final int       PASSWORD_INAPT       = 3;
    protected static final int       REQUEST_PARAMS_EMPTY = 4;

    @GetMapping("/sendsingup")
    public SingUpResponse getSingUp(@RequestParam("emailaddress") String emailAddress,
                                    @RequestParam("password") String password,
                                    @RequestParam("name") String name)
    {
        SingUpRequest    r;
        SingUpResponse   s;
        SingUpBase       i;
        UserAccountsBase a;

        r      = new SingUpRequest(emailAddress, password, name);
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

        a.setId(String.valueOf(System.currentTimeMillis()));
        a.setEmailAddress(Security.encrypt(r.getEmailAddress()));
        a.setPassWord(Security.encrypt(r.getPassword()));
        a.setName(r.getName());
        a.setSingUpId(s);

        return a;
    }

    public SingUpResponse getSingUpResponse(SingUpRequest r) { return createSingUpResponse(r);}

    protected SingUpResponse createSingUpResponse(SingUpRequest r)
    {
        SingUpResponse s;

        s = new SingUpResponse();
        s.setId(generateAndGetId());
        s.setReqId(r.getId());
        s.setResult(generateAndGetResult(r));
        s.setCode(generateAndGetCode(r));

        return s;
    }

    protected synchronized String generateAndGetId()
    {
        return String.valueOf(System.currentTimeMillis());
    }

    protected synchronized String generateAndGetResult (SingUpRequest r)
    {
        String e;
        String p;

        e = r.getEmailAddress();
        p = r.getPassword();

        if (isRequestValid(r)) {
            if (existsEmailAddress(e) || emailAddressIsNotValid(e) || !isPasswordCharsEnough(p)) {
                return "ERR";
            }
        } else {
            return "ERR";
        }
        return "OK";
    }

    protected synchronized int generateAndGetCode(SingUpRequest r)
    {
        String e;
        String p;

        e = r.getEmailAddress();
        p = r.getPassword();
        if(isRequestValid(r)) {
            if (!isResultOk(e)) {
                if (existsEmailAddress(e)) {
                    return EXISTING_EMAIL;
                } else if (emailAddressIsNotValid(e)) {
                    return VALIDATION_ERR;
                } else if (!isPasswordCharsEnough(p)) {
                    return PASSWORD_INAPT;
                }
            }
        } else {
            return REQUEST_PARAMS_EMPTY;
        }
        return 0;
    }

    protected boolean isSingUpSuccess       (int           c) {return c==0;}
    protected boolean isRequestValid        (SingUpRequest r) { return r.getEmailAddress() != null && r.getPassword() != null && r.getName() != null;}
    protected boolean isResultOk            (String result  ) { return result.equals("OK")                           ;}
    protected boolean existsEmailAddress    (String s       ) { return userAccountsRepo.findByEMailAddress(s) != null;}
    protected boolean emailAddressIsNotValid(String s       ) { return s.equals("validationErr")                     ;}
    protected boolean isPasswordCharsEnough (String s       ) { return s.length()>=8                                 ;}
}