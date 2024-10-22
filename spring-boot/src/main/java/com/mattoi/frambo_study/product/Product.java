package com.mattoi.frambo_study.product;

public record Product(
                Integer id,
                String name,
                String description,
                String photoUrl,
                Integer netWeight,
                Double price,
                Boolean inStock,
                String category) {
}