package com.ecommerceapi.ecommerceapi.dto;

import java.util.List;

public class CartRequestDTO {

    private List<Long> productIds;

    public List<Long> getProductIds() {
        return productIds;
    }

    public CartRequestDTO setProductIds(List<Long> productIds) {
        this.productIds = productIds;
        return this;
    }
}
