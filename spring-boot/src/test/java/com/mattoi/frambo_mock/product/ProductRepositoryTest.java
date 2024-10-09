package com.mattoi.frambo_mock.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(ProductRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        repository.createCategory(new Category(null, "Cookie"));
        repository.create(new Product(null,
                "Cookie Original",
                "Cookie à base de manteiga com gotas de chocolate",
                null,
                120,
                (float) 14.00,
                true,
                "Cookie"));

    }

    @Test
    public void shouldCreateNewProduct() {
        var products = repository.findAll();
        assertEquals(1, products.size());

        repository.create(new Product(
                null,
                "Cookie pink lemonade",
                "Cookie à base de limão siciliano com gotas de Ruby Chocolate",
                null,
                120,
                (float) 12.00,
                true,
                "Cookie"));

        products = repository.findAll();
        assertEquals(2, products.size());
    }

    @Test
    public void shouldUpdateProduct() {
        var product = repository.findByName("Cookie Original");
        String newDescription = "Cookie à base de manteiga com gotas de chocolate branco";
        repository.update(new Product(
                product.id(),
                product.name(),
                newDescription,
                product.photoUrl(),
                product.netWeight(),
                product.price(),
                product.inStock(),
                product.category()));
        var updatedProduct = repository.findByName("Cookie Original");

        assertEquals(newDescription, updatedProduct.description());

    }

    @Test
    public void shouldUpdateProductAvailability() {
        var product = repository.findByName("Cookie Original");
        assertEquals(true, product.inStock());
        repository.updateProductAvailability(product.id(), false);
        var updatedProduct = repository.findByName("Cookie Original");
        assertEquals(false, updatedProduct.inStock());
    }

    @Test
    public void shouldFindAllProducts() {
        var products = repository.findAll();
        assertEquals(1, products.size());
    }

    @Test
    public void shouldFindAllProductsInStock() {
        repository.create(new Product(
                null,
                "Cookie pink lemonade",
                "Cookie à base de limão siciliano com gotas de Ruby Chocolate",
                null,
                120,
                (float) 12.00,
                false,
                "Cookie"));
        var productsInStock = repository.findAllInStock();
        assertEquals(1, productsInStock.size());
    }

    @Test
    public void shouldFindProductByName() {
        var product = repository.findByName("Cookie Original");

        assertEquals("Cookie Original", product.name());
    }

    @Test
    public void shouldFindProductById() {
        var product = repository.findByName("Cookie Original");
        var productById = repository.findById(product.id());
        assertEquals(product.id(), productById.id());
    }

    @Test
    public void shouldDeleteProduct() {
        var product = repository.findByName("Cookie Original");
        repository.delete(product.id());
        var products = repository.findAll();
        assertEquals(0, products.size());
    }

    @Test
    public void shouldCreateCategory() {
        repository.createCategory(new Category(null, "Crinkle"));
        var categories = repository.findAllCategories();
        assertEquals(2, categories.size());
    }

    @Test
    public void shouldUpdateCategory() {
        var categories = repository.findAllCategories();
        repository.updateCategory(new Category(categories.get(0).id(), "Cookie recheado"));
        categories = repository.findAllCategories();
        assertEquals("Cookie recheado", categories.get(0).name());
    }

    @Test
    public void shouldFindAllCategories() {
        List<Category> categories = repository.findAllCategories();
        assertEquals(1, categories.size());
        repository.createCategory(new Category(null, "Cookie recheado"));
        repository.createCategory(new Category(null, "Recheadinho"));
        repository.createCategory(new Category(null, "Brigadeiro"));
        categories = repository.findAllCategories();
        assertEquals(4, categories.size());
    }

    @Test
    public void shouldDeleteCategory() {
        var product = repository.findByName("Cookie Original");
        repository.delete(product.id());
        var categories = repository.findAllCategories();
        repository.deleteCategory(categories.get(0).id());
        categories = repository.findAllCategories();
        assertEquals(0, categories.size());
    }

}
