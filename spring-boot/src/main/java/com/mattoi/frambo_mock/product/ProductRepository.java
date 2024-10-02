package com.mattoi.frambo_mock.product;

@Repository
public class ProductRepository{

    private final JdbcClient jdbcClient;    
    private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    //TODO post create(product fields..)
    //TODO put update(product fields..)
    //TODO put setProductAvailability(id, inStock)
    //TODO get findAll()
    //TODO get findAllInStock()
    //TODO get findById(id)
    //TODO delete delete(id)
    //TODO post createCategory(name)
    //TODO put updateCategory(id, newName)
    //TODO get findAllCategories()
    //TODO delete deleteCategory(id)

}
