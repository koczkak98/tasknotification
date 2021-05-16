package com.tasknotification.tasknotification.model.base;

import com.tasknotification.tasknotification.model.model.*;

import javax.persistence.*;
import java.text.*;
import java.util.*;

@Entity
@Table(name = "login")
public class LoginBase extends DateTimeEntity {
    public LoginBase() {
        this.id       = String.valueOf(System.currentTimeMillis()) ;
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}