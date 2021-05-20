package com.tasknotification.tasknotification.model.task;

import com.tasknotification.tasknotification.model.model.Request;

public class TaskRequest extends Request {

    public String getUserSessionId  () { return userSessionId  ;}
    public String getObject         () { return object         ;}
    public String getMessage        () { return message        ;}
    public String getTerm           () { return term           ;}
    public int    getGraceTimePeriod() { return graceTimePeriod;}
    public String getAddresseeEmail () { return addresseeEmail ;}

    public void setUserSessionId  (String userSessionId  ) { this.userSessionId   = userSessionId  ;}
    public void setObject         (String object         ) { this.object          = object         ;}
    public void setMessage        (String message        ) { this.message         = message        ;}
    public void setTerm           (String term           ) { this.term            = term           ;}
    public void setGraceTimePeriod(int    graceTimePeriod) { this.graceTimePeriod = graceTimePeriod;}
    public void setAddresseeEmail (String addresseeEmail ) { this.addresseeEmail  = addresseeEmail ;}

    protected String userSessionId  ;
    protected String object         ;
    protected String message        ;
    protected String term           ;
    protected int    graceTimePeriod;
    protected String addresseeEmail;

    public TaskRequest() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
