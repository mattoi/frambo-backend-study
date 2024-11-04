package com.mattoi.frambo_study.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@WebMvcTest(ProductController.class)
public class ProductControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

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
    public void shouldCreateNewProduct() {
        try {
            mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testProducts.get(0))))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidProduct() {
        try {
            Product invalidProduct = new Product(null, null, null, null, null, null, null, null);
            when(service.create(invalidProduct)).thenThrow(
                    new InvalidRequestException(List.of("Fields are empty"), null));
            mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper
                            .writeValueAsString(invalidProduct)))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
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
            when(service.update(1, updatedFields)).thenReturn(true);
            mockMvc.perform(patch("/api/products?id=1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedFields)))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonExistentId() {
        try {
            Product updatedFields = new Product(
                    null,
                    null,
                    "a",
                    null,
                    null,
                    null,
                    null,
                    null);
            when(service.update(0, updatedFields))
                    .thenThrow(new EntityNotFoundException("Couldn't find a product with ID 0", null));
            mockMvc.perform(patch("/api/products?id=0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedFields)))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateInvalidProduct() {
        try {
            Product invalidFields = new Product(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            when(service.update(1, invalidFields)).thenThrow(
                    new InvalidRequestException(List.of("There are no fields"), null));
            mockMvc.perform(patch("/api/products?id=1").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidFields)))
                    .andExpect(status().isUnprocessableEntity());
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
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testProducts.get(0))));
            mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
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
            when(service.findById(1)).thenReturn(testProducts.get(0));
            mockMvc.perform(get("/api/products?id=1")).andExpect(status().isOk());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotFindNonexistentId() {
        try {
            when(service.findById(0)).thenThrow(new EntityNotFoundException("Couldn't find a product with ID 0", null));
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
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testCategories.get(1))))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateInvalidCategory() {
        try {
            Category invalidCategory = new Category(null, null);
            when(service.createCategory(invalidCategory)).thenThrow(
                    new InvalidRequestException(List.of("Category cannot be empty"), null));
            mockMvc.perform(
                    post("/api/products/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidCategory)))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldUpdateCategory() {
        try {
            when(service.updateCategory(1, testCategories.get(1))).thenReturn(true);
            mockMvc.perform(patch("/api/products/categories?id=1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testCategories.get(1))))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateOnNonexistentCategory() {
        try {
            when(service.updateCategory(0, testCategories.get(0)))
                    .thenThrow(new EntityNotFoundException("Couldn't find a category with ID 0", null));
            mockMvc.perform(patch("/api/products/categories?id=0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testCategories.get(0))))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateInvalidCategory() {
        try {
            Category invalidCategory = new Category(null, null);
            when(service.updateCategory(1, invalidCategory))
                    .thenThrow(new InvalidRequestException(List.of("Category name cannot be null"), null));
            mockMvc.perform(patch("/api/products/categories?id=1")
                    .contentType(MediaType.APPLICATION_JSON)
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
