package com.mattoi.frambo_study.product;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.mattoi.frambo_study.exception.EntityNotFoundException;

@Repository
public class ProductRepository {
	private final JdbcClient jdbcClient;
	private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

	public ProductRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public int create(Product product) {
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

	//TODO consider creating a new exception for invalid category id
	public boolean update(int id, Product product) throws EntityNotFoundException {
		try{

			Integer categoryId = jdbcClient
			.sql("SELECT category_id FROM Categories WHERE category_name = :category")
			.param("category", product.category()).query(Integer.class).single();
			if (categoryId != null) {
				String query = "UPDATE Products SET   ";
				var updatedFields = new ArrayList<Object>();
				if (product.name() != null) {
					updatedFields.add(product.name());
					query += "product_name =?, ";
				}
				if (product.description()!= null) {
					updatedFields.add(product.description());
					query += "product_description =?, ";
				}
				if (product.photoUrl()!= null) {
					updatedFields.add(product.photoUrl());
					query += "photo_url =?, ";
				}
				if (product.netWeight()!= null) {
					updatedFields.add(product.netWeight());
					query += "net_weight =?, ";
				}
				if (product.price()!= null) {
					updatedFields.add(product.price());
					query += "price =?, ";
				}
				if (product.inStock()!= null) {
					updatedFields.add(product.inStock());
					query += "in_stock =?, ";
            }
			// Formatting query to remove last comma and add where
			query = query.substring(0, query.length() -2) +  "WHERE product_id =?";
			var updated = jdbcClient
			.sql(query)
			.params(
				product.name(),
				product.description(),
				product.photoUrl(),
				product.netWeight(),
				product.price(),
				product.inStock(),
				id)
				.update();
				return updated == 1;
			} 
			} catch (IndexOutOfBoundsException e){
            	throw new EntityNotFoundException("Couldn't find a category with ID " + id, e);
			}
		return false;
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

	public int createCategory(Category category) {
		var id = jdbcClient
				.sql("INSERT INTO Categories(category_name) VALUES (?) RETURNING category_id")
				.param(category.name())
				.query(int.class).single();

		return id;
	}

	public boolean updateCategory(Integer id, Category category) {
		var updated = jdbcClient
				.sql("UPDATE Categories SET category_name = ? WHERE category_id = ?")
				.params(category.name(), id).update();

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