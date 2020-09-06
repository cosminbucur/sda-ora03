package com.sda.recap.account;

import java.util.List;

public interface AccountDao {

    // CRUD

    // command vs query

    void create(Account account);

    List<Account> findAll();

    Account findById(Long id);

    void update(Long id, Account accountData);

    void delete(Long id);

    void deleteAll();
}
