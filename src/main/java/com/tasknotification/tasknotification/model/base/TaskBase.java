package com.tasknotification.tasknotification.model.base;

import com.tasknotification.tasknotification.model.model.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "task")
public class TaskBase extends DateTimeEntity {
    public String getObject         () { return object         ;}
    public String getMessage        () { return message        ;}
    public String getTerm           () { return term           ;}
    public int    getGracePeriod    () { return gracePeriod    ;}
    public String getAddresseeMail  () { return addresseeMail  ;}

    public void setObject         (String object         ) { this.object          = object         ;}
    public void setMessage        (String message        ) { this.message         = message        ;}
    public void setTerm           (String term           ) { this.term            = term           ;}
    public void setGracePeriod    (int    gracePeriod    ) { this.gracePeriod     = gracePeriod    ;}
    public void setAddresseeMail  (String addresseeMail  ) { this.addresseeMail   = addresseeMail  ;}

    @Column
    protected String object         ;
    @Column
    protected String message        ;
    @Column
    protected String term           ;
    @Column(name = "grace_period")
    protected int    gracePeriod    ;
    @Column(name= "addressee_email")
    protected String addresseeMail  ;

    public TaskBase() {
        id       = String.valueOf(System.currentTimeMillis());
        dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
