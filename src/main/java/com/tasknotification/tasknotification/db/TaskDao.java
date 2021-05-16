package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.repository.*;

public interface TaskDao extends Repository<TaskBase, String> {
    void save(TaskBase t);
}
