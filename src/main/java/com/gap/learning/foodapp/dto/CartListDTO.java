package com.gap.learning.foodapp.dto;

import java.util.List;

public class CartListDTO {

    private List<CartDTO> cartDTO;

    public List<CartDTO> getCartDTO() {
        return cartDTO;
    }

    public CartListDTO setCartDTO(List<CartDTO> cartDTO) {
        this.cartDTO = cartDTO;
        return this;
    }
}
