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

    // TODO post create(product fields..)
    // TODO put update(product fields..)
    // TODO put setProductAvailability(id, inStock)
    // TODO get findAll()
    // TODO get findAllInStock()
    // TODO get findById(id)
    // TODO delete delete(id)
    // TODO post createCategory(name)
    // TODO put updateCategory(id, newName)
    // TODO get findAllCategories()
    // TODO delete deleteCategory(id)
}
