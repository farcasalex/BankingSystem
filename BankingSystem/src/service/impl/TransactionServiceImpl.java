package service.impl;

import model.entity.Transaction;
import repository.TransactionRepository;
import service.TransactionService;
import java.util.List;
import java.util.logging.Logger;

public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = Logger.getLogger(TransactionServiceImpl.class.getName());

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(String accountOutId, String accountInId, Double moneyAmount, String type, Long userId) {
        Transaction transaction = new Transaction(accountOutId, accountInId, moneyAmount, type, userId);
        return  transactionRepository.create(transaction);
    }

    @Override
    public Transaction viewById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> viewAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> userReport(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }
}
