package com.mattoi.frambo_mock.order;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
                Integer id,
                Integer clientId,
                List<OrderItem> items,
                String status,
                LocalDateTime dateCreated,
                LocalDateTime lastUpdated) {

        public Double totalAmount() {
                Double totalAmount = 0.0;
                for (OrderItem item : items) {
                        totalAmount += item.productPrice() * item.quantity();
                }
                return totalAmount;
        }
}

/*
 * enum OrderStatus {
 * PAYMENT_PENDING,
 * SHIPPED,
 * DELIVERED,
 * CANCELED
 * }
 */