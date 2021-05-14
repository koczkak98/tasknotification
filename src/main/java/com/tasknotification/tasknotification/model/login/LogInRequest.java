package com.tasknotification.tasknotification.model.login;

public class LogInRequest {
    public String getId           () { return id           ;}
    public String getDateTime     () { return dateTime     ;}
    public String getEmailAddress () { return emailAddress ;}
    public String getPassword     () { return password     ;}
    public String getName         () { return name         ;}

    protected String id           ;
    protected String dateTime     ;
    protected String emailAddress ;
    protected String password     ;
    protected String name         ;

    public LogInRequest(String dateTime, String userAccountId, String emailAddress, String password, String name) {
        this.id            = String.valueOf(System.currentTimeMillis());
        this.dateTime      = dateTime                                  ;
        this.emailAddress  = emailAddress                              ;
        this.password      = password                                  ;
        this.name          = name                                      ;
    }
}
