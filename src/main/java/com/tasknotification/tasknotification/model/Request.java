package com.tasknotification.tasknotification.model;

public abstract class Request {
    public String getId          () { return id          ;}
    public String getEmailAddress() { return emailAddress;}
    public String getPassword    () { return password    ;}

    public void setId          (String id          ) { this.id           = id;}
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress;}
    public void setPassword    (String password    ) { this.password     = password;}

    protected String id          ;
    protected String emailAddress;
    protected String password    ;

}
