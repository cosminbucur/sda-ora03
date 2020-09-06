package com.sda.recap.account;

import com.sda.recap.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountHibernateDao implements AccountDao {

    private static final Logger logger = Logger.getLogger(AccountHibernateDao.class.getName());

    public void create(Account account) {
        // create session
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // begin transaction
            transaction = session.beginTransaction();

            // execute operations
            // * if error -> rollback transaction
            session.save(account);

            // commit
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        // close session
    }

    public List<Account> findAll() {
        List<Account> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // similar to select * from account
            // hibernate query language HQL
            String hql = "from Account";
            Query query = session.createQuery(hql);
            result = query.getResultList();
        } catch (Exception e) {
            logger.severe("failed to find all");
        }

        return result;
    }

    public Account findById(Long id) {
        Account result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            result = session.find(Account.class, id);
        } catch (Exception e) {
            logger.severe("failed to find");
        }
        return result;
    }

    public void update(Long id, Account accountData) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Account accountToUpdate = findById(id);
            accountToUpdate.setName(accountData.getName());
            accountToUpdate.setEmail(accountData.getEmail());
            accountToUpdate.setPassword(accountData.getPassword());

            transaction = session.beginTransaction();

            session.update(accountToUpdate);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Account accountToDelete = findById(id);

            transaction = session.beginTransaction();

            // need an account object with id = ?
            session.delete(accountToDelete);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void deleteAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM Account";
            Query query = session.createQuery(hql);
            query.executeUpdate();

//            List<Account> allAccounts = findAll();
//            for (Account account : allAccounts) {
//                session.delete(account.getId());
//            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
