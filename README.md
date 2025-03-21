# AirBnB Clone

**This is a REST API using Spring boot, MongoDB and Docker.
The application the backend part of a AirBnB-like living space rental
platform on which users can publish, book and leave reviews on listings**

---

## Getting started
### Prerequisites
- MongoDB
- Docker
- Postman

### Installation
1. clone the repository and open your IDE of choice
    ```
    git clone https://github.com/simonrintala/Backend-Project.git
    ```
2. re-name the application.properties file to application.yml (in /src/main/resources) and insert relevant code to connect to your mongoDB database. Below is an example to use if connecting to a MongoDB Atlas cluster (replace information in "{}" with your chosen database and jwt token information)

   #### */src/main/resources/application.yml*
    ```
   spring:
    data:
    mongodb:
    uri: {mongoDB Atlas uri}
    database: {your database name}

   jwt:
    secret: {jwt secret key}
    expirationMs: {jwt token expiration time in milliseconds}
    ```

### Usage
1. start a detached instance of docker by running the "docker-compose up -d" command in the folder of the cloned repository
2. run the AirBnBPlatform application from your IDE
3. test the application using Postman 

***Link to Postman documentation:*** *https://documenter.getpostman.com/view/40787093/2sAYX5KMiE*

---
