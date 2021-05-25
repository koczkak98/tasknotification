package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface SubordinateDao extends Repository<SubordinateBase, String> {
    void save(SubordinateBase s);
    SubordinateBase findById(String id);
    @Query("SELECT s FROM SubordinateBase s WHERE s.emailAddress=:emailAddress")
    List<SubordinateBase> findByEmailAddress(@Param("emailAddress") String s);
}
