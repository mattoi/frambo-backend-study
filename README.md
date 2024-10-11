# Frambô Confeitaria Mock Backend

This is a study project where I create a database for a cookie store's products, orders etc, and an API to communicate with it. 
I'm using Spring Boot with Java for the web application and a PostgreSQL database for storage. 
The Spring project is in a subfolder because I'm planning to create a simple front-end to consume the API.
I'm not planning on hosting either app on a server, so they'll have to be run locally.

You'll be able to find the docs for the backend and frontend apps in their respective READMEs.

### TODO
- Add exception handling to repository layer
- Use the logger
- Implement controller layer
- Implement controller integration tests
- Create a service layer and perform unit tests there instead of on the repository layer
- Create more tests involving invalid inputs
- Write docs for the endpoints
- Fill the tables with some data
- Add checks to the database schema
- Maybe work on authentication?
- Maybe work on a front-end, in Vue maybe?