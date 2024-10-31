package com.mattoi.frambo_study.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattoi.frambo_study.customer.Customer;
import com.mattoi.frambo_study.customer.CustomerService;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;
import com.mattoi.frambo_study.product.Category;
import com.mattoi.frambo_study.product.Product;
import com.mattoi.frambo_study.product.ProductService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Product> products = new ArrayList<Product>();
    List<Order> testOrders = new ArrayList<Order>();

    @BeforeEach
    void setup() throws InvalidRequestException, EntityNotFoundException {
        // service.initializeStatus();
        var firstCustomerId = customerService.create(
                new Customer(
                        null,
                        "Matheus",
                        "testmatheus@email.com",
                        "55598222222222"));
        var secondCustomerId = customerService.create(
                new Customer(
                        null,
                        "Cecilia",
                        "testcecilia@email.com",
                        "55598111111111"));
        productService.createCategory(new Category(null, "Test Cookie"));
        productService.create(new Product(null,
                "Test Cookie Original",
                "tCookie à base de manteiga com gotas de chocolate",
                null,
                120,
                14.00,
                true,
                "Test Cookie"));

        productService.create(new Product(
                null,
                "Test Cookie pink lemonade",
                "tCookie à base de limão siciliano com gotas de Ruby Chocolate",
                null,
                120,
                12.00,
                true,
                "Test Cookie"));
        products = productService.findAll();
        testOrders = List.of(new Order(null,
                firstCustomerId,
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
                        secondCustomerId,
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
        try {
            mockMvc.perform(post("/api/orders").content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidOrder() {
        try {
            mockMvc.perform(post("/api/orders")
                    .content(objectMapper.writeValueAsString(new Order(null, null, null, null, null, null))))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldUpdateOrderStatus() {
        try {
            var orderId = mockMvc
                    .perform(post("/api/orders").content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andReturn().getResponse().getContentAsString();
            var result = mockMvc.perform(patch("/api/orders?id=" + orderId).content("SHIPPED"))
                    .andExpect(status().isNoContent());
            assertEquals(true, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnInvalidId() {
        try {
            mockMvc.perform(patch("/api/orders?id=0").content("SHIPPED"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateWithInvalidStatus() {
        try {
            var orderId = mockMvc
                    .perform(post("/api/orders").content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andReturn().getResponse().getContentAsString();
            mockMvc.perform(patch("/api/orders?id=" + orderId).content("MADE UP STATUS"))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllOrders() {
        try {
            mockMvc.perform(get("/api/orders")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindOrderById() {
        try {
            var orderId = mockMvc
                    .perform(post("/api/orders").content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andReturn().getResponse().getContentAsString();
            mockMvc.perform(get("/api/orders?id=" + orderId)).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindInvalidId() {
        try {
            mockMvc.perform(get("/api/orders?id=0")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindOrdersByCustomerId() {
        try {
            mockMvc
                    .perform(post("/api/orders").content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andReturn().getResponse().getContentAsString();
            mockMvc.perform(get("/api/orders?customer_id=" + testOrders.get(0).customerId()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindInvalidCustomerId() {
        try {

            mockMvc.perform(get("/api/orders?customer_id=0")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindOrdersByStatus() {
        try {
            mockMvc
                    .perform(post("/api/orders").content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andReturn().getResponse().getContentAsString();
            mockMvc.perform(get("/api/orders?status=PAYMENT_PENDING")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

    }

    @Test
    public void shouldNotFindInvalidStatus() {
        try {
            mockMvc.perform(get("/api/orders?status=STATUS_THAT_DOESNT_EXIST")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
