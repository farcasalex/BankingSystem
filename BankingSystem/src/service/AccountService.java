package service;

import model.entity.Account;
import java.util.List;

public interface AccountService {
    Account create(Long clientId, String type);

    Account update(Account account);

    Boolean delete(Long id);

    Account viewById(Long id);

    Account viewByAccountId(String id);

    List<Account> viewAll();

    Account addMoney(Long id, Double amount);

    Account takeMoney(Long id, Double amount);
}
