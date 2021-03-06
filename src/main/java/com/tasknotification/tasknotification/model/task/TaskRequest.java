package com.tasknotification.tasknotification.model.task;

import com.tasknotification.tasknotification.model.model.Request;

public class TaskRequest extends Request {

    public String getUserSessionId  () { return userSessionId  ;}
    public String getObject         () { return object         ;}
    public String getMessage        () { return message        ;}
    public String getTerm           () { return term           ;}
    public int    getGracePeriod    () { return gracePeriod    ;}
    public String getSubordinateId  () { return subordinateId;}

    public void setUserSessionId  (String userSessionId  ) { this.userSessionId   = userSessionId  ;}
    public void setObject         (String object         ) { this.object          = object         ;}
    public void setMessage        (String message        ) { this.message         = message        ;}
    public void setTerm           (String term           ) { this.term            = term           ;}
    public void setGracePeriod    (int    gracePeriod    ) { this.gracePeriod     = gracePeriod    ;}
    public void setSubordinateId  (String subordinateId  ) { this.subordinateId   = subordinateId  ;}

    protected String userSessionId ;
    protected String object        ;
    protected String message       ;
    protected String term          ;
    protected int    gracePeriod   ;
    protected String subordinateId;

    public TaskRequest() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
