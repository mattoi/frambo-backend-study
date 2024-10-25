package com.mattoi.frambo_study.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_study.exception.EntityNotFoundException;

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
    void setup() {

    }

    @Test
    public void shouldCreateNewProduct() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var products = service.findAll();
        assertEquals(1, products.size());

    }

    @Test
    public void shouldUpdateProduct() throws EntityNotFoundException {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var product = service.findByName("Test Cookie Original");
        String newDescription = "Cookie à base de manteiga com gotas de chocolate branco";
        service.update(product.id(), new Product(
                null,
                product.name(),
                newDescription,
                product.photoUrl(),
                product.netWeight(),
                product.price(),
                product.inStock(),
                product.category()));
        var updatedProduct = service.findByName("Test Cookie Original");

        assertEquals(newDescription, updatedProduct.description());

    }

    @Test
    public void shouldUpdateProductAvailability() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var product = service.findByName("Test Cookie Original");
        assertEquals(true, product.inStock());
        service.updateProductAvailability(product.id(), false);
        var updatedProduct = service.findByName("Test Cookie Original");
        assertEquals(false, updatedProduct.inStock());
    }

    @Test
    public void shouldFindAllProducts() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var products = service.findAll();
        assertEquals(1, products.size());
    }

    @Test
    public void shouldFindAllProductsInStock() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        service.create(testProducts.get(1));
        var productsInStock = service.findAllInStock();
        assertEquals(1, productsInStock.size());
    }

    @Test
    public void shouldFindProductByName() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var product = service.findByName("Test Cookie Original");

        assertEquals("Test Cookie Original", product.name());
    }

    @Test
    public void shouldFindProductById() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var product = service.findByName("Test Cookie Original");
        var productById = service.findById(product.id());
        assertEquals(product.id(), productById.id());
    }

    @Test
    public void shouldDeleteProduct() {
        service.createCategory(testCategories.get(0));
        service.create(testProducts.get(0));
        var product = service.findByName("Test Cookie Original");
        service.delete(product.id());
        var products = service.findAll();
        assertEquals(0, products.size());
    }

    @Test
    public void shouldCreateCategory() {
        service.createCategory(testCategories.get(0));
        var categories = service.findAllCategories();
        assertEquals(1, categories.size());
    }

    @Test
    public void shouldUpdateCategory() {
        service.createCategory(testCategories.get(0));
        var categories = service.findAllCategories();
        service.updateCategory(categories.get(0).id(), new Category(null, "Test Cookie recheado"));
        categories = service.findAllCategories();
        assertEquals("Test Cookie recheado", categories.get(0).name());
    }

    @Test
    public void shouldFindAllCategories() {
        service.createCategory(testCategories.get(0));
        List<Category> categories = service.findAllCategories();
        assertEquals(1, categories.size());
    }

    @Test
    public void shouldDeleteCategory() {
        service.createCategory(testCategories.get(0));
        var categories = service.findAllCategories();
        service.deleteCategory(categories.get(0).id());
        categories = service.findAllCategories();
        assertEquals(0, categories.size());
    }

}
