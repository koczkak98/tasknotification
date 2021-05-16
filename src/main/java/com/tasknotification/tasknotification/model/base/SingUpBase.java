package com.tasknotification.tasknotification.model.base;

import com.tasknotification.tasknotification.model.model.*;

import javax.persistence.*;
import java.text.*;
import java.util.*;

@Entity
@Table(name = "singup")
public class SingUpBase extends DateTimeEntity {
    public SingUpBase() {
        this.id = String.valueOf(System.currentTimeMillis())  ;
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
