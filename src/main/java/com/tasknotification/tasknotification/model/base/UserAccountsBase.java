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
    public List<String> getLogInIds      () { return logInIds    ;}

    public void setId          (String       id          ) { this.id           = id          ;}
    public void setEmailAddress(String       eMailAddress) { this.emailAddress = eMailAddress;}
    public void setPassWord    (String       passWord    ) { this.passWord     = passWord    ;}
    public void setName        (String       name        ) { this.name         = name        ;}
    public void setSingUpId    (SingUpBase   singUpId    ) { this.singUpId     = singUpId    ;}
    public void setLogInIds    (List<String> logInIds    ) { this.logInIds     = logInIds    ;}

    public void addLoginId     (String       loginId     ) {this.logInIds.add(loginId)       ;}

    @Id
    @Column
    protected String id             ;
    @Column(name = "email_address")
    protected String emailAddress;
    @Column(name = "password")
    protected String passWord       ;
    @Column(name = "name")
    protected String name           ;
    @OneToOne
    @JoinColumn(name = "singup_id", referencedColumnName = "id")
    protected SingUpBase singUpId ;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "login_x_user_accounts_mapping",
            joinColumns = @JoinColumn(name = "user_accounts_id")
    )
    @Column(name = "login_id")
    protected List<String> logInIds ;

    public UserAccountsBase() {
        this.logInIds = new ArrayList<>();
    }

}