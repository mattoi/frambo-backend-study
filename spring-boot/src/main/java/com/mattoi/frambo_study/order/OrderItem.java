package com.mattoi.frambo_study.order;

public record OrderItem(
                Integer productId,
                String productName,
                Double productPrice,
                Integer quantity) {
}
