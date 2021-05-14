package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.repository.*;

public interface SingUpRepository extends Repository<SingUpBase, Integer> {
    void save(SingUpBase singUpBase);
}
