package repository;

import model.entity.Account;
import java.util.List;

public interface AccountRepository {
    Account create(Account account);

    Account update(Account account);

    boolean delete(Long id);

    Account findById(Long id);

    Account findByAccountId(String id);

    List<Account> findAll();
}
