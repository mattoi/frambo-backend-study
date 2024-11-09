
package com.mattoi.frambo_study.customer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@WebMvcTest(CustomerController.class)
public class CustomerControllerIntTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService service;

	@Autowired
	private ObjectMapper objectMapper;

	Customer testCustomer = new Customer(
			null,
			"Ayla",
			null,
			"5598333333333");
	Customer invalidCustomer = new Customer(
			null,
			null,
			null,
			null);

	@Test
	public void shouldCreateNewCustomer() throws JsonProcessingException, Exception {
		when(service.create(testCustomer)).thenReturn(1);
		mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testCustomer)))
				.andExpect(status().isCreated());
	}

	@Test
	public void shouldNotCreateInvalidCustomer() throws JsonProcessingException, Exception {
		when(service.create(invalidCustomer))
				.thenThrow(new InvalidRequestException(List.of("Name cannot be empty", "Phone number cannot be empty"), null));
		mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidCustomer)))
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void shouldFindAllCustomers() throws Exception {
		when(service.findAll()).thenReturn(List.of(testCustomer));
		mockMvc.perform(get("/api/customers")).andExpect(status().isOk());
	}

	@Test
	public void shouldFindById() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		int id = Integer.parseInt(mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testCustomer)))
				.andReturn().getResponse().getContentAsString());
		when(service.findById(id))
				.thenReturn(testCustomer);
		mockMvc.perform(get("/api/customers?id=" + id)).andExpect(status().isOk());

	}

	@Test
	public void shouldNotFindNonexistentId() throws Exception {
		var invalidId = 0;
		when(service.findById(invalidId))
				.thenThrow(new EntityNotFoundException("Invalid request fields", null));

		mockMvc.perform(get("/api/customers?id=" + invalidId)).andExpect(status().isNotFound());
	}

	@Test
	public void shouldUpdateCustomer() throws JsonProcessingException, Exception {

		var updatedFields = new Customer(null, "Ayla Soares", null, null);
		int id = 1;
		when(service.update(id, updatedFields))
				.thenReturn(true);

		mockMvc.perform(patch("/api/customers?id=" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper
						.writeValueAsString(updatedFields)))
				.andExpect(status().isNoContent());
	}

	@Test
	public void shouldNotUpdateInvalidFields() throws JsonProcessingException, Exception {
		int id = 1;
		when(service.update(id, invalidCustomer))
				.thenThrow(new InvalidRequestException(List.of("Name cannot be empty", "Phone number cannot be empty"),
						null));

		mockMvc.perform(patch("/api/customers?id=" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidCustomer)))
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void shouldNotUpdateNonexistentId() throws JsonProcessingException, Exception {
		int invalidId = 0;
		when(service.update(invalidId, testCustomer))
				.thenThrow(new EntityNotFoundException("Couldn't find a customer with ID " + invalidId, null));
		mockMvc.perform(patch("/api/customers?id=" + invalidId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testCustomer)))
				.andExpect(status().isNotFound());
	}

}