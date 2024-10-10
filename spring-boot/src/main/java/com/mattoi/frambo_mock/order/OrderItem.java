package com.mattoi.frambo_mock.order;

public record OrderItem(
        Integer productId,
        String productName,
        Integer quantity) {
}
