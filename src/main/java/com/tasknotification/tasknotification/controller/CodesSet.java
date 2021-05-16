package com.tasknotification.tasknotification.controller;

import java.util.*;

public abstract class CodesSet {
    protected static final int       SUCCESS              = 0;
    protected static final int       VALIDATION_ERR       = 1;
    protected static final int       EXISTING_EMAIL       = 2;
    protected static final int       PASSWORD_INAPT       = 3;
    protected static final int       REQUEST_PARAMS_EMPTY = 4;
    protected static final int       LOGIN_FAILED         = 5;
    protected static final int       TERM_IS_NOT_VALID    = 6;
    protected static final int       INACTIVE_SESSION_ID  = 7;

    public static Integer getCode(Codes c)
    {
        HashMap<Codes, Integer> e;

        e = createCodes();
        return e.get(c);
    }

    protected static HashMap<Codes, Integer> createCodes() {
        HashMap<Codes, Integer> e;

        e = new HashMap<>();

        e.put(Codes.SUCCESS             , SUCCESS             );
        e.put(Codes.VALIDATION_ERR      , VALIDATION_ERR      );
        e.put(Codes.EXISTING_EMAIL      , EXISTING_EMAIL      );
        e.put(Codes.PASSWORD_INAPT      , PASSWORD_INAPT      );
        e.put(Codes.REQUEST_PARAMS_EMPTY, REQUEST_PARAMS_EMPTY);
        e.put(Codes.LOGIN_FAILED        , LOGIN_FAILED        );
        e.put(Codes.TERM_IS_NOT_VALID   , TERM_IS_NOT_VALID   );
        e.put(Codes.INACTIVE_SESSION_ID , INACTIVE_SESSION_ID );

        return e;
    }
}
