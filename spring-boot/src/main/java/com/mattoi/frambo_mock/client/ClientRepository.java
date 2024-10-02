package com.mattoi.frambo_mock.client;

@Repository
public class ClientRepository {

    private final JdbcClient jdbcClient;    
    private static final Logger log = LoggerFactory.getLogger(ClientRepository.class);

    public ClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
