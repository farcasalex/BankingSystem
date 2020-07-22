package model.entity;

public class Account {
    private Long id;
    private Long clientId;
    private String accountId;
    private String type;
    private Double moneyAmount;
    private String dateOfCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accounttId) {
        this.accountId = accounttId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", accountId='" + accountId + '\'' +
                ", type='" + type + '\'' +
                ", moneyAmount=" + moneyAmount +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                '}';
    }
}
