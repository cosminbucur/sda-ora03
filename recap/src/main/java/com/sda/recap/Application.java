package com.sda.recap;

import com.sda.recap.account.Account;
import com.sda.recap.account.AccountDao;
import com.sda.recap.account.AccountJdbcDao;

public class Application {

    public static void main(String[] args) {
        AccountDao accountDao = new AccountJdbcDao();

        Account account = new Account("admin", "admin@test.com", "secret");

        accountDao.create(account);
    }
}
