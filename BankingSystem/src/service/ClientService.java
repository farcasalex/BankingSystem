package service;

import model.entity.Client;

import java.util.List;

public interface ClientService {
    Client add(String name, String firsName, String identityCardNumber, String cnp, String address);

    Client update(Client client);

    Client viewById(Long id);

    List<Client> viewAll();
}
