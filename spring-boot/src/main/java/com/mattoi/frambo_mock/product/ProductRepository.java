package com.mattoi.frambo_mock.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class ProductRepository {

    private final JdbcClient jdbcClient;
    private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void create(Product product) {
        Integer categoryId = jdbcClient.sql("SELECT category_id FROM Categories WHERE category_name = :category")
                .param("category", product.category()).query(Integer.class).single();
        Assert.state(categoryId != null, "Failed to find a category ID for this name");
        var updated = jdbcClient
                .sql("INSERT INTO Products(product_name, product_description, photo_url, net_weight, price, in_stock, category_id) VALUES (?,?,?,?,?,?,?)")
                .params(List.of(product.name(), product.description(), product.photoUrl(), product.netWeight(),
                        product.price(), product.inStock(), categoryId))
                .update();
        Assert.state(updated == 1, "Failed to create product " + product.name());
    }

    public void update(Product product) {
        Integer categoryId = jdbcClient.sql("SELECT category_id FROM Categories WHERE category_name = :category")
                .param("category", product.category()).query(Integer.class).single();
        Assert.state(categoryId != null, "Failed to find a category ID for this name");
        var updated = jdbcClient
                .sql("UPDATE Products SET product_id = ?, product_name = ?, product_description = ?, photo_url = ?, net_weight = ?, price = ?, in_stock = ?, category_id = ? WHERE product_id = ?")
                .params(List.of(product.id(), product.name(), product.description(), product.photoUrl(),
                        product.netWeight(), product.price(), product.inStock(), categoryId))
                .update();
        Assert.state(updated == 1, "Failed to update product " + product.name());
    }

    public void updateProductAvailability(Integer productId, boolean inStock) {
        var updated = jdbcClient
                .sql("UPDATE Products SET in_stock = ? WHERE product_id = ?").params(List.of(inStock, productId))
                .update();
        Assert.state(updated == 1, "Failed to update product");
    }

    public List<Product> findAll() {
        return jdbcClient
                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id")
                .query((rows, rowNum) -> new Product(
                        rows.getInt("product_id"),
                        rows.getString("product_name"),
                        rows.getString("product_description"),
                        rows.getString("photo_url"),
                        rows.getInt("net_weight"),
                        rows.getFloat("price"),
                        rows.getBoolean("in_stock"),
                        rows.getString("category_name")))
                .list();
    }

    public List<Product> findAllInStock() {
        return jdbcClient
                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id WHERE Products.inStock = 1")
                .query((rows, rowNum) -> new Product(
                        rows.getInt("product_id"),
                        rows.getString("product_name"),
                        rows.getString("product_description"),
                        rows.getString("photo_url"),
                        rows.getInt("net_weight"),
                        rows.getFloat("price"),
                        rows.getBoolean("in_stock"),
                        rows.getString("category_name")))
                .list();
    }

    public List<Product> findById(int id) {
        return jdbcClient
                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id WHERE Products.product_id = ?")
                .param(id)
                .query((rows, rowNum) -> new Product(
                        rows.getInt("product_id"),
                        rows.getString("product_name"),
                        rows.getString("product_description"),
                        rows.getString("photo_url"),
                        rows.getInt("net_weight"),
                        rows.getFloat("price"),
                        rows.getBoolean("in_stock"),
                        rows.getString("category_name")))
                .list();
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("DELETE FROM Products WHERE product_id = :id")
                .param(id).update();

        Assert.state(updated == 1, "Failed to delete product");
    }

    public void createCategory(Category category) {
        var updated = jdbcClient
                .sql("INSERT INTO Categories(category_name) VALUES (?)")
                .param(category.name())
                .update();

        Assert.state(updated == 1, "Failed to create product " + category.name());
    }

    public void updateCategory(Category category) {
        var updated = jdbcClient
                .sql("UPDATE Categories SET category_name = ? WHERE category_id = :id")
                .param("id", category.id()).update();

        Assert.state(updated == 1, "Failed to update product " + category.name());
    }

    public List<Category> findAllCategories() {
        return jdbcClient
                .sql("SELECT * FROM Categories")
                .query((rows, rowNum) -> new Category(
                        rows.getInt("category_id"),
                        rows.getString("category_name")))
                .list();
    }

    public void deleteCategory(Integer id) {
        var updated = jdbcClient.sql("DELETE FROM Categories WHERE category_id = :id")
                .param(id).update();

        Assert.state(updated == 1, "Failed to delete category");
    }
}