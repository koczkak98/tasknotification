package com.tasknotification.tasknotification.controller.singup;

import com.tasknotification.tasknotification.controller.*;
import com.tasknotification.tasknotification.db.SingUpDao;
import com.tasknotification.tasknotification.db.UserAccountsDao;
import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.singup.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class SingUpController {
    @Autowired
    protected UserAccountsDao userAccountsRepo;
    @Autowired
    protected SingUpDao singUpRepo      ;

    @GetMapping("/sendsingup")
    public SingUpResponse getSingUp(@RequestParam("emailaddress") String emailAddress,
                                    @RequestParam("name") String name)
    {
        SingUpRequest    r;
        SingUpResponse   s;
        SingUpBase       i;
        UserAccountsBase a;

        r      = getSingUpRequest(emailAddress, name);
        s      = getSingUpResponse(r);
        i      = new SingUpBase();

        try {
            if (isSingUpSuccess(s.getCode())) {
                saveSingUpBase(i);
                a = getUserAccountsBase(r, i);
                saveUserAccountsBase(a);
            }
        } catch (Exception e) {
            s = createSingUpResponse(s, Results.ERR, Codes.UNKNOWN_ERROR, "[ " + Codes.UNKNOWN_ERROR.toString() + " ]: " + e.getMessage());
        }
        return s;
    }

    public SingUpRequest getSingUpRequest(String e, String n) { return createSingUpRequest(e, n);}
    protected SingUpRequest createSingUpRequest(String e, String n)
    {
        SingUpRequest r;
        r = new SingUpRequest();
        r.setEmailAddress(e);
        r.setName(n);

        return  r;
    }

    public SingUpResponse getSingUpResponse(SingUpRequest r) { return createSingUpResponse(r);}
    protected SingUpResponse createSingUpResponse(SingUpRequest r)
    {
        SingUpResponse s;

        s = new SingUpResponse();

        s.setReqId(r.getId());
        s = getSingUpResponse(r, s);

        return s;
    }
    protected SingUpResponse getSingUpResponse(SingUpRequest r, SingUpResponse s) { return checkSingUpResponse(r, s);}
    protected SingUpResponse checkSingUpResponse(SingUpRequest r, SingUpResponse s)
    {
        String e;

        e = r.getEmailAddress();

        if (isRequestValid(r)) {
            if (existsEmailAddress(e)) {
                s = createSingUpResponse(s, Results.ERR, Codes.EXISTING_EMAIL, "");
            } else if (emailAddressIsNotValid(e)) {
                s = createSingUpResponse(s, Results.ERR, Codes.VALIDATION_ERR, "");
            } else {
                s = createSingUpResponse(s, Results.OK, Codes.SUCCESS, "");
            }
        } else {
            s = createSingUpResponse(s, Results.ERR, Codes.REQUEST_PARAMS_EMPTY, "");
        }

        return s;
    }
    protected SingUpResponse createSingUpResponse(SingUpResponse s, Results r, Codes c, String m)
    {
        s.setResult(r.toString());
        s.setCode(CodesSet.getCode(c));
        s.setMessage(m);

        return s;
    }

    protected boolean isRequestValid        (SingUpRequest r) { return r.getEmailAddress() != null && r.getName() != null;}
    protected boolean existsEmailAddress    (String        s) { return userAccountsRepo.findByEmailAddress(s).size() > 0;}
    protected boolean emailAddressIsNotValid(String        s) { return s.equals("validationErr")                     ;}

    protected boolean isSingUpSuccess       (int           c) { return c==0;}

    protected UserAccountsBase getUserAccountsBase(SingUpRequest r, SingUpBase s) { return createUserAccountsBase(r,s);}
    protected UserAccountsBase createUserAccountsBase(SingUpRequest r, SingUpBase s)
    {
        UserAccountsBase a;

        a = new UserAccountsBase();

        a.setEmailAddress(r.getEmailAddress());
        a.setName(r.getName());
        a.setSingUpId(s);

        return a;
    }

    public void saveSingUpBase      (SingUpBase       s) { singUpRepo      .save(s);}
    public void saveUserAccountsBase(UserAccountsBase a) { userAccountsRepo.save(a);}
}