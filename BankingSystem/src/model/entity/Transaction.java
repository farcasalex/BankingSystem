package model.entity;

public class Transaction {
    private Long id;
    private String accountOutId;
    private String accountInId;
    private Double moneyAmount;
    private String type;
    private Long userId;

    public Transaction(String accountOutId, String accountInId, Double moneyAmount, String type, Long userId) {
        this.accountOutId = accountOutId;
        this.accountInId = accountInId;
        this.moneyAmount = moneyAmount;
        this.type = type;
        this.userId = userId;
    }

    public Transaction() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountOutId() {
        return accountOutId;
    }

    public void setAccountOutId(String accountOutId) {
        this.accountOutId = accountOutId;
    }

    public String getAccountInId() {
        return accountInId;
    }

    public void setAccountInId(String accountInId) {
        this.accountInId = accountInId;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountOutId='" + accountOutId + '\'' +
                ", accountInId='" + accountInId + '\'' +
                ", moneyAmount=" + moneyAmount +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                '}';
    }
}
