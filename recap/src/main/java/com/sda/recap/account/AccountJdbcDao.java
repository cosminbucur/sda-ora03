package com.sda.recap.account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountJdbcDao implements AccountDao {

    public static final String URL = "jdbc:mysql://localhost:3306/hibernate?serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    private static final Logger logger = Logger.getLogger(AccountJdbcDao.class.getName());

    public void create(Account account) {
        Connection connection = null;
        Statement statement = null;
        try {
            // create connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // create statement
            statement = connection.createStatement();
            String sql = "INSERT INTO account(name, email, password) VALUES "
                + "('" + account.getName() + "','" + account.getEmail() + "','" + account.getPassword() + "')";

            // execute
            statement.execute(sql);

            // * process response (if needed)
        } catch (SQLException e) {
            // handle jdbc exceptions
            logger.severe("failed to create account");
        } catch (Exception e) {
            // handle other exceptions
            logger.severe("something went wrong");
        } finally {
            // close resources
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                logger.severe("failed to close statement");
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.severe("failed to close connection");
            }
        }
    }

    public List<Account> findAll() {
        String sql = "SELECT * FROM account";

        List<Account> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // iterate result set and get all values
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                logger.info(id + ", " + name + ", " + email + ", " + password);

                Account account = new Account(name, email, password);
                account.setId(id);
                logger.info(account.toString());
                result.add(account);
            }
        } catch (SQLException e) {
            logger.severe("failed to update");
        }

        return result;
    }

    public Account findById(Long id) {
        String sql = "SELECT * FROM account WHERE id = " + id;

        Account result = null;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                logger.info(id + ", " + name + ", " + email + ", " + password);

                result = new Account(name, email, password);
                result.setId(id);
                logger.info(result.toString());
            }
        } catch (SQLException e) {
            logger.severe("failed to update");
        }
        return result;
    }

    public void update(Long id, Account accountData) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "UPDATE account SET name = '" + accountData.getName() + "', " +
                "email = '" + accountData.getEmail() + "', " +
                "password = '" + accountData.getPassword() +
                "' WHERE id = " + id;

            // execute
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.severe("failed to update");
        }
    }

    public void delete(Long id) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "DELETE FROM account WHERE id = " + id;

            statement.execute(sql);
        } catch (SQLException e) {
            logger.severe("failed to delete");
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "DELETE FROM account";

            statement.execute(sql);
        } catch (SQLException e) {
            logger.severe("failed to delete");
        }
    }
}
