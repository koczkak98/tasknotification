package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface TaskDao extends Repository<TaskBase, String> {
    void           save        (TaskBase t );
    List<TaskBase> findByObject(String   s );
    TaskBase       findById    (String   id);
    List<TaskBase> findAll     (           );
    @Query("SELECT t FROM TaskBase t WHERE t.id=:id AND t.object=:object")
    TaskBase findByIdAndObject(@Param("id") String id, @Param("object") String object);
}
