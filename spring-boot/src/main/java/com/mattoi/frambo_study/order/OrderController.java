package com.mattoi.frambo_study.order;

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
        // return 201 on success
        // return 400 on missing fields
        // return 422 on invalid fields
    }

    @PatchMapping(value = { "" }, params = { "id" })
    void updateOrderStatus(@RequestParam(name = "id") Integer id, @RequestBody HashMap<String, String> status) {
        repository.updateOrderStatus(id, status.get("status"));
        // return 204 on success
        // return 404 on id not found
        // return 422 on invalid fields
    }

    @GetMapping("")
    List<Order> findAll() {
        return repository.findAll();
        // return 200 on success
    }

    @GetMapping(value = { "" }, params = { "id" })
    Order findById(@RequestParam(name = "id") Integer id) {
        return repository.findById(id);
        // return 200 on success
        // return 404 on id not found
    }

    @GetMapping(value = { "" }, params = { "customer_id" })
    List<Order> findAllByCustomerId(@RequestParam(name = "customer_id") Integer customerId) {
        return repository.findAllByCustomerId(customerId);
        // return 200 on success
        // return 404 on id not found
    }

    @GetMapping(value = { "" }, params = { "status" })
    List<Order> findAllByStatus(@RequestParam(name = "status") String status) {
        return repository.findAllByStatus(status);
        // return 200 on success
    }
}
