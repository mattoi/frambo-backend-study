package com.mattoi.frambo_mock.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    void create(@ModelAttribute Customer newCustomer) {
        repository.create(newCustomer);
        // return 201 on success
        // return 400 on missing fields
        // return 422 on invalid fields
    }

    @PatchMapping(value = { "" }, params = { "id" })
    void update(@RequestParam(name = "id") Integer id, @ModelAttribute Customer updatedCustomer) {
        repository.update(updatedCustomer);
        // return 204 on success
        // return 404 on id not found
        // return 422 on invalid fields
    }

    @GetMapping("")
    List<Customer> findAll() {
        return repository.findAll();
        // return 200 on success
    }

    @GetMapping(value = { "" }, params = { "id" })
    Customer findById(@RequestParam(name = "id") Integer id) {
        return repository.findById(id);
        // return 200 on success
        // return 404 on id not found
    }

    // consider avoid deleting customers
    /*
     * @DeleteMapping(value = { "" }, params = { "id" })
     * void delete(@RequestParam(name = "id") Integer id) {
     * repository.delete(id);
     * }
     */

}
