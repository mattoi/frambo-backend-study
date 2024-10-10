package com.mattoi.frambo_mock.order;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Integer id,
        Integer clientId,
        Float totalAmount,
        List<OrderItem> items,
        String status,
        LocalDateTime dateCreated,
        LocalDateTime lastUpdated) {
}

/*
 * enum OrderStatus {
 * PAYMENT_PENDING,
 * SHIPPED,
 * DELIVERED,
 * CANCELED
 * }
 */