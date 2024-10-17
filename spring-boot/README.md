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

### Customer

Represents a store customer, with a name, contact information and an ID that's tied into any orders they create.

| Field        | Type        | Description                      |
| ------------ | ----------- | -------------------------------- |
| id*          | int         | Unique identifier.               |
| name*        | string(250) | Customer's name.                 |
| emailAddress | string(250) | Customer's email address.        |
| phoneNumber* | string(20)  | Customer's phone number. Unique. |

#### [POST] New customer
Adds a new customer to the database. The ID is generated automatically.
Request body:
```
{
    name*: String
    email: String
    phoneNumber*: String
}
```

#### [PUT] Update customer
#### [GET] All customers
#### [GET] Find customer by ID

### Product

Represents a product that can be ordered by a customer.

| Field        | Type          | Description                                        |
| ------------ | ------------- | -------------------------------------------------- |
| id*          | int           | Unique identifier.                                 |
| name*        | string(50)    | Product's name. Unique.                            |
| description* | string(250)   | Product description. Unique.                       |
| netWeight*   | int           | Product's net weight.                              |
| price*       | decimal(5, 2) | Product price. Has to be bigger than 0.            |
| inStock*     | boolean       | Whether or not it's available for ordering.        |
| categoryId*  | int           | Refers to an existing row in the categories table. |

The *category* table works as an enum, only there to ensure the category assigned to a product exists.

| Field | Type       | Description            |
| ----- | ---------- | ---------------------- |
| id*   | int        | Unique identifier.     |
| name* | string(50) | Category name. Unique. |

#### [POST] New product
#### [PUT] Update product
#### [GET] All products
#### [GET] Find product by ID
#### [GET] Find products in stock
#### [POST] New category
#### [PUT] Update category
#### [GET] All categories

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

Each order's items is stored as a row in a separate table of *order items*.

| Field      | Type | Description                           |
| ---------- | ---- | ------------------------------------- |
| orderId*   | int  | The order it's assigned to.           |
| productId* | int  | The product that was ordered.         |
| quantity*  | int  | How many of the product were ordered. |

The *status* table serves as an enum, currently with only 4 values: `PAYMENT_PENDING`, `SHIPPED`, `DELIVERED` and `CANCELED`.

 | Field | Type       | Description            |
 | ----- | ---------- | ---------------------- |
 | id*   | int        | Unique identifier.     |
 | name* | string(50) | Category name. Unique. |

#### [POST] New order
#### [PUT] Update order status
#### [GET] All orders
#### [GET] Find by ID
#### [GET] Find all by customer ID
#### [GET] Find all by status

## Project structure

### Database

### Model Layer

### Repository Layer

### Service Layer

### Controller Layer
