package repository.impl;

import model.entity.Client;
import repository.ClientRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public ClientRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }

    @Override
    public Client create(Client client) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO client (firstname, name, identity_card_number, cnp, address) VALUES(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getIdentityCardNumber());
            preparedStatement.setString(4, client.getCnp());
            preparedStatement.setString(5, client.getAddress());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()) {
                client.setId(resultSet.getLong(1));
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Client update(Client client) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE client SET firstname=?, name=?, identity_card_number=?, cnp=?, address=? WHERE id=?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getIdentityCardNumber());
            preparedStatement.setString(4, client.getCnp());
            preparedStatement.setString(5, client.getAddress());
            preparedStatement.setLong(6, client.getId());

            int changedRows = preparedStatement.executeUpdate();

            if(changedRows > 0) {
                return client;
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM client WHERE id= ?");
            preparedStatement.setLong(1, id);

            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Client findById(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE id=?");
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client();

                client.setId(resultSet.getLong(1));
                client.setFirstName(resultSet.getString(2));
                client.setName(resultSet.getString(3));
                client.setIdentityCardNumber(resultSet.getString(4));
                client.setCnp(resultSet.getString(5));
                client.setAddress(resultSet.getString(6));

                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Client> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Client> clients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();

                client.setId(resultSet.getLong(1));
                client.setFirstName(resultSet.getString(2));
                client.setName(resultSet.getString(3));
                client.setIdentityCardNumber(resultSet.getString(4));
                client.setCnp(resultSet.getString(5));
                client.setAddress(resultSet.getString(6));

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
