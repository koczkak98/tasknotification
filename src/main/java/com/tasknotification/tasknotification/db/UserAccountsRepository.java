package com.tasknotification.tasknotification.db;

import java.util.*;

import com.tasknotification.tasknotification.model.base.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

public interface UserAccountsRepository extends Repository<UserAccountsBase, Integer> {
    @Query("SELECT ua FROM UserAccountsBase ua WHERE ua.emailAddress=:emailAddress")
    List<UserAccountsBase> findByEmailAddress(@Param("emailAddress") String s);
    @Query("SELECT ua FROM UserAccountsBase ua WHERE ua.emailAddress=:emailAddress AND ua.passWord=:password")
    List<UserAccountsBase> findByEmailAddressAndPwd(@Param("emailAddress") String e, @Param("password") String password);
    void save(UserAccountsBase userAccounts);
}
