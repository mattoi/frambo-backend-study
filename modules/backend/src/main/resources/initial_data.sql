TRUNCATE TABLE orderitems RESTART IDENTITY CASCADE;

TRUNCATE TABLE orders RESTART IDENTITY CASCADE;

TRUNCATE TABLE products RESTART IDENTITY CASCADE;

TRUNCATE TABLE categories RESTART IDENTITY CASCADE;

TRUNCATE TABLE customers RESTART IDENTITY CASCADE;

TRUNCATE TABLE orderstatus RESTART IDENTITY CASCADE;

INSERT INTO
    OrderStatus (status_name)
VALUES ('PAYMENT_PENDING'),
    ('SHIPPED'),
    ('DELIVERED'),
    ('CANCELED');

INSERT INTO
    Customers (customer_name, phone_number)
VALUES ('Matheus', '559811111111'),
    ('Cecilia', '559822222222'),
    ('Ayla', '559833333333');

INSERT INTO
    Categories (category_name)
VALUES ('Cookie'),
    ('Cookie recheado'),
    ('Recheadinho'),
    ('Brigadeiro');

INSERT INTO
    Products (
        product_name,
        product_description,
        net_weight,
        price,
        in_stock,
        category_id
    )
VALUES (
        'Cookie Original',
        'Cookie à base de manteiga com gotas de chocolate',
        120,
        14.00,
        true,
        1
    ),
    (
        'Cookie Come-Come',
        'Cookie à base de manteiga azulado com gotas de chocolate branco e biscoito Oreo',
        100,
        11.00,
        false,
        1
    ),
    (
        'Cookie Frambô Velvet',
        'Cookie de massa red velvet com gotas de chocolate branco e recheio de cream cheese',
        100,
        12.00,
        true,
        2
    ),
    (
        'Recheadinho de churros',
        'Massa açucarada com toque de canela e recheio de doce de leite',
        120,
        12.00,
        false,
        3
    );

INSERT INTO
    orders (
        customer_id,
        total_amount,
        status_name,
        date_created,
        last_updated
    )
VALUES (
        1,
        36.00,
        'PAYMENT_PENDING',
        '2024-10-03T8:00:00',
        '2024-10-03T8:00:00'
    ),
    (
        1,
        39.00,
        'SHIPPED',
        '2024-10-03T8:00:00',
        '2024-10-03T9:00:00'
    ),
    (
        2,
        47.00,
        'PAYMENT_PENDING',
        '2024-10-03T8:00:00',
        '2024-10-03T8:00:00'
    ),
    (
        3,
        37.00,
        'DELIVERED',
        '2024-10-03T8:00:00',
        '2024-10-03T9:00:00'
    );

INSERT INTO
    orderitems (
        order_id,
        product_id,
        quantity
    )
VALUES (1, 1, 1),
    (1, 2, 2),
    (2, 1, 2),
    (2, 2, 1),
    (3, 2, 1),
    (3, 3, 3),
    (4, 1, 1),
    (4, 2, 1),
    (4, 3, 1)
    -- 01x1, 02x2; 02x1, 01x2; 01x2, 03x3; 01x1, 01x2, 01x3