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

***Link to Postman documentation:*** *https://documenter.getpostman.com/view/40844842/2sAYkGLem6*

---


## Refactoring 
The booking process has been refactored to better follow the structure of a Java project. A couple of design patterns and principles has been followed as a result of that.
The goal is to make the code more maintainable and also easier to continue develop. 

### Principles used

#### Single Responsibilty:
Starting out our BookingService was responsible for a lot of operations which we would seperate to make it so it only have one resposibility.
The end goal was for it to oversee the booking-operations and only assign instructions for other classes for them to handle.

#### Open/Closed:
The newly refactored operations should be possible to extend but without altering the existing source code.

#### Dependency Invasion:
We're making classes depend on interfaces instead of eachother, making them easier to modify and maintain.

### Design patterns used

#### Strategy: 
We wanted to expand on the defferent types of pricing so that depending on season or weekend the price would alternate.
Strategy pattern let us better pick what strategy will be used in the price calculation and also make it easier for future implementation of different pricings.

#### State: 
The booking process has a bunch of different status options that we want to separate in to multiple state-classes.
These classes will have less possible operations to handle and should be easier to use. This will also make the implementation of adding future statuses simpler.

#### Factory: 
In using factory pattern we can centralize all different operations that is required for a booking to be complete.
It will make the code much simpler and improve the development and testing possibility in the future.

#### Template Method:
The template method helps us create a type of standard service operation path.
All service operations are to follow the same path with alternating details depending on the operation. 

---

## Diagrams
Diagrams created while planning can be found here.        
https://drive.google.com/file/d/1vjZqXoIeQTIFehQvcYVYt8CMpKg8pMNV/view?usp=sharing




