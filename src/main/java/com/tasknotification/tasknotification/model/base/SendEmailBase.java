package com.tasknotification.tasknotification.model.base;

import java.text.*;
import java.util.*;
import javax.persistence.*;

import com.tasknotification.tasknotification.model.model.DateTimeEntity;

public class SendEmailBase extends DateTimeEntity {

    public SubordinateBase getSubordinateId() { return subordinateId;}

    public void setSubordinateId(SubordinateBase subordinateId) { this.subordinateId = subordinateId;}

    @OneToOne
    @JoinColumn(name = "subordinate_id", referencedColumnName = "id")
    protected SubordinateBase subordinateId ;
    public SendEmailBase() {
        id       = String.valueOf(System.currentTimeMillis());
        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
