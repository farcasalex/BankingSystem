package tests;

import org.junit.Assert;
import org.junit.Test;
import repository.AccountRepository;
import repository.ClientRepository;
import repository.TransactionRepository;
import repository.UserRepository;
import repository.impl.*;
import service.AccountService;
import service.ClientService;
import service.TransactionService;
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.ClientServiceImpl;
import service.impl.TransactionServiceImpl;
import service.impl.UserServiceImpl;
import util.AccountIdGenerator;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomTests {
    @Test
    public void generateAccountId(){
        AccountIdGenerator generator = new AccountIdGenerator();
        for (int i = 0; i < 10; i++){
            System.out.println(generator.randomRoId());
        }
    }

    @Test
    public void currentDate(){
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(formatter.format(date));
    }

    @Test
    public void databaseConnection(){
        // connector
        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper("assig1");
        try {
            System.out.println("Successfully connected : " + connectionWrapper.testConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // repository
        UserRepository userRepository = new UserRepositoryImpl(connectionWrapper);
        ClientRepository clientRepository = new ClientRepositoryImpl(connectionWrapper);
        AccountRepository accountRepository = new AccountRepositoryImpl(connectionWrapper);
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(connectionWrapper);

        // service
        UserService userService = new UserServiceImpl(userRepository);
        ClientService clientService = new ClientServiceImpl(clientRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);

        userService.create("admin", "password", true);
        userService.create("user", "password", false);

        Assert.assertNull(userService.login("admin", "parola"));
        Assert.assertNotNull(userService.login("admin", "password"));
        Assert.assertNull(userService.login("user", "parola"));
        Assert.assertNotNull(userService.login("user", "password"));
    }
}
