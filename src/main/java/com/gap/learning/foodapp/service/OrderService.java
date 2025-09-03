package com.gap.learning.foodapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.dto.Order;
import com.gap.learning.foodapp.dto.UserDTO;
import com.gap.learning.foodapp.feignclient.BankAppClient;
import com.gap.learning.foodapp.feignclient.FundTransferResponseDTO;
import com.gap.learning.foodapp.feignclient.UserFundTransferDTO;
import com.gap.learning.foodapp.repository.OrderRepository;
import com.gap.learning.foodapp.validator.EcommerceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAppClient bankAppClient;

    @Value("${merchant.account.no}")
    private Long merchantAccountNo;

    @Autowired
    private EcommerceValidator ecommerceValidator;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public void placeOrder(Long userId) {
        logger.info("Placing order for user {}%n", userId);

        final FoodList foodList = cartService.findCartFoodDetails(userId);

        logger.info("Product list in the cart is {}", foodList);

        final UserDTO user = userService.getUsersDetailsById(userId);

        final UserFundTransferDTO userFundTransferDTO = buildUserFundTransferDTO(user, foodList);

        logger.info("Fund transfer started for user in Bank App {}", userFundTransferDTO.getUserName());

        final FundTransferResponseDTO fundTransferResponseDTO = bankAppClient.transferFundsForEcommerceAccount(userFundTransferDTO, userId);

        logger.info("Fund transfer response received from bank app for user {}", userFundTransferDTO.getUserName());
        logger.info("Transfer response: {}", fundTransferResponseDTO);

        ecommerceValidator.validate(fundTransferResponseDTO.getAmount(), foodList.getBasketPrice());

        final Order order = buildOrders(userId, fundTransferResponseDTO, foodList, user);

        orderRepository.save(order);
        logger.info("Order saved successfully for user {}", userId);
        cartService.cleanUpCartData(userId);
        logger.info("Cart cleaned up for user {}", userId);
    }

    /**
     * Builds the list of orders for the user and products.
     */
    private static Order buildOrders(Long userId, FundTransferResponseDTO fundTransferResponseDTO, FoodList foodList, UserDTO user) {
        final String transactionId = fundTransferResponseDTO.getTransactionId();
        final LocalDate orderDate = LocalDate.now();
        final String userName = user.getFirstName() + " " + user.getLastName();

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(String.valueOf(System.currentTimeMillis()));
        order.setOrderDate(orderDate);
        order.setTransactionId(transactionId);

        List<Food> foods = foodList.getFoodItems();

        BigDecimal totalOrderPrice = foodList.getFoodItems().stream()
                .map(food -> food.getPrice().multiply(BigDecimal.valueOf(food.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalOrderPrice);
        order.setItems(foods);

        return order;
    }

    private UserFundTransferDTO buildUserFundTransferDTO(UserDTO user, FoodList productListDTO) {
        UserFundTransferDTO dto = new UserFundTransferDTO();
        dto.setUserName(user.getFirstName() + " " + user.getLastName());
        dto.setToAccountName(merchantAccountNo);
        dto.setAmount(productListDTO.getBasketPrice());
        return dto;
    }


    private boolean waitForKafkaResult(CompletableFuture<SendResult<String, String>> future) {
        try {
            future.get();
            return true;
        } catch (Exception e) {
            logger.error("Kafka send failed: {}", e.getMessage(), e);
            return false;
        }
    }

    private LocalDate getRandomDeliveryDate() {
        int daysToAdd = ThreadLocalRandom.current().nextInt(10, 16);
        return LocalDate.now().plusDays(daysToAdd);
    }
}
