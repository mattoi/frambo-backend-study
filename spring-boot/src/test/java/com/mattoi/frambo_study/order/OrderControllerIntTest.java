package com.mattoi.frambo_study.order;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@WebMvcTest(OrderController.class)
public class OrderControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @Autowired
    private ObjectMapper objectMapper;

    List<Order> testOrders = List.of(new Order(null,
            1,
            List.of(
                    new OrderItem(
                            1,
                            null,
                            12.00,
                            2),
                    new OrderItem(
                            2,
                            null,
                            14.00,
                            3)),
            "PAYMENT_PENDING",
            LocalDateTime.now(),
            LocalDateTime.now()),
            new Order(null,
                    2,
                    List.of(
                            new OrderItem(
                                    1,
                                    null,
                                    12.00,
                                    1),
                            new OrderItem(
                                    2,
                                    null,
                                    14.00,
                                    1)),
                    "DELIVERED",
                    LocalDateTime.now(),
                    LocalDateTime.now()));

    @Test
    public void shouldCreateNewOrder() {
        try {
            when(service.create(testOrders.get(0))).thenReturn(1);
            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testOrders.get(0))))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidOrder() {
        try {
            Order invalidOrder = new Order(null, null, null, null, null, null);
            when(service.create(invalidOrder))
                    .thenThrow(new InvalidRequestException(List.of("There are no fields"), null));
            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidOrder)))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldUpdateOrderStatus() {
        try {
            when(service.updateOrderStatus(1, "SHIPPED")).thenReturn(true);
            mockMvc.perform(patch("/api/orders?id=1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"status\": \"SHIPPED\"}"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnInvalidId() {
        try {
            when(service.updateOrderStatus(0, "SHIPPED"))
                    .thenThrow(new EntityNotFoundException("Couldn't find an order with ID 0", null));
            mockMvc.perform(patch("/api/orders?id=0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"status\": \"SHIPPED\"}"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateWithInvalidStatus() {
        try {
            when(service.updateOrderStatus(1, "MADE_UP_STATUS"))
                    .thenThrow(new InvalidRequestException(List.of("Invalid status"), null));
            mockMvc.perform(patch("/api/orders?id=1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"status\": \"MADE_UP_STATUS\"}"))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllOrders() {
        try {
            when(service.findAll()).thenReturn(testOrders);
            mockMvc.perform(get("/api/orders")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindOrderById() {
        try {
            when(service.findById(1)).thenReturn(testOrders.get(0));
            mockMvc.perform(get("/api/orders?id=1")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindInvalidId() {
        try {
            when(service.findById(0)).thenThrow(new EntityNotFoundException("Couldn't find an order with ID 0", null));
            mockMvc.perform(get("/api/orders?id=0")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindOrdersByCustomerId() {
        try {
            when(service.findAllByCustomerId(1)).thenReturn(List.of(testOrders.get(0)));
            mockMvc.perform(get("/api/orders?customer_id=1"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindInvalidCustomerId() {
        try {
            when(service.findAllByCustomerId(0))
                    .thenThrow(new EntityNotFoundException("Couldn't find a customer with ID 0", null));
            mockMvc.perform(get("/api/orders?customer_id=0")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindOrdersByStatus() {
        try {
            when(service.findAllByStatus("PAYMENT_PENDING")).thenReturn(List.of(testOrders.get(0)));
            mockMvc.perform(get("/api/orders?status=PAYMENT_PENDING")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

    }

    @Test
    public void shouldNotFindInvalidStatus() {
        try {
            when(service.findAllByStatus("STATUS_THAT_DOESNT_EXIST"))
                    .thenThrow(new EntityNotFoundException("Invalid status", null));
            mockMvc.perform(get("/api/orders?status=STATUS_THAT_DOESNT_EXIST")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
