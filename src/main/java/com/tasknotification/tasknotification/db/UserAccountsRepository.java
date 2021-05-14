package com.tasknotification.tasknotification.db;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.Param;

public interface UserAccountsRepository extends Repository<UserAccountsBase, Integer> {
    @Query("SELECT ua FROM UserAccountsBase ua WHERE ua.emailAddress=:emailAddress")
    UserAccountsBase findByEMailAddress(@Param("emailAddress") String s);
    void save(UserAccountsBase userAccounts);
}
