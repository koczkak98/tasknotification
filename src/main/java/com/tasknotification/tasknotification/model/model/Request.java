package com.tasknotification.tasknotification.model.model;

public abstract class Request {
    public String getId          () { return id          ;}
    public String getEmailAddress() { return emailAddress;}

    public void setId          (String id          ) { this.id           = id;}
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress;}

    protected String id          ;
    protected String emailAddress;

}
