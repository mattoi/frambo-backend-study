package com.mattoi.frambo_study.product;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("")
    ResponseEntity<?> create(@RequestBody Product product) {
        try {
            return new ResponseEntity<>(service.create(product), HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(value = { "" }, params = { "id" })
    ResponseEntity<?> update(@RequestParam(name = "id") Integer id, @RequestBody Product product) {
        try {
            return new ResponseEntity<>(service.update(id, product), HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("")
    ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/in_stock")
    ResponseEntity<?> findAllInStock() {
        return new ResponseEntity<>(service.findAllInStock(), HttpStatus.OK);
    }

    @GetMapping(value = { "" }, params = { "id" })
    ResponseEntity<?> findById(@RequestParam(name = "id") Integer id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // consider not allowing deletion and encouraging setting inStock to false
    // instead, since this would mess up the order items table.
    /*
     * @DeleteMapping(value = { "" }, params = { "id" })
     * void delete(@RequestParam(name = "id") Integer id) {
     * repository.delete(id);
     * }
     */

    @PostMapping("/categories")
    ResponseEntity<?> createCategory(Category category) {
        try {
            return new ResponseEntity<>(service.createCategory(category), HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(value = { "/categories" }, params = { "id" })
    ResponseEntity<?> updateCategory(@RequestParam("id") Integer id, @RequestBody Category category) {
        try {
            return new ResponseEntity<>(service.updateCategory(id, category), HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/categories")
    ResponseEntity<?> findAllCategories() {
        return new ResponseEntity<>(service.findAllCategories(), HttpStatus.OK);
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
