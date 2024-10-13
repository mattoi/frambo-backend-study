package com.mattoi.frambo_mock.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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
    }

    @PutMapping("")
    void update(@ModelAttribute Customer updatedCustomer) {
        repository.update(updatedCustomer);
    }

    @GetMapping("")
    List<Customer> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = { "" }, params = { "id" })
    Customer findById(@RequestParam(name = "id") Integer id) {
        return repository.findById(id);
    }

    @DeleteMapping(value = { "" }, params = { "id" })
    void delete(@RequestParam(name = "id") Integer id) {
        repository.delete(id);
    }

}
