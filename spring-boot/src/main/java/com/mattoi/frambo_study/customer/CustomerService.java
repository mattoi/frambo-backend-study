package com.mattoi.frambo_study.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Transactional
    public int create(Customer customer) throws InvalidRequestException {
        var errors = new ArrayList<String>();
        if (customer.name() == null || customer.name().length() == 0) {
            errors.add("Name cannot be empty");
        } else if (customer.name().length() > 250) {
            errors.add("Name cannot exceed 250 characters");
        }

        if (customer.email() != null) {
            if (customer.email().length() == 0) {
                errors.add("Email address is optional, but can't be empty");
            } else if (customer.email().length() > 250) {
                errors.add("Email address can't exceed 250 characters");
            }
        }

        if (customer.phoneNumber() == null || customer.phoneNumber().length() < 7) {
            errors.add("Phone number needs to have at least 7 characters");
        } else if (customer.phoneNumber().length() > 20) {
            errors.add("Phone number cannot exceed 20 characters");
        }

        if (errors.size() == 0) {
        try {
                return repository.create(customer);
            }
        }else{
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
        catch(DuplicateKeyException e){
    
        // TODO find a way to specify the unique field
        /*
         * Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");
         * Matcher matcher = pattern.matcher(e.getMessage());
         * errors.add("The value "+ matcher.group(2) + " on field " + matcher.group(1) +
         * " is already in use.");
         */
        errors.add("One or more unique fields is already in use");
        throw new InvalidRequestException("Invalid request fields", errors, e);}
    }

    // TODO handle case of a user that wants to remove their email
    @Transactional
    boolean update(int id, Customer customer) throws InvalidRequestException, EntityNotFoundException {
        var errors = new ArrayList<String>();

        if (customer.name() == null && customer.email() == null && customer.phoneNumber() == null) {
            throw new InvalidRequestException("Invalid request fields", List.of("At least one field is required"),
                    null);
        }

        if (customer.name() != null) {
            if (customer.name().length() == 0) {
                errors.add("New name cannot be empty");
            } else if (customer.name().length() > 250) {
                errors.add("New name cannot exceed 250 characters");
            }
        }

        if (customer.email() != null) {
            if (customer.email().length() == 0) {
                errors.add("New email address can't be empty");
            } else if (customer.email().length() > 250) {
                errors.add("New email address can't exceed 250 characters");
            }
        }

        if (customer.phoneNumber() != null) {
            if (customer.phoneNumber().length() < 7) {
                errors.add("New phone number needs to have at least 7 characters");
            } else if (customer.name().length() > 20) {
                errors.add("New phone number cannot exceed 20 characters");
            }
        }

        try {
            if (errors.size() == 0) {
                return repository.update(id, customer);
            } else {
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find a customer with ID " + id, e);
        } catch (DuplicateKeyException e) {
            errors.add(e.getMessage());
            throw new InvalidRequestException("Invalid request fields", errors, e);
        }
    }

    @Transactional
    List<Customer> findAll() {
        return repository.findAll();
    }

    @Transactional
    Customer findById(int id) throws EntityNotFoundException {
        try {
            return repository.findById(id);
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find a customer with ID " + id, e);
        }
    }

}
