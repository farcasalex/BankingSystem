package repository;

import model.entity.Client;
import java.util.List;

public interface ClientRepository {
    Client create(Client client);

    Client update(Client client);

    boolean delete(Long id);

    Client findById(Long id);

    List<Client> findAll();
}
