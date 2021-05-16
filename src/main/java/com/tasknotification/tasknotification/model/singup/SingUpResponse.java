package com.tasknotification.tasknotification.model.singup;

import com.tasknotification.tasknotification.model.model.Response;

public class SingUpResponse extends Response {

    public SingUpResponse() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
