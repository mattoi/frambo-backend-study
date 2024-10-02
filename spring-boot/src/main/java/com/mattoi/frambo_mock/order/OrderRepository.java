package com.mattoi.frambo_mock.order;

@Repository
public class OrderRepository {
    private final JdbcClient jdbcClient;    
    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    //TODO post create (order fields + order items ..)
    //TODO put cancelOrder(id)
    //TODO get findAll()
    //TODO get findById(id)
    //TODO get findAllByClientId(id)
    //TODO get findAllByStatus(statusId)
}
