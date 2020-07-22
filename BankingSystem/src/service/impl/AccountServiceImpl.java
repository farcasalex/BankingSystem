package service.impl;

import model.entity.Account;
import repository.AccountRepository;
import service.AccountService;
import util.AccountIdGenerator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class.getName());

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Account create(Long clientId, String type) {
        Account account = new Account();
        AccountIdGenerator generator = new AccountIdGenerator();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        account.setClientId(clientId);
        account.setType(type);
        account.setAccountId(generator.randomRoId());
        account.setDateOfCreation(formatter.format(date));
        account.setMoneyAmount((double) 0);

        return accountRepository.create(account);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.update(account);
    }

    @Override
    public Boolean delete(Long id) {
        return accountRepository.delete(id);
    }

    @Override
    public Account viewById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account viewByAccountId(String id) {
        return accountRepository.findByAccountId(id);
    }

    @Override
    public List<Account> viewAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account addMoney(Long id, Double amount) {
        Account account = accountRepository.findById(id);
        account.setMoneyAmount(account.getMoneyAmount() + amount);
        return accountRepository.update(account);
    }

    @Override
    public Account takeMoney(Long id, Double amount) {
        Account account = accountRepository.findById(id);
        account.setMoneyAmount(account.getMoneyAmount() - amount);
        return accountRepository.update(account);
    }
}
