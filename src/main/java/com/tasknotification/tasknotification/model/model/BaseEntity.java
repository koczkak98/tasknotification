package com.tasknotification.tasknotification.model.model;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {
    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    @Id
    @Column
    protected String id;
}
