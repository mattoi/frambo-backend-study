
package com.mattoi.frambo_study.customer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerIntTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void shouldCreateNewCustomer() throws JsonProcessingException, Exception {
                mockMvc.perform(post("/api/customers")
                                .content(objectMapper.writeValueAsString(new Customer(
                                                null,
                                                "Ayla",
                                                null,
                                                "5598333333333"))))
                                .andExpect(status().isCreated());

        }

        @Test
        public void shouldNotCreateInvalidCustomer() throws JsonProcessingException, Exception {
                mockMvc.perform(post("/api/customers")
                                .content(objectMapper.writeValueAsString(new Customer(
                                                null,
                                                null,
                                                null,
                                                null))))
                                .andExpect(status().isUnprocessableEntity());

        }

        @Test
        public void shouldFindAllCustomers() throws Exception {
                mockMvc.perform(get("/api/customers")).andExpect(status().isOk());

        }

        @Test
        public void shouldNotFindNonexistentNumber() throws Exception {
                mockMvc.perform(get("/api/customers?id=0")).andExpect(status().isNotFound());
        }

        @Test
        public void shouldFindById() throws JsonProcessingException, UnsupportedEncodingException, Exception {
                var id = mockMvc.perform(get("/api/customers")
                                .content(objectMapper.writeValueAsString(new Customer(
                                                null,
                                                "Ayla",
                                                null,
                                                "5598333333333"))))
                                .andReturn().getResponse().getContentAsString();
                mockMvc.perform(get("/api/customers?id=" + id)).andExpect(status().isOk());

        }

        public void shouldNotFindNonexistentId() throws Exception {
                mockMvc.perform(get("/api/customers?id=0")).andExpect(status().isNotFound());

        }

        @Test
        public void shouldUpdateCustomer() throws JsonProcessingException, Exception {
                var id = mockMvc.perform(get("/api/customers")
                                .content(objectMapper.writeValueAsString(new Customer(
                                                null,
                                                "Ayla",
                                                null,
                                                "5598333333333"))))
                                .andReturn().getResponse().getContentAsString();
                mockMvc.perform(patch("/api/customers?id=" + id)
                                .content(objectMapper
                                                .writeValueAsString(new Customer(null, "Matheus Soares", null, null))))
                                .andExpect(status().isNoContent());
        }

        public void shouldNotUpdateInvalidFields() throws JsonProcessingException, Exception {
                var id = mockMvc.perform(get("/api/customers")
                                .content(objectMapper.writeValueAsString(new Customer(
                                                null,
                                                "Ayla",
                                                null,
                                                "5598333333333"))))
                                .andReturn().getResponse().getContentAsString();
                mockMvc.perform(patch("/api/customers?id=" + id)
                                .content(objectMapper.writeValueAsString(new Customer(null, "", null, null))))
                                .andExpect(status().isUnprocessableEntity());
        }

        public void shouldNotUpdateNonexistentId() throws JsonProcessingException, Exception {
                mockMvc.perform(patch("/api/customers?id=0")
                                .content(objectMapper
                                                .writeValueAsString(new Customer(null, "Matheus Soares", null, null))))
                                .andExpect(status().isNotFound());
        }
}