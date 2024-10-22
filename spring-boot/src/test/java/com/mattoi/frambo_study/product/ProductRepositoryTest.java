package com.mattoi.frambo_study.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_study.product.Category;
import com.mattoi.frambo_study.product.Product;
import com.mattoi.frambo_study.product.ProductRepository;

@JdbcTest
@Import(ProductRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

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
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var products = repository.findAll();
        assertEquals(1, products.size());

    }

    @Test
    public void shouldUpdateProduct() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var product = repository.findByName("Test Cookie Original");
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
        var updatedProduct = repository.findByName("Test Cookie Original");

        assertEquals(newDescription, updatedProduct.description());

    }

    @Test
    public void shouldUpdateProductAvailability() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var product = repository.findByName("Test Cookie Original");
        assertEquals(true, product.inStock());
        repository.updateProductAvailability(product.id(), false);
        var updatedProduct = repository.findByName("Test Cookie Original");
        assertEquals(false, updatedProduct.inStock());
    }

    @Test
    public void shouldFindAllProducts() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var products = repository.findAll();
        assertEquals(1, products.size());
    }

    @Test
    public void shouldFindAllProductsInStock() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        repository.create(testProducts.get(1));
        var productsInStock = repository.findAllInStock();
        assertEquals(1, productsInStock.size());
    }

    @Test
    public void shouldFindProductByName() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var product = repository.findByName("Test Cookie Original");

        assertEquals("Test Cookie Original", product.name());
    }

    @Test
    public void shouldFindProductById() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var product = repository.findByName("Test Cookie Original");
        var productById = repository.findById(product.id());
        assertEquals(product.id(), productById.id());
    }

    @Test
    public void shouldDeleteProduct() {
        repository.createCategory(testCategories.get(0));
        repository.create(testProducts.get(0));
        var product = repository.findByName("Test Cookie Original");
        repository.delete(product.id());
        var products = repository.findAll();
        assertEquals(0, products.size());
    }

    @Test
    public void shouldCreateCategory() {
        repository.createCategory(testCategories.get(0));
        var categories = repository.findAllCategories();
        assertEquals(1, categories.size());
    }

    @Test
    public void shouldUpdateCategory() {
        repository.createCategory(testCategories.get(0));
        var categories = repository.findAllCategories();
        repository.updateCategory(new Category(categories.get(0).id(), "Test Cookie recheado"));
        categories = repository.findAllCategories();
        assertEquals("Test Cookie recheado", categories.get(0).name());
    }

    @Test
    public void shouldFindAllCategories() {
        repository.createCategory(testCategories.get(0));
        List<Category> categories = repository.findAllCategories();
        assertEquals(1, categories.size());
    }

    @Test
    public void shouldDeleteCategory() {
        repository.createCategory(testCategories.get(0));
        var categories = repository.findAllCategories();
        repository.deleteCategory(categories.get(0).id());
        categories = repository.findAllCategories();
        assertEquals(0, categories.size());
    }

}
