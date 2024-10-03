package com.mattoi.frambo_mock.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final JdbcClient jdbcClient;
    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // TODO post create (order fields + order items ..)
    // TODO put cancelOrder(id)
    // TODO get findAll()
    // TODO get findById(id)
    // TODO get findAllByClientId(id)
    // TODO get findAllByStatus(statusId)
}
