package com.tasknotification.tasknotification.model.task.subordinate;

import com.tasknotification.tasknotification.model.model.Response;

public class SubordinateResponse extends Response {
    public SubordinateResponse() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
