package com.mattoi.frambo_study.order;

import org.springframework.stereotype.Service;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@Service
public class OrderService {

    public Object create(Order order) throws InvalidRequestException{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    public Object updateOrderStatus(Integer id, String string) throws InvalidRequestException, EntityNotFoundException{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOrderStatus'");
    }

    public Object findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    public Object findById(Integer id) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    public Object findAllByCustomerId(Integer customerId) throws EntityNotFoundException{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByCustomerId'");
    }

    public Object findAllByStatus(String status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByStatus'");
    }

}
