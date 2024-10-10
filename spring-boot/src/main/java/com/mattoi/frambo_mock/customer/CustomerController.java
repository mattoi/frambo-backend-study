package com.mattoi.frambo_mock.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }
    // TODO post create(customer fields..)
    // TODO put update(customer fields..)
    // TODO get findAll()
    // TODO get findById(id)
    // TODO delete delete(id)
}
