package com.tasknotification.tasknotification.model.model;

import javax.persistence.*;

@MappedSuperclass
public class DateTimeEntity extends BaseEntity {
    public String getDateTime() { return dateTime;}

    public void setDateTime(String dateTime) { this.dateTime = dateTime;}
    @Column(name = "date_time")
    protected String dateTime;
}
