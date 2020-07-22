package repository.impl;

import model.entity.Account;
import repository.AccountRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public AccountRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }

    @Override
    public Account create(Account account) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account (clientid, account_id, type, money_amount, date_of_creation) VALUES(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, account.getClientId());
            preparedStatement.setString(2, account.getAccountId());
            preparedStatement.setString(3, account.getType());
            preparedStatement.setDouble(4, account.getMoneyAmount());
            preparedStatement.setString(5, account.getDateOfCreation());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()) {
                account.setId(resultSet.getLong(1));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account update(Account account) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET clientid=?, account_id=?, type=?, money_amount=?, date_of_creation=? WHERE id=?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, account.getClientId());
            preparedStatement.setString(2, account.getAccountId());
            preparedStatement.setString(3, account.getType());
            preparedStatement.setDouble(4, account.getMoneyAmount());
            preparedStatement.setString(5, account.getDateOfCreation());
            preparedStatement.setLong(6, account.getId());

            int changedRows = preparedStatement.executeUpdate();

            if(changedRows > 0) {
                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM account WHERE id= ?");
            preparedStatement.setLong(1, id);

            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Account findById(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE id=?");
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Account account = new Account();

                account.setId(resultSet.getLong(1));
                account.setClientId(resultSet.getLong(2));
                account.setAccountId(resultSet.getString(3));
                account.setType(resultSet.getString(4));
                account.setMoneyAmount(resultSet.getDouble(5));
                account.setDateOfCreation(resultSet.getString(6));

                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Account findByAccountId(String id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE account_id=?");
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Account account = new Account();

                account.setId(resultSet.getLong(1));
                account.setClientId(resultSet.getLong(2));
                account.setAccountId(resultSet.getString(3));
                account.setType(resultSet.getString(4));
                account.setMoneyAmount(resultSet.getDouble(5));
                account.setDateOfCreation(resultSet.getString(6));

                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Account> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Account account = new Account();

                account.setId(resultSet.getLong(1));
                account.setClientId(resultSet.getLong(2));
                account.setAccountId(resultSet.getString(3));
                account.setType(resultSet.getString(4));
                account.setMoneyAmount(resultSet.getDouble(5));
                account.setDateOfCreation(resultSet.getString(6));

                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
