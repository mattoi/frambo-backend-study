# Frambô Confeitaria Mock Backend

This is the main application for this study project: a Spring Boot web app for a cookie store e-commerce system. It's the follow-up to [this project](https://github.com/mattoi/fcc-spring-boot-tutorial) where I coded along a FreeCodeCamp tutorial. This app provides a database integration and endpoints to access it. The database is set to be containerized for easy setup. You can perform various CRUD operations to manipulate customer, product and order data. I implemented the operations I thought would be useful in a real-life scenario.

Right now the application works roughly as intended, accepting valid request bodies and rejecting invalid entries (thanks to the database schema), but it's not doing a good job on communicating what's wrong with each invalid request. The response codes are all over the place and there's no exception handling to communicate that throughout the app, either. My next steps after writing the initial documentation are implementing exceptions and response entities.

## How To Run This Application

Requirements:
- Windows, Linux or Mac OS
- Docker Desktop (for the PostgreSQL container)
- Java 17 or later

Setup:
1. Clone the project repository
2. Make sure Docker Desktop is running
3. Go to the `./spring-boot/` folder
4. (Optional) To start the database container independently from the application, run `docker compose up`. Otherwise it will be started automatically with the Spring application, and stopped when it closes.
5. Run `./gradlew bootRun` to start the application. If you want to use the provided example database rows (recommended for first launch), run `./gradlew bootRun --args='--initializeDefaultValues=true'` instead.

The database schema is set to be executed on startup. After launching the app, you can try out the endpoints with a HTTP client like Postman. The provided [request collection](src\main\resources\Frambo.postman_collection.json) can be imported to Postman for an easy setup.

## API Documentation

Here you can find descriptions for each type of JSON object representing an entity in the application and the endpoints for their manipulation. Fields with a * can't be null, but not all of them are required in a request body. I did implement some Delete methods, but realized that the relational structure will rarely allow any deletions, so I omitted them.

##### TODO
- Provide the appropriate response codes and bodies (when necessary) for better clarity. Right now, pretty much every successful call returns a 200 and every unsuccessful call returns a 500.
- Update response bodies in this section after the implementation.
- Consider changing Put requests to Patch, as they're not supposed to replace *all* of an object's fields (i.e the ID should always stay the same).
- Consider using id as a path parameter for update methods instead of being included in the request body.

### Customer

Represents a store customer, with a name, contact information and an ID that's tied into any orders they create.

| Field        | Type        | Description                                    |
| ------------ | ----------- | ---------------------------------------------- |
| id*          | int         | Unique identifier.                             |
| name*        | string(250) | Customer's name.                               |
| emailAddress | string(250) | Customer's email address. Unique.              |
| phoneNumber* | string(20)  | Customer's phone number. Only numbers. Unique. |

#### [POST] New customer
`/api/customers`

Adds a new customer to the database. The ID is generated automatically.

##### Request body format
```
{
    name* (String(250))
    email (String(250))
    phoneNumber* (String(20))
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```

#### [PATCH] Update customer
`/api/customers`

Updates a costumer's name or contact fields. The provided `id` specifies the customer that will be modified. All the fields need to be present in the request body at the same time, even if some of them are unchanged. 

##### Request body format
```
{
    id* (int)
    name (String(250))
    email* (String(250))
    phoneNumber* (String(20))
}
```
##### Responses
```
200 OK
```

##### TODO 
- Modify repository method so not every single field is required in the response body
- Entering a non-existing id will result in a 200 but nothing will happen

#### [GET] All customers
`/api/customers`

Returns a list with all registered customers.

##### Responses
```
200 OK

[
    {
        id (int)
        name (String(250))
        email (String(250))
        phoneNumber (String(20))
    }, ...
]
```

#### [GET] Find customer by ID
`/api/customers?id={id}`

Returns the customer associated with the `id` parameter.

##### Responses
```
200 OK

{
    id (int)
    name (String(250))
    email (String(250))
    phoneNumber (String(20))
}
```

```
500 Internal Server Error
```

##### TODO
- Return a 404 instead of 500

### Product

Represents a product that can be ordered by a customer.

| Field        | Type          | Description                                        |
| ------------ | ------------- | -------------------------------------------------- |
| id*          | int           | Unique identifier.                                 |
| name*        | string(50)    | Product name. Unique.                              |
| description* | string(250)   | Product description. Unique.                       |
| netWeight*   | int           | Product net weight.                                |
| price*       | decimal(5, 2) | Product price. Has to be bigger than 0.            |
| inStock*     | boolean       | Whether or not it's available for ordering.        |
| categoryId*  | int           | Refers to an existing row in the categories table. |

The **category** table works as an enum, only there to ensure the product belongs to an existing category.

| Field | Type       | Description            |
| ----- | ---------- | ---------------------- |
| id*   | int        | Unique identifier.     |
| name* | string(50) | Category name. Unique. |

#### [POST] New product
`/api/products`

Adds a new product to the database. The ID is generated automatically. The `category` needs to refer to the name of an existing category.

##### Request body format
```
{
    name* (String(50))
    description* (String(250))
    photoUrl (String(250))
    netWeight* (int)
    price* (double)
    inStock* (boolean)
    category* (String(50))
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```
#### [PATCH] Update product
`/api/products`

Updates a product's fields. The provided `id` specifies the poduct that will be modified. All the fields need to be present in the request body at the same time, even if some of them are unchanged. 

