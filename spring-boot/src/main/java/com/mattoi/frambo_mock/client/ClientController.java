package com.mattoi.frambo_mock.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private ClientRepository clientRepository;

    public ClientController(ClientRepository repository) {
        this.clientRepository = repository;
    }
    // TODO post create(client fields..)
    // TODO put update(client fields..)
    // TODO get findAll()
    // TODO get findById(id)
    // TODO delete delete(id)
}
