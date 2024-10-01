package com.mattoi.frambo_mock.product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductRepository productRepository;

    public ProductController(ProductRepository repository) {
        this.productRepository = repository;
    }

    // TODO bring product api functions from notes
}
