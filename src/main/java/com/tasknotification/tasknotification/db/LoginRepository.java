package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.repository.*;

public interface LoginRepository extends Repository<LoginBase, String> {
    void save(LoginBase l);
}
