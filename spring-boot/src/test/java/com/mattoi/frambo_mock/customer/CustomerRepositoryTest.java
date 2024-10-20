package com.mattoi.frambo_mock.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(CustomerRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    void setup() {
        repository.create(
                new Customer(
                        null,
                        "Cecilia",
                        "cecilia@email.com",
                        "559811111111"));
        repository.create(
                new Customer(
                        null,
                        "Matheus",
                        "matheus@email.com",
                        "559822222222"));
    }

    @Test
    public void shouldCreateNewCustomer() {
        repository.create(new Customer(null, "Ayla", "ayla@email.com", "559833333333"));
        var customers = repository.findAll();
        assertEquals(3, customers.size());
    }

    @Test
    public void shouldFindAllCustomers() {
        List<Customer> customers = repository.findAll();
        assertEquals(2, customers.size());
    }

    // It's not possible to test findById() with this test framework because
    // the id of the @BeforeEach customers keeps changing, so I'm using
    // findByPhoneNumber to get the ID first
    @Test
    public void shouldFindByPhoneNumber() {
        var customer = repository.findByPhoneNumber("559811111111");
        assertEquals("Cecilia", customer.name());
    }

    @Test
    public void shouldFindById() {
        var customerFromPhoneNumber = repository.findByPhoneNumber("559811111111");
        var customerFromId = repository.findById(customerFromPhoneNumber.id());
        assertEquals(customerFromId, customerFromPhoneNumber);
    }

    @Test
    public void shouldUpdateClient() {
        var client = repository.findByPhoneNumber("559811111111");
        repository.update(new Customer(client.id(), "Matheus Soares", client.email(), client.phoneNumber()));
        var updatedClient = repository.findByPhoneNumber("559811111111");
        assertEquals(updatedClient.name(), "Matheus Soares");
    }

    @Test
    public void shouldDeleteClient() {
        var customer = repository.findByPhoneNumber("559811111111");
        repository.delete(customer.id());
        var customers = repository.findAll();
        assertEquals(1, customers.size());
    }
}
