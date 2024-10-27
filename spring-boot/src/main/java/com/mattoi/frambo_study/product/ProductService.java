package com.mattoi.frambo_study.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional
    public int create(Product product) throws InvalidRequestException {
        var errors = new ArrayList<String>();
        if (product.name() == null || product.name().isEmpty()) {
            errors.add("Product name cannot be empty");
        } else if (product.name().length() > 250) {
            errors.add("Product name cannot exceed 250 characters");
        }
        if (product.description() == null || product.description().isEmpty()) {
            errors.add("Product description cannot be empty");
        } else if (product.description().length() > 250) {
            errors.add("Product description cannot exceed 250 characters");
        }
        if (product.photoUrl() != null) {
            if (product.photoUrl().isEmpty()) {
                errors.add("Product photo URL can be null, but not empty");
            } else if (product.photoUrl().length() > 250) {
                errors.add("Product photo URL cannot exceed 250 characters");
            }
        }
        if (product.netWeight() <= 0) {
            errors.add("Product net weight must be greater than zero");
        }
        if (product.price() <= 0) {
            errors.add("Product price must be greater than zero");
        }
        if (product.inStock() == null) {
            errors.add("Product in stock status cannot be null");
        }
        if (product.category() == null) {
            errors.add("Product category cannot be null");
        }

        try {
            if (errors.isEmpty()) {
                return repository.create(product);
            } else {
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
        } catch (DuplicateKeyException e) {
            errors.add("One of the fields is already in use");
            throw new InvalidRequestException("Invalid request fields", errors, e);
        }
    }

    @Transactional
    public boolean update(Integer id, Product product) throws InvalidRequestException, EntityNotFoundException {
        var errors = new ArrayList<String>();

        if (product.name() == null && product.description() == null && product.photoUrl() == null
                && product.netWeight() == null && product.price() == null && product.inStock() == null
                && product.category() == null) {
            throw new InvalidRequestException("Invalid request fields", List.of("At least one field is required"),
                    null);
        }

        if (product.name() != null) {
            if (product.name().isEmpty()) {
                errors.add("Product name cannot be empty");
            } else if (product.name().length() > 250) {
                errors.add("Product name cannot exceed 250 characters");
            }
        }
        if (product.description() != null) {
            if (product.description().isEmpty()) {
                errors.add("Product description cannot be empty");
            } else if (product.description().length() > 250) {
                errors.add("Product description cannot exceed 250 characters");
            }
        }
        if (product.photoUrl() != null) {
            if (product.photoUrl().isEmpty()) {
                errors.add("Product photo URL can be null, but not empty");
            } else if (product.photoUrl().length() > 250) {
                errors.add("Product photo URL cannot exceed 250 characters");
            }
        }
        if (product.netWeight() != null) {
            if (product.netWeight() <= 0) {
                errors.add("Product net weight must be greater than zero");
            }
        }
        if (product.price() != null) {
            if (product.price() <= 0) {
                errors.add("Product price must be greater than zero");
            }
        }

        /*
         * if (product.inStock() == null) {
         * errors.add("Product in stock status cannot be null");
         * }
         */

        if (product.category() != null) {
            if (product.category().length() == 0) {
                errors.add("Product category name cannot be empty");
            }
        }

        try {
            if (errors.size() == 0) {
                var result = repository.update(id, product);
                if (result) {
                    return result;
                } else {
                    throw new EntityNotFoundException("Couldn't find a product with ID " + id, null);
                }
            } else {
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Couldn't find a product with ID " + id, e);
        } catch (DuplicateKeyException e) {
            errors.add(e.getMessage());
            throw new InvalidRequestException("Invalid request fields", errors, e);
        }
    }

    @Transactional
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional
    public List<Product> findAllInStock() {
        return repository.findAllInStock();
    }

    @Transactional
    public Product findById(Integer id) throws EntityNotFoundException {
        try {
            return repository.findById(id);
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find a product with ID " + id, e);
        }
    }

    public Product findByName(String name) throws EntityNotFoundException {
        try {
            return repository.findByName(name);
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find a product named " + name, e);
        }
    }

    @Transactional
    public int createCategory(Category category) throws InvalidRequestException {
        var errors = new ArrayList<String>();
        if (category.name() == null || category.name().isEmpty()) {
            errors.add("Category name cannot be empty");
        } else if (category.name().length() > 250) {
            errors.add("Category name cannot exceed 250 characters");
        }

        try {
            if (errors.size() == 0) {
                return repository.createCategory(category);
            } else {
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
        } catch (DuplicateKeyException e) {
            errors.add("A category with the same name already exists");
            throw new InvalidRequestException("Invalid request fields", errors, e);
        }
    }

    @Transactional
    public boolean updateCategory(Integer id, Category category)
            throws InvalidRequestException, EntityNotFoundException {
        var errors = new ArrayList<String>();
        if (category.name() == null || category.name().isEmpty()) {
            errors.add("New category name cannot be empty");
        } else if (category.name().length() > 250) {
            errors.add("New category name cannot exceed 250 characters");
        }

        try {
            if (errors.size() == 0) {
                return repository.updateCategory(id, category);
            } else {
                throw new InvalidRequestException("Invalid request fields", errors, null);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Couldn't find a product with ID " + id, e);
        } catch (DuplicateKeyException e) {
            errors.add("A category with the same name already exists");
            throw new InvalidRequestException("Invalid request fields", errors, e);
        }
    }

    @Transactional
    public List<Category> findAllCategories() {
        return repository.findAllCategories();
    }
}
