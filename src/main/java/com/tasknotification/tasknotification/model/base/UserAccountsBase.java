package com.tasknotification.tasknotification.model.base;

import com.tasknotification.tasknotification.model.model.BaseEntity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user_accounts")
public class UserAccountsBase extends BaseEntity {
    public String       getEmailAddress  () { return emailAddress;}
    public String       getName          () { return name        ;}
    public SingUpBase   getSingUpId      () { return singUpId    ;}
    public List<String> getLogInIds      () { return logInIds    ;}
    public List<String> getTaskIds       () { return taskIds     ;}

    public void setEmailAddress(String       eMailAddress) { this.emailAddress = eMailAddress;}
    public void setName        (String       name        ) { this.name         = name        ;}
    public void setSingUpId    (SingUpBase   singUpId    ) { this.singUpId     = singUpId    ;}
    public void setLogInIds    (List<String> logInIds    ) { this.logInIds     = logInIds    ;}
    public void setTaskIds     (List<String> taskIds     ) { this.taskIds      = taskIds     ;}

    public void addLoginId     (String       loginId     ) {this.logInIds.add(loginId)       ;}
    public void addTaskId      (String       taskId      ) {this.taskIds .add(taskId )       ;}

    @Column(name = "email_address")
    protected String emailAddress;
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
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "task_x_user_accounts_mapping",
            joinColumns = @JoinColumn(name = "user_accounts_id")
    )
    @Column(name = "task_id")
    protected List<String> taskIds ;
    public UserAccountsBase() {
        id            = String.valueOf(System.currentTimeMillis());
        this.logInIds = new ArrayList<>();
        this.taskIds  = new ArrayList<>();
    }

}