package com.mattoi.frambo_mock.product;

@Repository
public class ProductRepository{

    private final JdbcClient jdbcClient;    
    private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
}
