package com.mattoi.frambo_mock.order;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    void create(@RequestBody Order order) {
        repository.create(order);
    }

    @PatchMapping(value = { "" }, params = { "id" })
    void updateOrderStatus(@RequestParam(name = "id") Integer id, @RequestBody HashMap<String, String> status) {
        repository.updateOrderStatus(id, status.get("status"));
    }

    @GetMapping("")
    List<Order> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = { "" }, params = { "id" })
    Order findById(@RequestParam(name = "id") Integer id) {
        return repository.findById(id);
    }

    @GetMapping(value = { "" }, params = { "customer_id" })
    List<Order> findAllByCustomerId(@RequestParam(name = "customer_id") Integer customerId) {
        return repository.findAllByCustomerId(customerId);
    }

    @GetMapping(value = { "" }, params = { "status" })
    List<Order> findAllByStatus(@RequestParam(name = "status") String status) {
        return repository.findAllByStatus(status);
    }
}
