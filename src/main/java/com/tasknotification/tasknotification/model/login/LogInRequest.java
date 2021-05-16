package com.tasknotification.tasknotification.model.login;

import com.tasknotification.tasknotification.model.model.Request;

public class LogInRequest extends Request {

    public LogInRequest() {
        id                 = String.valueOf(System.currentTimeMillis());
    }
}
