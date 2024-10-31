package com.mattoi.frambo_study.product;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@JdbcTest
@Import(ProductService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductServiceTest {
    @Autowired
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

    @BeforeEach
    void setup() throws InvalidRequestException {
        service.createCategory(testCategories.get(0));
    }

    @Test
    public void shouldCreateNewProduct() {
        try {
            service.create(testProducts.get(0));
            var products = service.findAll();
            assertEquals(1, products.size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidProduct() {
        assertThrows(InvalidRequestException.class, () -> {
            service.create(new Product(null, null, null, null, null, null, null, null));
        });
    }

    @Test
    public void shouldUpdateProduct() {
        try {
            var id = service.create(testProducts.get(0));
            String newDescription = "Cookie à base de manteiga com gotas de chocolate branco";
            service.update(id, new Product(
                    null,
                    null,
                    newDescription,
                    null,
                    null,
                    null,
                    null,
                    null));
            var updatedProduct = service.findById(id);
            assertEquals(newDescription, updatedProduct.description());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonExistentId() {
        assertThrows(EntityNotFoundException.class, () -> {
            service.update(0, testProducts.get(1));
        });
    }

    @Test
    public void shouldNotUpdateInvalidProduct() {
        assertThrows(InvalidRequestException.class, () -> {
            int id = service.create(testProducts.get(0));
            service.update(id, new Product(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null));
        });
    }
    /*
     * @Test
     * public void shouldUpdateProductAvailability() {
     * service.create(testProducts.get(0));
     * var product = service.findByName("Test Cookie Original");
     * assertEquals(true, product.inStock());
     * service.updateProductAvailability(product.id(), false);
     * var updatedProduct = service.findByName("Test Cookie Original");
     * assertEquals(false, updatedProduct.inStock());
     * }
     */

    @Test
    public void shouldFindAllProducts() {
        try {
            service.findAll();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllProductsInStock() {
        try {
            service.create(testProducts.get(0));
            service.create(testProducts.get(1));
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
    public void shouldFindProductByName() {
        try {
            service.create(testProducts.get(0));
            var product = service.findByName("Test Cookie Original");
            assertEquals("Test Cookie Original", product.name());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindNonexistentName() {
        assertThrows(InvalidRequestException.class, () -> {
            service.findByName("---");
        });
    }

    @Test
    public void shouldFindProductById() {
        try {
            service.create(testProducts.get(0));
            var product = service.findByName("Test Cookie Original");
            var productById = service.findById(product.id());
            assertEquals(product.id(), productById.id());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindNonexistentId() {
        assertThrows(InvalidRequestException.class, () -> {
            service.findById(0);
        });
    }
    /*
     * @Test
     * public void shouldDeleteProduct() {
     * service.create(testProducts.get(0));
     * var product = service.findByName("Test Cookie Original");
     * service.delete(product.id());
     * var products = service.findAll();
     * assertEquals(0, products.size());
     * }
     */

    @Test
    public void shouldCreateCategory() {
        try {
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
            var categories = service.findAllCategories();
            service.updateCategory(categories.get(0).id(), testCategories.get(1));
            categories = service.findAllCategories();
            assertEquals(testCategories.get(1).name(), categories.get(0).name());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonexistentCategory() {
        assertThrows(InvalidRequestException.class, () -> {
            service.updateCategory(0, testCategories.get(0));
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
            service.findAllCategories();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /*
     * @Test
     * public void shouldDeleteCategory() {
     * var categories = service.findAllCategories();
     * service.deleteCategory(categories.get(0).id());
     * categories = service.findAllCategories();
     * assertEquals(0, categories.size());
     * }
     */
}
