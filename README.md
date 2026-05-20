# hyperlink-task
Core points are covered and from the Bonus feature - Swagger UI is implemented.

A Java 21 and Spring Boot 3.4.7 used.


Must update the database details in both of these files if your local PostgreSQL setup differs:

1. auth-service/src/main/resources/application.properties
2. task-service/src/main/resources/application.properties
  (Make sure the database and both schemas already exist before you start the services.)
```example properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hyperlink?currentSchema=auth_service
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=admin
```


## Admin user creation
The admin user is created automatically from the property file when the auth service starts.
- The registration API is only for normal users.
- To create an admin locally, configure the bootstrap properties in auth-service/src/main/resources/application.properties and restart the auth service.
```example properties
auth.bootstrap.admin.enabled=true
auth.bootstrap.admin.email=admin@example.com
auth.bootstrap.admin.password=Admin123!
auth.bootstrap.admin.full-name=Admin User
```


## Build and run
From the project root, run the following commands in this order:
```
mvn clean package
mvn -pl auth-service spring-boot:run
mvn -pl task-service spring-boot:run
```


## Postman
Added Postman collection in repo itself.


## Swagger UI
- auth-service: http://localhost:8081/swagger-ui/index.html
- task-service: http://localhost:8082/swagger-ui/index.html