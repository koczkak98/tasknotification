package com.tasknotification.tasknotification.model.base;

import javax.persistence.*;
import java.text.*;
import java.util.*;

@Entity
@Table(name = "login")
public class LoginBase {
    public String getId      () { return id      ;}
    public String getDateTime() { return dateTime;}

    public void setId      () { this.id       = String.valueOf(System.currentTimeMillis())                           ;}
    public void setDateTime() { this.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());}

    @Id
    @Column
    protected String id      ;
    @Column(name = "date_time")
    protected String dateTime;
}