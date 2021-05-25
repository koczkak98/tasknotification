package com.tasknotification.tasknotification.model.base;

import javax.persistence.*;
import java.text.*;
import java.util.*;

import com.tasknotification.tasknotification.model.model.*;

@Entity
@Table(name = "task")
public class TaskBase extends DateTimeEntity {
    public String       getObject         () { return object         ;}
    public String       getMessage        () { return message        ;}
    public String       getTerm           () { return term           ;}
    public int          getGracePeriod    () { return gracePeriod    ;}
    public List<String> getSubordinate_ids() { return subordinate_ids;}
    public List<String> getSendingEmailIds() { return sendingEmailIds;}

    public void setObject        (String       object          ) { this.object         = object          ;}
    public void setMessage       (String       message         ) { this.message        = message         ;}
    public void setTerm          (String       term            ) { this.term           = term            ;}
    public void setGracePeriod   (int          gracePeriod     ) { this.gracePeriod    = gracePeriod     ;}
    public void setSubordinate_ids(List<String> subordinate_ids) { this.subordinate_ids = subordinate_ids;}
    public void setSendingEmailIds(List<String> sendingEmailIds) { this.sendingEmailIds = sendingEmailIds; }

    public void addSubordinate_id(String id) { subordinate_ids.add(id);}
    public void addSendingEmailId(String id) { sendingEmailIds.add(id);}

    @Column
    protected String object         ;
    @Column
    protected String message        ;
    @Column
    protected String term           ;
    @Column(name = "grace_period")
    protected int    gracePeriod    ;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "subordinate_x_task_mapping",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name= "subordinate_id")
    protected List<String> subordinate_ids;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "sending_email_x_task_mapping",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name= "sending_email_id")
    protected List<String> sendingEmailIds;

    public TaskBase() {
        id              = String.valueOf(System.currentTimeMillis());
        dateTime        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        subordinate_ids = new ArrayList<>();
        sendingEmailIds = new ArrayList<>();
    }
}
