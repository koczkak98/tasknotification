package com.tasknotification.tasknotification.model.base;

import javax.persistence.*;
import java.text.*;
import java.util.*;

@Entity
@Table(name = "singup")
public class SingUpBase {
    public String getId      () { return id      ;}
    public String getDateTime() { return dateTime;}

    public void setId      (String id      ) { this.id       = id      ;}
    public void setDateTime(String dateTime) { this.dateTime = dateTime;}

    @Id
    @Column
    protected String id      ;
    @Column(name = "date_time")
    protected String dateTime;

    public SingUpBase() {
        this.id = String.valueOf(System.currentTimeMillis())  ;
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
