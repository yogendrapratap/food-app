package com.gap.learning.foodapp.feignclient;

import java.math.BigDecimal;

public class FundTransferResponseDTO {

    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String transactionId;
    private String transactionTimes;

    public FundTransferResponseDTO() {
    }

    public Long getFromAccount() {
        return fromAccount;
    }

    public FundTransferResponseDTO setFromAccount(Long fromAccount) {
        this.fromAccount = fromAccount;
        return this;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public FundTransferResponseDTO setToAccount(Long toAccount) {
        this.toAccount = toAccount;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public FundTransferResponseDTO setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public FundTransferResponseDTO setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getTransactionTimes() {
        return transactionTimes;
    }

    public FundTransferResponseDTO setTransactionTimes(String transactionTimes) {
        this.transactionTimes = transactionTimes;
        return this;
    }

    @Override
    public String toString() {
        return "FundTransferResponseDTO{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", transactionId='" + transactionId + '\'' +
                ", transactionTimes='" + transactionTimes + '\'' +
                '}';
    }
}
