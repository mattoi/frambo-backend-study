package com.mattoi.frambo_study.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mattoi.frambo_study.exception.CustomerNotFoundException;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;
import com.mattoi.frambo_study.exception.ProductsNotFoundException;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    // TODO test these
    @Transactional
    public int create(Order order) throws InvalidRequestException {
        var errors = new ArrayList<String>();
        if (order.customerId() == null) {
            errors.add("Customer ID can't be empty");
        }
        if (order.totalAmount() <= 0) {
            errors.add("Total amount must be higher than zero");
        }

        for (int i = 0; i > order.items().size() - 1; i++) {
            if (order.items().get(i).productId() == null) {
                errors.add("(Item " + i + ": Product ID can't be empty");
            }
            if (order.items().get(i).quantity() <= 0) {
                errors.add("(Item " + i + ": Quantity must be higher than zero");
            }
        }

        try {
            if (errors.size() == 0) {
                return repository.create(order);
            } else {
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
            // TODO find a way to include both customer and products errors; this is only
            // detecting one or the other
        } catch (CustomerNotFoundException e) {
            errors.add(e.getMessage());
            throw new InvalidRequestException("Invalid request fields", errors, e);
        } catch (ProductsNotFoundException e) {
            errors.addAll(e.getMessages());
            throw new InvalidRequestException("Invalid request fields", errors, e);
        }
    }

    @Transactional
    public boolean updateOrderStatus(Integer id, String newStatus)
            throws InvalidRequestException, EntityNotFoundException {
        try {
            return repository.updateOrderStatus(id, newStatus);
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find an order with ID " + id, e);
        } catch (Exception e) { // TODO find out what exception is this
            throw new InvalidRequestException("Invalid request field", List.of("Invalid status", e.getMessage()), e);
            // TODO remove e.getmessages a fter finding out what exception is this
        }
    }

    @Transactional
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Order findById(Integer id) throws EntityNotFoundException {
        try {
            return repository.findById(id);
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find an order with ID " + id, e);
        }
    }

    @Transactional
    public List<Order> findAllByCustomerId(Integer customerId) throws EntityNotFoundException {
        try {
            return repository.findAllByCustomerId(customerId);
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find a customer with ID " + customerId, e);
        }
    }

    @Transactional
    public List<Order> findAllByStatus(String status) {
        return repository.findAllByStatus(status);
    }

}
