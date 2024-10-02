package com.mattoi.frambo_mock.order;

@Repository
public class OrderRepository {
    private final JdbcClient jdbcClient;    
    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
