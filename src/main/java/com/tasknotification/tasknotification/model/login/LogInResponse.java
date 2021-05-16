package com.tasknotification.tasknotification.model.login;

import com.tasknotification.tasknotification.model.model.Response;

public class LogInResponse extends Response {

    public String getSessionId() { return sessionId;}

    public void setSessionId(String sessionId) { this.sessionId = sessionId;}

    protected String sessionId;


    public LogInResponse() {
        id = String.valueOf(System.currentTimeMillis());
    }
}