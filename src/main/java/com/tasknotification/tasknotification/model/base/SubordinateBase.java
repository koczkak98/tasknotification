package com.tasknotification.tasknotification.model.base;

import com.tasknotification.tasknotification.controller.*;
import com.tasknotification.tasknotification.model.model.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "subordinate")
public class SubordinateBase extends BaseEntity {

    public String       getEmailAddress   () { return emailAddress   ;}
    public String       getName           () { return name           ;}

    public void setEmailAddress   (String       emailAddress   ) { this.emailAddress    = emailAddress   ;}
    public void setName           (String       name           ) { this.name            = name           ;}


    @Column(name= "email_address")
    protected String emailAddress;
    @Column
    protected String name        ;

    public SubordinateBase() {
        id              = Cfg.getSubordinateIdCount();
    }
}
