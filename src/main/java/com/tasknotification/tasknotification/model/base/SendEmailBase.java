package com.tasknotification.tasknotification.model.base;

import java.text.*;
import java.util.*;

import com.tasknotification.tasknotification.model.model.DateTimeEntity;

public class SendEmailBase extends DateTimeEntity {
    public SendEmailBase() {
        id       = String.valueOf(System.currentTimeMillis());
        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
