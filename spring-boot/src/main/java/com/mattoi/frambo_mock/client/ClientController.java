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
    // TODO bring client api functions from notes
}
