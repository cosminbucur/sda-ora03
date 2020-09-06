package com.sda.recap;

import com.sda.recap.account.Account;
import com.sda.recap.account.AccountDao;
import com.sda.recap.account.AccountHibernateDao;
import com.sda.recap.account.AccountJdbcAdvancedDao;
import com.sda.recap.account.AccountJdbcDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDaoIntegrationTest {

    public AccountDao accountDao = new AccountHibernateDao();
//    public AccountDao accountDao = new AccountJdbcAdvancedDao();
//    public AccountDao accountDao = new AccountJdbcDao();

    @BeforeEach
    void setUp() {
        accountDao.deleteAll();
    }

    @AfterEach
    void afterEach() {
        accountDao.deleteAll();
    }

    @Test
    void givenAccount_whenCRUD_thenOk() {
        // given
        // account dao

        // 2 accounts
        Account account1 = new Account("alex", "alex@gmail.com", "secret");
        Account account2 = new Account("ana", "ana@gmail.com", "secret");

        // when
        // create account
        accountDao.create(account1);
        accountDao.create(account2);

        // find all
        List<Account> accounts = accountDao.findAll();
        assertThat(accounts).isNotEmpty();
        assertThat(accounts.size()).isEqualTo(2);

        // find by id
        Account firstAccount = accounts.get(0);
        Long firstAccountId = firstAccount.getId();

        Account foundFirstAccount = accountDao.findById(firstAccountId);
        assertThat(foundFirstAccount).isNotNull();

        // update id 1
        Account newAccountData = new Account("test", "test@test.com", "new");
        accountDao.update(firstAccountId, newAccountData);
        // search updated account 1
        Account updatedFirstAccount = accountDao.findById(firstAccountId);

        // assert that account 1 values are modified
        // assert actual = expected
        assertThat(updatedFirstAccount.getName()).isEqualTo(newAccountData.getName());
        assertThat(updatedFirstAccount.getEmail()).isEqualTo(newAccountData.getEmail());
        assertThat(updatedFirstAccount.getPassword()).isEqualTo(newAccountData.getPassword());

        // delete id 2
        Account secondAccount = accounts.get(1);
        accountDao.delete(secondAccount.getId());

        List<Account> finalAccountList = accountDao.findAll();
        // then
        // assert that 1 account left
        assertThat(finalAccountList).hasSize(1);
    }

    // given
    // account dao
    // 2 accounts

    // when
    // create account
    // create account
    // find all
    // find by id
    // update id 1
    // delete id 2

    // then
    // assert that 1 account left
    // assert that account 1 values are modified

    @Test
    void givenState_whenMethodUnderTest_thenResult() {
        // given
        Account account = new Account("alex", "alex@gmail.com", "secret");

        // when
        accountDao.create(account);

        // then
    }
}
