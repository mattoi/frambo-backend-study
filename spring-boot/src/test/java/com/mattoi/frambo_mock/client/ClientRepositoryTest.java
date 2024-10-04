package com.mattoi.frambo_mock.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(ClientRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository repository;

    @BeforeEach
    void setup() {
        repository.create(
                new Client(
                        0,
                        "Cecilia",
                        "cecilia@email.com",
                        "559811111111"));
        repository.create(
                new Client(
                        null,
                        "Matheus",
                        "matheus@email.com",
                        "559822222222"));
    }

    @Test
    public void shouldCreateNewClient() {
        repository.create(new Client(null, "Ayla", "ayla@email.com", "2"));
        var clients = repository.findAll();
        assertEquals(3, clients.size());
    }

    @Test
    public void shouldFindAllClients() {
        List<Client> clients = repository.findAll();
        assertEquals(2, clients.size());
    }

    // It's not possible to test findById() with this framework because
    // the id of the @BeforeEach clients keeps changing, so I'm using
    // findByPhoneNumber to get the ID first
    @Test
    public void shouldFindByPhoneNumber() {
        var client = repository.findByPhoneNumber("559811111111");
        assertEquals("Cecilia", client.name());
    }

    @Test
    public void shouldFindById() {
        var clientFromPhoneNumber = repository.findByPhoneNumber("559811111111");
        var clientFromId = repository.findById(clientFromPhoneNumber.id());
        assertEquals(clientFromId, clientFromPhoneNumber);
    }

    @Test
    public void shouldUpdateClient() {
        var client = repository.findByPhoneNumber("559811111111");
        repository.update(new Client(client.id(), "Matheus Soares", client.email(), client.phoneNumber()));
        var updatedClient = repository.findByPhoneNumber("559811111111");
        assertEquals(updatedClient.name(), "Matheus Soares");
    }

    @Test
    public void shouldDeleteClient() {
        var client = repository.findByPhoneNumber("559811111111");
        repository.delete(client.id());
        var clients = repository.findAll();
        assertEquals(1, clients.size());
    }
    // TODO write tests to ensure invalid data doesn't get added
}
