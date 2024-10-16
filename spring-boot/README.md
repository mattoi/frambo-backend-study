# Frambô Confeitaria Mock Backend

This is the main application for this study project: a Spring Boot web app for a cookie store e-commerce system.
This app provides a database integration and endpoints to access it. The database is set to be containerized for easy setup.
You can perform various CRUD operations to manipulate customer, product and order data. I implemented the operations I thought would be useful in a real-life scenario.

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

## Project structure

### Database

### Model Layer

### Repository Layer

### Service Layer

### Controller Layer