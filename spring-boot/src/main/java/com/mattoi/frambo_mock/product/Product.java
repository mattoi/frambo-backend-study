package com.mattoi.frambo_mock.product;

public record Product(
        Integer id,
        String name,
        String description,
        String photoUrl,
        String category,
        Integer netWeight,
        Float price,
        Boolean inStock) {
}