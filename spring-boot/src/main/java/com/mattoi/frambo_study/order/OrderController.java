package com.mattoi.frambo_study.order;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("")
    ResponseEntity<?> create(@RequestBody Order order) {
        try {
           return new ResponseEntity<>(service.create(order), HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
           return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        } 
    }

    @PatchMapping(value = { "" }, params = { "id" })
    ResponseEntity<?> updateOrderStatus(@RequestParam(name = "id") Integer id, @RequestBody HashMap<String, String> status) {
           try{
            return new ResponseEntity<>(service.updateOrderStatus(id, status.get("status")), HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidRequestException e){
            return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("")
    ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = { "" }, params = { "id" })
    ResponseEntity<?> findById(@RequestParam(name = "id") Integer id) {
        try{
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } 
    }

    @GetMapping(value = { "" }, params = { "customer_id" })
    ResponseEntity<?> findAllByCustomerId(@RequestParam(name = "customer_id") Integer customerId) {
        try{
            return new ResponseEntity<>(service.findAllByCustomerId(customerId), HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } 
    }

    @GetMapping(value = { "" }, params = { "status" })
    ResponseEntity<?>findAllByStatus(@RequestParam(name = "status") String status) {
            return new ResponseEntity<>(service.findAllByStatus(status), HttpStatus.OK);
    }
}
