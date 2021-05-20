package com.tasknotification.tasknotification.db;

import java.util.*;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

public interface UserAccountsDao extends Repository<UserAccountsBase, Integer> {
    @Query("SELECT ua FROM UserAccountsBase ua WHERE ua.emailAddress=:emailAddress")
    List<UserAccountsBase> findByEmailAddress(@Param("emailAddress") String s);
    void save(UserAccountsBase userAccounts);
}
