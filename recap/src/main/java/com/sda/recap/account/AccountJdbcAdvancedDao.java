package com.sda.recap.account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountJdbcAdvancedDao implements AccountDao {

    public static final String URL = "jdbc:mysql://localhost:3306/hibernate?serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    private static final Logger logger = Logger.getLogger(AccountJdbcAdvancedDao.class.getName());

    public void create(Account account) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // create connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // create statement
            String sql = "INSERT INTO account(name, email, password) VALUES (?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, account.getName());
            statement.setString(2, account.getEmail());
            statement.setString(3, account.getPassword());

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
             PreparedStatement statement = connection.prepareStatement(sql);
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
        String sql = "SELECT * FROM account WHERE id = ?";

        Account result = null;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql)) {

            statement.setLong(1, id);

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
        String sql = "UPDATE account SET name = ?, email = ?, password = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountData.getName());
            statement.setString(2, accountData.getEmail());
            statement.setString(3, accountData.getPassword());
            statement.setLong(4, id);

            // execute
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.severe("failed to update");
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM account WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.execute(sql);
        } catch (SQLException e) {
            logger.severe("failed to delete");
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM account";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.execute(sql);
        } catch (SQLException e) {
            logger.severe("failed to delete");
        }
    }
}
