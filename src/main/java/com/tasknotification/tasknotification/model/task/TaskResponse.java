package com.tasknotification.tasknotification.model.task;

import com.tasknotification.tasknotification.model.model.Response;

public class TaskResponse extends Response {
    public TaskResponse() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
