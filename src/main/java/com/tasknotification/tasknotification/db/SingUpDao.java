package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.repository.*;

public interface SingUpDao extends Repository<SingUpBase, String> {
    void save(SingUpBase singUpBase);
}
