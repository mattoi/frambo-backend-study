package com.mattoi.frambo_mock.client;

@Repository
public class ClientRepository {

    private final JdbcClient jdbcClient;    
    private static final Logger log = LoggerFactory.getLogger(ClientRepository.class);

    public ClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //TODO post create(client fields..)
    //TODO put update(client fields..)
    //TODO get findAll()
    //TODO get findById(id)
    //TODO delete delete(id)
}
