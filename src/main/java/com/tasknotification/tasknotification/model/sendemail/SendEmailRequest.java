package com.tasknotification.tasknotification.model.sendemail;

import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.model.*;

import java.util.*;

public class SendEmailRequest extends Request {
    public String         getSender   () { return sender   ;}
    public String         getAddressee() { return addressee;}
    public List<TaskBase> getLetters  () { return letters  ;}

    public void setSender   (String         sender   ) { this.sender    = sender   ;}
    public void setAddressee(String         addressee) { this.addressee = addressee;}
    public void setLetters  (List<TaskBase> letters  ) { this.letters   = letters  ;}

    public void addLetters   (TaskBase t) { letters.add(t)   ;}
    public void removeLetters(TaskBase t) { letters.remove(t);}

    protected String   sender   ;
    protected String   addressee;
    protected List<TaskBase> letters   ;

    public SendEmailRequest() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
