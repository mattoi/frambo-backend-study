package com.mattoi.frambo_mock.product;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    void create(@ModelAttribute Product product) {
        repository.create(product);
    }

    @PutMapping("")
    void update(@ModelAttribute Product product) {
        repository.update(product);
    }

    // todo? put setProductAvailability(id, inStock)

    @GetMapping("")
    List<Product> findAll() {
        return repository.findAll();
    }

    // TODO use a parameter instead of a whole different method
    @GetMapping("/in_stock")
    List<Product> findAllInStock() {
        return repository.findAllInStock();
    }

    @GetMapping(value = { "" }, params = { "id" })
    Product findById(@RequestParam(name = "id") Integer id) {
        return repository.findById(id);
    }

    @DeleteMapping(value = { "" }, params = { "id" })
    void delete(@RequestParam(name = "id") Integer id) {
        repository.delete(id);
    }

    @PostMapping("/categories")
    void createCategory(Category category) {
        repository.createCategory(category);
    }

    @PutMapping("/categories")
    void updateCategory(Category category) {
        repository.updateCategory(category);
    }

    @GetMapping("/categories")
    List<Category> findAllCategories() {
        return repository.findAllCategories();
    }

    @DeleteMapping(value = { "/categories" }, params = { "id" })
    void deleteCategory(@RequestParam(name = "id") Integer id) {
        repository.deleteCategory(id);
    }
}
