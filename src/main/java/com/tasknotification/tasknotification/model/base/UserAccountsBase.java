package com.tasknotification.tasknotification.model.base;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user_accounts")
public class UserAccountsBase {
    public String       getId            () { return id          ;}
    public String       getEmailAddress  () { return emailAddress;}
    public String       getPassWord      () { return passWord    ;}
    public String       getName          () { return name        ;}
    public SingUpBase   getSingUpId      () { return singUpId    ;}

    public void setId          (String       id          ) { this.id           = id          ;}
    public void setEmailAddress(String       eMailAddress) { this.emailAddress = eMailAddress;}
    public void setPassWord    (String       passWord    ) { this.passWord     = passWord    ;}
    public void setName        (String       name        ) { this.name         = name        ;}
    public void setSingUpId    (SingUpBase   singUpId    ) { this.singUpId     = singUpId    ;}

    @Id
    @Column
    protected String id             ;
    @Column(name = "email_address")
    protected String emailAddress;
    @Column(name = "password")
    protected String passWord       ;
    @Column(name = "name")
    protected String name           ;

    /**
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "login_x_user_accounts_mapping",
            joinColumns = @JoinColumn(name = "login_id")
    )
    protected List<String> logInIds ;
    */
    @OneToOne
    @JoinColumn(name = "singup_id", referencedColumnName = "id")
    protected SingUpBase singUpId ;
}