package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.dto.CartDTO;
import com.gap.learning.foodapp.dto.CartListDTO;
import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CartRepository cartRepository;

    public CartListDTO addToCart(CartListDTO cartListDTO, Long userId) {
       // userService.getUsersDetailsById( userId);

        cartListDTO.getCartDTO().forEach(cartDTO -> foodService.searchByIdAndVendorId(cartDTO.getItemId(), cartDTO.getVendorId()));

        List<CartDTO> existingCart = cartRepository.findAllByUserId(userId);

        List<CartDTO> toAddList = new java.util.ArrayList<>(cartListDTO.getCartDTO());
        List<CartDTO> finalListToAdd = new java.util.ArrayList<>();

        for (CartDTO newItem : toAddList) {
            boolean matched = false;
            for (CartDTO existing : existingCart) {
                if (existing.getItemId().equals(newItem.getItemId())) {
                    existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                finalListToAdd.add(newItem);
            }
        }

        existingCart.addAll(finalListToAdd);
        List<CartDTO> saved = cartRepository.saveAll(existingCart);
        return new CartListDTO().setCartDTO(saved);
    }

    public FoodList findCartFoodDetails(Long userId) {
        List<CartDTO> cartDTOs = cartRepository.findAllByUserId(userId);

        FoodList foodList = foodService.searchProductListForCart(cartDTOs);

        BigDecimal total = getTotalOfBasket(foodList, cartDTOs);

        assignQuantityFromCartToProductDTO(foodList, cartDTOs);

        foodList.setBasketPrice(total);

        return foodList;
    }

    private void assignQuantityFromCartToProductDTO(FoodList foodList, List<CartDTO> cartDTOs) {


        /*for (Food food : foodList.getFoodItems()) {
            for (CartDTO cartDTO : cartDTOs) {
                if (String.valueOf(cartDTO.getItemId()).equalsIgnoreCase(food.getId())
                        && cartDTO.getVendorId().equalsIgnoreCase(food.getVendorId())) {
                    food.setQuantity(cartDTO.getQuantity());
                    break; // Exit the inner loop once a match is found
                }
            }
            food.setQuantity(0); // Initialize quantity to 0
        }*/

        foodList.getFoodItems().forEach(food -> {
            CartDTO matchCartDTO = cartDTOs.stream()
                    .filter(cartDTO -> String.valueOf(cartDTO.getItemId())
                            .equalsIgnoreCase(food.getId()))
                    .findFirst()
                    .orElse(null);

            int quantity = (matchCartDTO != null) ? matchCartDTO.getQuantity() : 0;
            food.setQuantity(quantity);
        });
    }

    private static BigDecimal getTotalOfBasket(FoodList productListDTO, List<CartDTO> cartDTOs) {
        return productListDTO.getFoodItems().stream()
                .flatMap(food -> cartDTOs.stream()
                        .filter(cartDTO ->
                                String.valueOf(cartDTO.getItemId()).equalsIgnoreCase(food.getId()) &&
                                        cartDTO.getVendorId().equalsIgnoreCase(food.getVendorId())

                        )
                        .map(cartDTO -> food.getPrice().multiply(
                                new BigDecimal(cartDTO.getQuantity()))))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }





    public void cleanUpCartData(Long userId) {
        cartRepository.deleteCartDTOByUserId(userId);
    }
}
