package com.mattoi.frambo_mock.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(OrderRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository repository;

    @BeforeEach
    void setup() {
        // TODO add a client, a category and 2 products
    }

    @Test
    public void shouldCreateNewOrder() {
        // TODO
        assertEquals(true, false);
    }

    @Test
    public void shouldUpdateOrderStatus() {
        // TODO
        assertEquals(true, false);
    }

    @Test
    public void shouldFindAllOrders() {
        assertEquals(true, false);
    }

    @Test
    public void shouldFindOrderById() {
        // TODO
        assertEquals(true, false);
    }

    @Test
    public void shouldFindOrdersByClientId() {
        // TODO
        assertEquals(true, false);
    }

    @Test
    public void shouldFindOrdersByStatus() {
        // TODO
        assertEquals(true, false);
    }

}
