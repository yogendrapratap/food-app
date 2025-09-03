package com.gap.learning.foodapp.feignclient;

import java.math.BigDecimal;

public class UserFundTransferDTO {

    private String userName;
    private BigDecimal amount;
    private Long toAccountName;

    public String getUserName() {
        return userName;
    }

    public UserFundTransferDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UserFundTransferDTO setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Long getToAccountName() {
        return toAccountName;
    }

    public UserFundTransferDTO setToAccountName(Long toAccountName) {
        this.toAccountName = toAccountName;
        return this;
    }
}
