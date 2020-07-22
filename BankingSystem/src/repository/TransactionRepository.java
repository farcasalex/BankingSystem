package repository;

import model.entity.Transaction;
import java.util.List;

public interface TransactionRepository {
    Transaction create(Transaction transaction);

    Transaction update(Transaction transaction);

    boolean delete(Long id);

    Transaction findById(Long id);

    List<Transaction> findAll();

    List<Transaction> findAllByUserId(Long userId);
}
