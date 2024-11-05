package com.mattoi.frambo_study.product;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    List<Product> testProducts = List.of(new Product(null,
            "Test Cookie Original",
            "Cookie à base de manteiga com gotas de chocolate",
            null,
            120,
            14.00,
            true,
            "Test Cookie"),
            new Product(
                    null,
                    "Test Cookie pink lemonade",
                    "Cookie à base de limão siciliano com gotas de Ruby Chocolate",
                    null,
                    120,
                    12.00,
                    false,
                    "Test Cookie"));
    List<Category> testCategories = List.of(
            new Category(null, "Test Cookie"),
            new Category(null, "Test Cookie recheado"));

    @Test
    public void shouldCreateNewProduct() {
        try {
            when(repository.create(testProducts.get(0))).thenReturn(1);
            assertEquals(1, service.create(testProducts.get(0)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidProduct() {
        var invalidProduct = new Product(null, null, null, null, 0, 0d, null, null);
        assertThrows(InvalidRequestException.class, () -> {
            service.create(invalidProduct);
        });
    }

    @Test
    public void shouldUpdateProduct() {
        try {
            Product updatedFields = new Product(
                    null,
                    null,
                    "Cookie à base de manteiga com gotas de chocolate branco",
                    null,
                    null,
                    null,
                    null,
                    null);
            when(repository.update(1, updatedFields)).thenReturn(true);
            assertEquals(true, service.update(1, updatedFields));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonExistentId() {
        when(repository.update(0,testProducts.get(1))).thenThrow(new EntityNotFoundException("Couldn't find a product with ID 0", null));
        assertThrows(EntityNotFoundException.class, () -> {
            service.update(0, testProducts.get(1));
        });
    }

    @Test
    public void shouldNotUpdateInvalidProduct() {
        Product invalidProduct = new Product(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        assertThrows(InvalidRequestException.class, () -> {
            service.update(1, invalidProduct);
        });
    }

    @Test
    public void shouldFindAllProducts() {
        try {
            when(repository.findAll()).thenReturn(testProducts);
            service.findAll();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllProductsInStock() {
        try {
            when(repository.findAllInStock()).thenReturn(List.of(testProducts.get(0)));
            var productsInStock = service.findAllInStock();
            for (var product : productsInStock) {
                if (product.inStock() == false) {
                    fail("Product shouldn't be in this list: " + product.name());
                }
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindProductById() {
        try {
            when(repository.findById(1)).thenReturn(testProducts.get(0));
            assertEquals(testProducts.get(0), service.findById(1));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindNonexistentId() {
        when(repository.findById(0)).thenThrow(new EntityNotFoundException("Could not find a product with ID 0", null));
        assertThrows(EntityNotFoundException.class, () -> {
            service.findById(0);
        });
    }

    @Test
    public void shouldCreateCategory() {
        try {
            when(repository.createCategory(testCategories.get(1))).thenReturn(1);
            service.createCategory(testCategories.get(1));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidCategory() {
        assertThrows(InvalidRequestException.class, () -> {
            service.createCategory(new Category(null, null));
        });
    }

    @Test
    public void shouldUpdateCategory() {
        try {
            Category updatedCategory = new Category(null, "Cookie recheado");
            when(repository.updateCategory(1, updatedCategory)).thenReturn(true);
            assertEquals(true, service.updateCategory(1, updatedCategory));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonexistentCategory() {
        Category category = new Category(null, "Cookie recheado");
        when(repository.updateCategory(0, category))
                .thenThrow(new EntityNotFoundException("Could not find a category with ID 0", null));
        assertThrows(EntityNotFoundException.class, () -> {
            service.updateCategory(0, category);
        });
    }

    @Test
    public void shouldNotUpdateInvalidCategory() {
        assertThrows(InvalidRequestException.class, () -> {
            service.updateCategory(1, new Category(null, null));
        });
    }

    @Test
    public void shouldFindAllCategories() {
        try {
            when(repository.findAllCategories()).thenReturn(testCategories);
            service.findAllCategories();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
