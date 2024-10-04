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
                        null,
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
    public void shouldFindAllClients() {
        List<Client> clients = repository.findAll();
        assertEquals(2, clients.size());
    }

    @Test
    public void shouldCreateNewClient() {
        repository.create(new Client(null, "Ayla", "ayla@email.com", "2"));
        var clients = repository.findAll();
        assertEquals(3, clients.size());
    }
}
