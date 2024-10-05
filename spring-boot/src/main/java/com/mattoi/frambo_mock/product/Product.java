package com.mattoi.frambo_mock.product;

public record Product(
                int id,
                String name,
                String description,
                String photoUrl,
                int netWeight,
                Float price,
                Boolean inStock,
                String category) {
}