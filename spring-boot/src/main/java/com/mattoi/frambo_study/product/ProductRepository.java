package com.mattoi.frambo_study.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
        private final JdbcClient jdbcClient;
        private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

        public ProductRepository(JdbcClient jdbcClient) {
                this.jdbcClient = jdbcClient;
        }

        public Integer create(Product product) {
                int updated = 0;
                Integer categoryId = jdbcClient
                                .sql("SELECT category_id FROM Categories WHERE category_name = :category")
                                .param("category", product.category()).query(Integer.class).single();
                if (categoryId != null) {
                        updated = jdbcClient
                                        .sql("INSERT INTO Products(product_name, product_description, photo_url, net_weight, price, in_stock, category_id)"
                                                        + " VALUES (?,?,?,?,?,?,?) RETURNING product_id")
                                        .params(
                                                        product.name(),
                                                        product.description(),
                                                        product.photoUrl(),
                                                        product.netWeight(),
                                                        product.price(),
                                                        product.inStock(),
                                                        categoryId)
                                        .query(Integer.class).single();
                }
                return updated;
        }

        public boolean update(Product product) {
                int updated = 0;
                Integer categoryId = jdbcClient
                                .sql("SELECT category_id FROM Categories WHERE category_name = :category")
                                .param("category", product.category()).query(Integer.class).single();
                if (categoryId != null) {
                        updated = jdbcClient
                                        .sql("UPDATE Products SET product_id = ?, product_name = ?, product_description = ?, photo_url = ?, net_weight = ?, price = ?, in_stock = ?, category_id = ? WHERE product_id = ?")
                                        .params(
                                                        product.id(),
                                                        product.name(),
                                                        product.description(),
                                                        product.photoUrl(),
                                                        product.netWeight(),
                                                        product.price(),
                                                        product.inStock(),
                                                        categoryId,
                                                        product.id())
                                        .update();
                }
                return updated == 1;
        }

        public boolean updateProductAvailability(Integer productId, boolean inStock) {
                var updated = jdbcClient
                                .sql("UPDATE Products SET in_stock = ? WHERE product_id = ?")
                                .params(inStock, productId)
                                .update();
                return updated == 1;
        }

        public List<Product> findAll() {
                return jdbcClient
                                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id ORDER BY Products.product_id")
                                .query((rs, rowNum) -> new Product(
                                                rs.getInt("product_id"),
                                                rs.getString("product_name"),
                                                rs.getString("product_description"),
                                                rs.getString("photo_url"),
                                                rs.getInt("net_weight"),
                                                rs.getDouble("price"),
                                                rs.getBoolean("in_stock"),
                                                rs.getString("category_name")))
                                .list();
        }

        public List<Product> findAllInStock() {
                return jdbcClient
                                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id WHERE Products.in_stock = true ORDER BY Products.product_id")
                                .query((rs, rowNum) -> new Product(
                                                rs.getInt("product_id"),
                                                rs.getString("product_name"),
                                                rs.getString("product_description"),
                                                rs.getString("photo_url"),
                                                rs.getInt("net_weight"),
                                                rs.getDouble("price"),
                                                rs.getBoolean("in_stock"),
                                                rs.getString("category_name")))
                                .list();
        }

        public Product findByName(String name) {
                return jdbcClient
                                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id WHERE Products.product_name = ? ORDER BY Products.product_id")
                                .param(name)
                                .query((rs, rowNum) -> new Product(
                                                rs.getInt("product_id"),
                                                rs.getString("product_name"),
                                                rs.getString("product_description"),
                                                rs.getString("photo_url"),
                                                rs.getInt("net_weight"),
                                                rs.getDouble("price"),
                                                rs.getBoolean("in_stock"),
                                                rs.getString("category_name")))
                                .list().get(0);
        }

        public Product findById(int id) {
                return jdbcClient
                                .sql("SELECT * FROM Products INNER JOIN Categories ON Products.category_id = Categories.category_id WHERE Products.product_id = ?")
                                .param(id)
                                .query((rs, rowNum) -> new Product(
                                                rs.getInt("product_id"),
                                                rs.getString("product_name"),
                                                rs.getString("product_description"),
                                                rs.getString("photo_url"),
                                                rs.getInt("net_weight"),
                                                rs.getDouble("price"),
                                                rs.getBoolean("in_stock"),
                                                rs.getString("category_name")))
                                .list().get(0);
        }

        public boolean delete(Integer id) {
                var updated = jdbcClient.sql("DELETE FROM Products WHERE product_id = ?")
                                .param(id).update();
                return updated == 1;
        }

        public boolean createCategory(Category category) {
                var updated = jdbcClient
                                .sql("INSERT INTO Categories(category_name) VALUES (?)")
                                .param(category.name())
                                .update();

                return updated == 1;
        }

        public boolean updateCategory(Category category) {
                var updated = jdbcClient
                                .sql("UPDATE Categories SET category_name = ? WHERE category_id = ?")
                                .params(category.name(), category.id()).update();

                return updated == 1;
        }

        public List<Category> findAllCategories() {
                return jdbcClient
                                .sql("SELECT * FROM Categories ORDER BY category_id")
                                .query((rows, rowNum) -> new Category(
                                                rows.getInt("category_id"),
                                                rows.getString("category_name")))
                                .list();
        }

        public boolean deleteCategory(Integer id) {
                var updated = jdbcClient.sql("DELETE FROM Categories WHERE category_id = ?")
                                .param(id).update();

                return updated == 1;
        }
}