##### Request body format
```
{
    name* (String(50))
    description* (String(250))
    photoUrl (String(250))
    netWeight* (int)
    price* (double)
    inStock* (boolean)
    category* (String(50))
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```

##### TODO
- Modify repository method so not every single field is required in the response body
- Entering a non-existing id will result in a 200 but nothing will happen
  
#### [GET] All products
`/api/products`

Returns a list with all registered products.

##### Responses
```
200 OK

[
    {
        id (int)
        name (String(50))
        description (String(250))
        photoUrl (String(250))
        netWeight (int)
        price (double)
        inStock (bool)
        category (String(50))
    }, ...
]
```

#### [GET] Find product by ID
`/api/products?id={id}`

Returns the product with the specified ID.

##### Responses
```
200 OK

{
    id (int)
    name (String(50))
    description (String(250))
    photoUrl (String(250))
    netWeight (int)
    price (double)
    inStock (bool)
    category (String(50))
}
```

```
500 Internal Server Error
```

##### TODO
- Return a 404 instead of 500

#### [GET] Find products in stock
`/api/products/in_stock`

Returns a list of all products where `inStock` equals `true`.

##### Responses
```
200 OK

[
    {
        id (int)
        name (String(50))
        description (String(250))
        photoUrl (String(250))
        netWeight (int)
        price (double)
        inStock (bool)
        category (String(50))
    }, ...
]
```

#### [POST] New category
`/api/products/categories`
Adds a new category to the database. The ID is generated automatically. 

##### Request body format
```
{
    name* (String(50))
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```

#### [PATCH] Update category
`/api/products/categories`

Updates a category's name. The provided `id` specifies the category that will be modified.

##### Request body format
```
{
    name* (String(50))
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```

##### TODO
- Entering a non-existing id will result in a 200 but nothing will happen

#### [GET] All categories
`/api/products/categories`

Returns a list of all categories.

##### Responses
```
200 OK
```

### Order

Represents an order, always assigned to a customer and containing one or more items.

| Field        | Type          | Description                                    |
| ------------ | ------------- | ---------------------------------------------- |
| id*          | int           | Unique identifier.                             |
| customerId*  | int           | Refers to the customer that made the order.    |
| totalAmount* | double        | The order's final price.                       |
| statusName*  | string(50)    | Refers to an existing row in the status table. |
| dateCreated* | LocalDateTime | Moment when the order was created.             |
| lastUpdated* | LocalDateTime | Moment when the status was last updated.       |

Each order's items is stored as a row in a separate table of **order items**.

| Field      | Type | Description                           |
| ---------- | ---- | ------------------------------------- |
| orderId*   | int  | The order it's assigned to.           |
| productId* | int  | The product that was ordered.         |
| quantity*  | int  | How many of the product were ordered. |

The **status** table serves as an enum, currently with only 4 values: `PAYMENT_PENDING`, `SHIPPED`, `DELIVERED` and `CANCELED`.

 | Field | Type       | Description            |
 | ----- | ---------- | ---------------------- |
 | id*   | int        | Unique identifier.     |
 | name* | string(50) | Category name. Unique. |

#### [POST] New order
`/api/orders`

Adds a new order to the database. The ID is generated automatically. The `customerId` must match an existing customer ID, and each item's `productId` must match an existing product ID.

##### Request body format
```
{
    customerId (int)
    totalAmount (double)
    items (array)[
        {
            productId (int)
            productName (String(50))
            productPrice (double)
            quantity (int)
        }
    ], ...
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```

#### [PATCH] Update order status
`/api/orders?id={id}`

Updates an order's status. 

##### Request body format
```
{
    status* (String(50))
}
```
##### Responses
```
200 OK
```

```
500 Internal Server Error
```

##### TODO
- Entering a non-existing id will result in a 200 but nothing will happen

#### [GET] All orders
`/api/orders`

Returns a list with all registered orders.

##### Responses
```
200 OK

[
    {
        id (int)
        customerId (int)
        items (array)[
            {
                productId (int)
                productName (String(50))
                productPrice (double)
                quantity (int)
            }, ...
        ]
        status (String(50))
        dateCreated (LocalDateTime)
        lastUpdated (LocalDateTime)
    }, ...
]
```

#### [GET] Find by ID
`/api/orders?id={id}`

Returns the order associated with the provided ID.

##### Responses
```
200 OK

{
    id (int)
    customerId (int)
    items (array)[
        {
            productId (int)
            productName (String(50))
            productPrice (double)
            quantity (int)
        }, ...
    ]
    status (String(50))
    dateCreated (LocalDateTime)
    lastUpdated (LocalDateTime)
}
```

#### [GET] Find all by customer ID
`/api/orders?customer_id={id}`

Returns a list of all orders associated with the provided customer ID.

##### Responses
```
200 OK

{
    id (int)
    customerId (int)
    items (array)[
        {
            productId (int)
            productName (String(50))
            productPrice (double)
            quantity (int)
        }, ...
    ]
    status (String(50))
    dateCreated (LocalDateTime)
    lastUpdated (LocalDateTime)
}
```

#### [GET] Find all by status
`/api/orders?status={status}`

Returns a list of all orders marked with the provided status name.

##### Responses
```
200 OK

{
    id (int)
    customerId (int)
    items (array)[
        {
            productId (int)
            productName (String(50))
            productPrice (double)
            quantity (int)
        }, ...
    ]
    status (String(50))
    dateCreated (LocalDateTime)
    lastUpdated (LocalDateTime)
}
```

## Project structure

### Database

### Model Layer

### Repository Layer

### Service Layer

### Controller Layer
