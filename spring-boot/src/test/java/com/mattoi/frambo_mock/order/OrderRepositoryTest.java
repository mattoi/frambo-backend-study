package com.mattoi.frambo_mock.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_mock.client.Client;
import com.mattoi.frambo_mock.client.ClientRepository;
import com.mattoi.frambo_mock.product.Category;
import com.mattoi.frambo_mock.product.Product;
import com.mattoi.frambo_mock.product.ProductRepository;

@JdbcTest
@Import({ OrderRepository.class, ClientRepository.class, ProductRepository.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    Integer clientId;

    @BeforeEach
    void setup() {
        repository.initializeStatus();
        clientRepository.create(
                new Client(
                        null,
                        "Matheus",
                        "matheus@email.com",
                        "559822222222"));
        clientId = clientRepository.findByPhoneNumber("559822222222").id();
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
    }

    @Test
    public void shouldCreateNewOrder() {
        // var items = new ArrayList<OrderItem>();
        var products = productRepository.findAll();
        var items = List.of(
                new OrderItem(
                        products.get(0).id(),
                        null,
                        products.get(0).price(),
                        2),
                new OrderItem(
                        products.get(1).id(),
                        null,
                        products.get(1).price(),
                        3));
        var result = repository.create(new Order(null,
                clientId,
                items,
                "PAYMENT_PENDING",
                LocalDateTime.now(),
                LocalDateTime.now()));
        assertEquals(true, result);
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
