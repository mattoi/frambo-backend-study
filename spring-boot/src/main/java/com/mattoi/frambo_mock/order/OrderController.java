package com.mattoi.frambo_mock.order;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderRepository orderRepository;

    public OrderController(OrderRepository repository) {
        this.orderRepository = repository;
    }

    // TODO bring order api functions from notes
}
