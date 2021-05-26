package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.repository.*;

public interface SendEmailDao extends Repository<SendEmailBase, String> {
    SendEmailBase findById(String id);
    void          save(SendEmailBase s);
}
