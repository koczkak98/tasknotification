package com.tasknotification.tasknotification.model.singup;

import com.tasknotification.tasknotification.model.model.Request;

public class SingUpRequest extends Request {

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    protected String name;

    public SingUpRequest() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
