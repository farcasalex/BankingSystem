package service.impl;

import model.entity.Client;
import repository.ClientRepository;
import service.ClientService;
import java.util.List;
import java.util.logging.Logger;

public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = Logger.getLogger(ClientServiceImpl.class.getName());

    private final ClientRepository clientRepository;


    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client add(String name, String firsName, String identityCardNumber, String cnp, String address) {
        Client client = new Client(name, firsName, identityCardNumber, cnp, address);
        return clientRepository.create(client);
    }

    @Override
    public Client update(Client client) {
        return clientRepository.update(client);
    }

    @Override
    public Client viewById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> viewAll() {
        return clientRepository.findAll();
    }
}
