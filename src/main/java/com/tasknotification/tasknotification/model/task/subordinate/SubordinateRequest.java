package com.tasknotification.tasknotification.model.task.subordinate;

import com.tasknotification.tasknotification.model.model.Request;

public class SubordinateRequest extends Request {
    public String getName         () { return name         ;}
    public String getUserSessionId() { return userSessionId;}

    public void setName         (String name         ) { this.name          = name         ;}
    public void setUserSessionId(String userSessionId) { this.userSessionId = userSessionId;}

    protected String name;
    protected String userSessionId;

    public SubordinateRequest() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
