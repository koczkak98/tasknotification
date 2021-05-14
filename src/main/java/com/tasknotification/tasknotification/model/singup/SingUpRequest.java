package com.tasknotification.tasknotification.model.singup;

public class SingUpRequest {
    public String getId          () { return id          ;}
    public String getEmailAddress() { return emailAddress;}
    public String getPassword    () { return password    ;}
    public String getName        () { return name        ;}

    protected String id          ;
    protected String emailAddress;
    protected String password    ;
    protected String name        ;

    public SingUpRequest(String eMailAddress, String password, String name) {
        this.id           = String.valueOf(System.currentTimeMillis());
        this.emailAddress = eMailAddress                              ;
        this.password     = password                                  ;
        this.name         = name                                      ;
    }
}
