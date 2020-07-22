package repository.impl;

import model.entity.Transaction;
import repository.TransactionRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;

    public TransactionRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }

    @Override
    public Transaction create(Transaction transaction) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transaction (account_out_id, account_in_id, money_amount, type, userid) VALUES(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,transaction.getAccountOutId());
            preparedStatement.setString(2,transaction.getAccountInId());
            preparedStatement.setDouble(3, transaction.getMoneyAmount());
            preparedStatement.setString(4, transaction.getType());
            preparedStatement.setLong(5, transaction.getUserId());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()) {
                transaction.setId(resultSet.getLong(1));
                return transaction;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction update(Transaction transaction) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET account_out_id=?, account_in_id=?, money_amount=?, type=?, userid=? WHERE id=?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,transaction.getAccountOutId());
            preparedStatement.setString(2,transaction.getAccountInId());
            preparedStatement.setDouble(3, transaction.getMoneyAmount());
            preparedStatement.setString(4, transaction.getType());
            preparedStatement.setLong(5, transaction.getUserId());
            preparedStatement.setLong(6, transaction.getId());

            int changedRows = preparedStatement.executeUpdate();

            if(changedRows > 0) {
                return transaction;
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM transaction WHERE id= ?");
            preparedStatement.setLong(1, id);

            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Transaction findById(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE id=?");
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setId(resultSet.getLong(1));
                transaction.setAccountOutId(resultSet.getString(2));
                transaction.setAccountInId(resultSet.getString(3));
                transaction.setMoneyAmount(resultSet.getDouble(4));
                transaction.setType(resultSet.getString(5));
                transaction.setUserId(resultSet.getLong(6));

                return transaction;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Transaction> transactions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transaction");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setId(resultSet.getLong(1));
                transaction.setAccountOutId(resultSet.getString(2));
                transaction.setAccountInId(resultSet.getString(3));
                transaction.setMoneyAmount(resultSet.getDouble(4));
                transaction.setType(resultSet.getString(5));
                transaction.setUserId(resultSet.getLong(6));

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> findAllByUserId(Long userId) {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Transaction> transactions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE transaction.userid = ?");
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setId(resultSet.getLong(1));
                transaction.setAccountOutId(resultSet.getString(2));
                transaction.setAccountInId(resultSet.getString(3));
                transaction.setMoneyAmount(resultSet.getDouble(4));
                transaction.setType(resultSet.getString(5));
                transaction.setUserId(resultSet.getLong(6));

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
