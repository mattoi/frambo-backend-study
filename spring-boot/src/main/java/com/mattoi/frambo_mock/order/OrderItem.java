package com.mattoi.frambo_mock.order;

public record OrderItem(
                Integer productId,
                String productName,
                Double productPrice,
                Integer quantity) {
}
