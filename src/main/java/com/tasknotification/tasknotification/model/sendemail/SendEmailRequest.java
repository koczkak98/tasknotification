package com.tasknotification.tasknotification.model.sendemail;

import com.tasknotification.tasknotification.model.base.*;
import com.tasknotification.tasknotification.model.model.*;

import java.util.*;

public class SendEmailRequest extends Request {
    public String   getSender   () { return sender   ;}
    public String   getAddressee() { return addressee;}
    public TaskBase getLetter  () { return letter  ;}

    public void setSender   (String         sender   ) { this.sender    = sender   ;}
    public void setAddressee(String         addressee) { this.addressee = addressee;}
    public void setLetter   (TaskBase       letters  ) { this.letter    = letter  ;}

    protected String   sender   ;
    protected String   addressee;
    protected TaskBase letter   ;

    public SendEmailRequest() {
        id = String.valueOf(System.currentTimeMillis());
    }
}
