package com.tasknotification.tasknotification.controller;

import java.util.*;

public abstract class CodesSet {
    protected static final int       SUCCESS                      =  0;
    protected static final int       VALIDATION_ERR               =  1;
    protected static final int       EXISTING_EMAIL               =  2;
    protected static final int       UNKNOWN_ERROR                =  3;
    protected static final int       REQUEST_PARAMS_EMPTY         =  4;
    protected static final int       LOGIN_FAILED                 =  5;
    protected static final int       TERM_IS_NOT_VALID            =  6;
    protected static final int       INACTIVE_SESSION_ID          =  7;
    protected static final int       OBJECT_CHAR_INCORRECT        =  8;
    protected static final int       EXISTING_OBJECT_AT_ADDRESSEE =  9;
    protected static final int       MESSAGE_CHAR_INCORRECT       = 10;
    protected static final int       GRACE_PERIOD_INCORRECT       = 11;

    public static Integer getCode(Codes c)
    {
        HashMap<Codes, Integer> e;

        e = createCodes();
        return e.get(c);
    }

    protected static HashMap<Codes, Integer> createCodes() {
        HashMap<Codes, Integer> e;

        e = new HashMap<>();

        e.put(Codes.SUCCESS                     , SUCCESS                     );
        e.put(Codes.VALIDATION_ERR              , VALIDATION_ERR              );
        e.put(Codes.EXISTING_EMAIL              , EXISTING_EMAIL              );
        e.put(Codes.UNKNOWN_ERROR               , UNKNOWN_ERROR               );
        e.put(Codes.REQUEST_PARAMS_EMPTY        , REQUEST_PARAMS_EMPTY        );
        e.put(Codes.LOGIN_FAILED                , LOGIN_FAILED                );
        e.put(Codes.TERM_IS_NOT_VALID           , TERM_IS_NOT_VALID           );
        e.put(Codes.INACTIVE_SESSION_ID         , INACTIVE_SESSION_ID         );
        e.put(Codes.OBJECT_CHAR_INCORRECT       , OBJECT_CHAR_INCORRECT       );
        e.put(Codes.EXISTING_OBJECT_AT_ADDRESSEE, EXISTING_OBJECT_AT_ADDRESSEE);
        e.put(Codes.MESSAGE_CHAR_INCORRECT      , MESSAGE_CHAR_INCORRECT      );
        e.put(Codes.GRACE_PERIOD_INCORRECT      , GRACE_PERIOD_INCORRECT      );

        return e;
    }
}
