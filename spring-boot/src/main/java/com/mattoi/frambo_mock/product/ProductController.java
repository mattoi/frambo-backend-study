package com.mattoi.frambo_mock.product;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        // return 201 on success
        // return 400 on missing fields
        // return 422 on invalid fields
    }

    @PatchMapping(value = { "" }, params = { "id" })
    void update(@RequestParam("id") Integer id, @ModelAttribute Product product) {
        repository.update(product);
        // return 204 on success
        // return 404 on id not found
        // return 422 on invalid fields
    }

    @GetMapping("")
    List<Product> findAll() {
        return repository.findAll();
        // return 200 on success
    }

    @GetMapping("/in_stock")
    List<Product> findAllInStock() {
        return repository.findAllInStock();
        // return 200 on success
    }

    @GetMapping(value = { "" }, params = { "id" })
    Product findById(@RequestParam(name = "id") Integer id) {
        return repository.findById(id);
        // return 200 on success
        // return 404 on id not found
    }

    // consider not allowing deletion and encouraging removing from stock,
    // since this would mess up the order items table
    /*
     * @DeleteMapping(value = { "" }, params = { "id" })
     * void delete(@RequestParam(name = "id") Integer id) {
     * repository.delete(id);
     * }
     */

    @PostMapping("/categories")
    void createCategory(Category category) {
        repository.createCategory(category);
        // return 201 on success
        // return 400 on missing fields
        // return 422 on invalid fields
    }

    @PatchMapping(value = { "/categories" }, params = { "id" })
    void updateCategory(@RequestParam("id") Integer id, Category category) {
        repository.updateCategory(category);
        // return 204 on success
        // return 404 on id not found
        // return 422 on invalid fields
    }

    @GetMapping("/categories")
    List<Category> findAllCategories() {
        return repository.findAllCategories();
        // return 200 on success
    }

    // same as above
    /*
     * @DeleteMapping(value = { "/categories" }, params = { "id" })
     * void deleteCategory(@RequestParam("id") Integer id) {
     * repository.deleteCategory(id);
     * // return 204 on success
     * // return 404 on id not found
     * }
     */
}
