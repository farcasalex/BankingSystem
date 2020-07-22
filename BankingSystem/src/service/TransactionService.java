package service;

import model.entity.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction create(String accountOutId, String accountInId, Double moneyAmount, String type, Long userId);

    Transaction viewById(Long id);

    List<Transaction> viewAll();

    List<Transaction> userReport(Long userId);
}
