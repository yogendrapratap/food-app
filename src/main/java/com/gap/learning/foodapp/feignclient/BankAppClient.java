package com.gap.learning.foodapp.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "bankAppClient", url = "localhost:8090")
@FeignClient(name ="http://APP-BANK/appbank/" )
public interface BankAppClient {

    @PostMapping("/online-amount-transfer/{userId}")
    public FundTransferResponseDTO transferFundsForEcommerceAccount(@RequestBody UserFundTransferDTO userFundTransfer,
                                                                    @PathVariable("userId") Long userId);
}
