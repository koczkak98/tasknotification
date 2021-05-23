package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface TaskDao extends Repository<TaskBase, String> {
    void save(TaskBase t);
    List<TaskBase> findByObject(String s);
}
