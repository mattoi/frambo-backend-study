package com.mattoi.frambo_mock.product;

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
        repository.createCategory(new Category(null, "Cookie recheado"));
        repository.createCategory(new Category(null, "Recheadinho"));
        repository.createCategory(new Category(null, "Brigadeiro"));
        repository.create(new Product(null,
                "Cookie Original",
                "Cookie à base de manteiga com gotas de chocolate",
                null,
                120,
                (float) 14.00,
                true,
                "Cookie"));

        repository.create(new Product(null,
                "Cookie Come-Come",
                "Cookie à base de manteiga azulado com gotas de chocolate branco e biscoito Oreo",
                null,
                100,
                (float) 11.00,
                false,
                "Cookie"));
        repository.create(new Product(null,
                "Cookie Frambô Velvet",
                "Cookie de massa red velvet com gotas de chocolate branco e recheio de cream cheese",
                null,
                100,
                (float) 12.00,
                true,
                "Cookie recheado"));
        repository.create(new Product(null,
                "Recheadinho de churros",
                "Massa açucarada com toque de canela e recheio de doce de leite",
                null,
                120,
                (float) 12.00,
                false,
                "Recheadinho"));
    }

    @Test
    public void shouldCreateNewProduct() {
        // cookie pink lemonade, cookie à base de limão siciliano com gotas de Ruby
        // Chocolate, null, 120g, R$12, true, cookie
        // TODO
    }

    @Test
    public void shouldUpdateProduct() {
        // TODO
    }

    @Test
    public void shouldUpdateProductAvailability() {
        // TODO
    }

    @Test
    public void shouldFindAllProducts() {
        // TODO
    }

    @Test
    public void shouldFindAllProductsInStock() {
        // TODO
    }

    @Test
    public void shouldFindProductById() {
        // TODO
    }

    @Test
    public void shouldDeleteProduct() {
        // TODO
    }

    @Test
    public void shouldCreateCategory() {
        // crinkle
        // TODO
    }

    @Test
    public void shouldUpdateCategory() {
        // TODO
    }

    @Test
    public void shouldFindAllCategories() {
        // TODO
    }

    @Test
    public void shouldDeleteCategory() {
        // TODO
    }

}
