package com.mattoi.frambo_mock.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mattoi.frambo_mock.exception.EntityNotFoundException;
import com.mattoi.frambo_mock.exception.InvalidRequestException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("")
    ResponseEntity<?> create(@ModelAttribute Customer newCustomer) {
        try {
           return new ResponseEntity<>(service.create(newCustomer), HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
           return new ResponseEntity<>(e.getMessages(), HttpStatus.UNPROCESSABLE_ENTITY);
        } 
  }

    @PatchMapping(value = { "" }, params = { "id" })
    ResponseEntity<?> update(@RequestParam(name = "id") Integer id, @ModelAttribute Customer updatedCustomer) {
        try{
            return new ResponseEntity<>(service.update(id, updatedCustomer), HttpStatus.NO_CONTENT);
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

    // consider avoid deleting customers
    /*
     * @DeleteMapping(value = { "" }, params = { "id" })
     * void delete(@RequestParam(name = "id") Integer id) {
     * repository.delete(id);
     * }
     */

}
