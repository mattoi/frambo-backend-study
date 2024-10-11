package com.mattoi.frambo_mock.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_mock.customer.Customer;
import com.mattoi.frambo_mock.customer.CustomerRepository;
import com.mattoi.frambo_mock.product.Category;
import com.mattoi.frambo_mock.product.Product;
import com.mattoi.frambo_mock.product.ProductRepository;

@JdbcTest
@Import({ OrderRepository.class, CustomerRepository.class, ProductRepository.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private CustomerRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    Integer clientId;
    List<Product> products = new ArrayList<Product>();
    List<Order> testOrders = new ArrayList<Order>();

    @BeforeEach
    void setup() {
        repository.initializeStatus();
        clientRepository.create(
                new Customer(
                        null,
                        "Matheus",
                        "matheus@email.com",
                        "559822222222"));
        clientRepository.create(
                new Customer(
                        null,
                        "Cecilia",
                        "cecilia@email.com",
                        "559811111111"));
        productRepository.createCategory(new Category(null, "Cookie"));
        productRepository.create(new Product(null,
                "Cookie Original",
                "Cookie à base de manteiga com gotas de chocolate",
                null,
                120,
                14.00,
                true,
                "Cookie"));

        productRepository.create(new Product(
                null,
                "Cookie pink lemonade",
                "Cookie à base de limão siciliano com gotas de Ruby Chocolate",
                null,
                120,
                12.00,
                true,
                "Cookie"));
        clientId = clientRepository.findByPhoneNumber("559822222222").id();
        products = productRepository.findAll();
        testOrders = List.of(new Order(null,
                clientId,
                List.of(
                        new OrderItem(
                                products.get(0).id(),
                                null,
                                products.get(0).price(),
                                2),
                        new OrderItem(
                                products.get(1).id(),
                                null,
                                products.get(1).price(),
                                3)),
                "PAYMENT_PENDING",
                LocalDateTime.now(),
                LocalDateTime.now()),
                new Order(null,
                        clientId + 1,
                        List.of(
                                new OrderItem(
                                        products.get(0).id(),
                                        null,
                                        products.get(0).price(),
                                        1),
                                new OrderItem(
                                        products.get(1).id(),
                                        null,
                                        products.get(1).price(),
                                        1)),
                        "DELIVERED",
                        LocalDateTime.now(),
                        LocalDateTime.now()));
    }

    @Test
    public void shouldCreateNewOrder() {
        var result = repository.create(testOrders.get(0));
        assertEquals(true, result);
    }

    @Test
    public void shouldUpdateOrderStatus() {
        repository.create(testOrders.get(0));
        var orderId = repository.findAllByClientId(clientId).get(0).id();
        var result = repository.updateOrderStatus(orderId, "SHIPPED");
        assertEquals(true, result);
    }

    @Test
    public void shouldFindAllOrders() {
        repository.create(testOrders.get(0));
        repository.create(testOrders.get(1));
        var orders = repository.findAll();
        assertEquals(2, orders.size());
    }

    @Test
    public void shouldFindOrderById() {
        repository.create(testOrders.get(0));
        var orderId = repository.findAll().get(0).id();
        var savedOrder = repository.findById(orderId);
        assertEquals(orderId, savedOrder.id());
    }

    @Test
    public void shouldFindOrdersByClientId() {
        repository.create(testOrders.get(0));
        repository.create(testOrders.get(1));
        var ordersFromFirstCustomer = repository.findAllByClientId(testOrders.get(0).customerId());
        assertEquals(1, ordersFromFirstCustomer.size());
    }

    @Test
    public void shouldFindOrdersByStatus() {
        repository.create(testOrders.get(0));
        repository.create(testOrders.get(1));
        var pendingOrders = repository.findAllByStatus("PAYMENT_PENDING");
        assertEquals(1, pendingOrders.size());
    }

}
