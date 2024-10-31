package com.mattoi.frambo_study.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.fail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void createProduct_ShouldReturn201_WhenProductIsCreated() throws Exception {
        /*
         * Product newProduct = new Product("New Product",
         * "A description of the new product", 19.99, 50);
         * 
         * mockMvc.perform(post("/api/products")
         * .contentType(MediaType.APPLICATION_JSON)
         * .content(objectMapper.writeValueAsString(newProduct))) // Convert object to
         * JSON
         * .andExpect(status().isCreated())
         * .andExpect(header().exists("Location"));
         */
    }

    @Test
    public void shouldCreateNewProduct() {
        try {
            mockMvc.perform(post("/api/products")
                    .content(objectMapper.writeValueAsString(testProducts.get(0))))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidProduct() {
        try {
            mockMvc.perform(post("/api/products")
                    .content(objectMapper
                            .writeValueAsString(new Product(null, null, null, null, null, null, null, null))))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldUpdateProduct() {
        try {
            var id = mockMvc.perform(post("/api/products")
                    .content(objectMapper.writeValueAsString(testProducts.get(0))));
            String newDescription = "Cookie à base de manteiga com gotas de chocolate branco";
            mockMvc.perform(patch("/api/products?id=" + id).content(objectMapper.writeValueAsString(new Product(
                    null,
                    null,
                    newDescription,
                    null,
                    null,
                    null,
                    null,
                    null)))).andExpect(status().isNoContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonExistentId() {
        try {
            mockMvc.perform(patch("/api/products?id=0").content(objectMapper.writeValueAsString(new Product(
                    null,
                    null,
                    "a",
                    null,
                    null,
                    null,
                    null,
                    null)))).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateInvalidProduct() {
        try {
            var id = mockMvc.perform(post("/api/products")
                    .content(objectMapper.writeValueAsString(testProducts.get(0))));
            mockMvc.perform(patch("/api/products?id=" + id).content(objectMapper.writeValueAsString(new Product(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null)))).andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllProducts() {
        try {
            mockMvc.perform(get("/api/products")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllProductsInStock() {
        try {
            mockMvc.perform(post("/api/products")
                    .content(objectMapper.writeValueAsString(testProducts.get(0))));
            mockMvc.perform(post("/api/products")
                    .content(objectMapper.writeValueAsString(testProducts.get(1))));
            var jsonResponse = mockMvc.perform(get("/api/products")).andReturn().getResponse().getContentAsString();

            var productsInStock = objectMapper.readValue(jsonResponse, new TypeReference<List<Product>>() {
            });
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
            var id = mockMvc.perform(post("/api/products")
                    .content(objectMapper.writeValueAsString(testProducts.get(0))));
            mockMvc.perform(get("/api/products?id=" + id)).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindNonexistentId() {
        try {

            mockMvc.perform(get("/api/products?id=0")).andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldCreateCategory() {
        try {
            mockMvc.perform(
                    post("/api/products/categories")
                            .content(objectMapper.writeValueAsString(testCategories.get(1))))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidCategory() {
        try {
            mockMvc.perform(
                    post("/api/products/categories")
                            .content(objectMapper.writeValueAsString(new Category(null, null))))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldUpdateCategory() {
        try {
            var id = mockMvc.perform(post("/api/products/categories")
                    .content(objectMapper.writeValueAsString(testCategories.get(0))));
            mockMvc.perform(patch("/api/products/categories?id=" + id)
                    .content(objectMapper.writeValueAsString(testCategories.get(1))))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonexistentCategory() {
        try {
            var id = mockMvc.perform(post("/api/products/categories")
                    .content(objectMapper.writeValueAsString(testCategories.get(0))));
            mockMvc.perform(patch("/api/products/categories?id=" + id)
                    .content(objectMapper.writeValueAsString(testCategories.get(1))))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateInvalidCategory() {
        try {
            var id = mockMvc.perform(post("/api/products/categories")
                    .content(objectMapper.writeValueAsString(testCategories.get(0))));
            mockMvc.perform(patch("/api/products/categories?id=" + id)
                    .content(objectMapper.writeValueAsString(new Category(null, null))))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldFindAllCategories() {
        try {
            mockMvc.perform(get("/api/products/categories")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
