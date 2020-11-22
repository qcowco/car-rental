# car-rental
### Microservice-based car rental application
This is an application that simulates car rental logic. It's designed using the microservice architure, meaning it's split into multiple services that communicate with each other. The microservice logic is accomplished using the Spring Cloud Netflix stack as follows:

| Functionality     | Implementation |
| ----------------- | -------------- |
| Service Discovery | Netflix Eureka |
| Gateway           | Netflix Zuul   |
| HTTP Client       | Netflix Feign  |

## Table of contents
* [Technologies](#technologies)
* [Services](#services)
* [Security](#security)
* [Database](#database)
* [Configuration](#configuration)
* [Setup](#setup)

## Technologies
* Java 11
* Spring Boot
* JPA/Hibernate
* MySQL
* Spring Security 5 with JWT
* Keycloak
* Netflix Eureka + Zuul + Feign

## Services

| Name                   | Port | Path                    |
| ---------------------- | ---- | ----------------------- |
| car-service            | 8090 | /api/v1/cars            |
| rental-service         | 8091 | /api/v1/rentals         |
| rental-request-service | 8092 | /api/v1/rental-requests |
| user-service           | 8099 | / |
| discovery-service      | 8761 | / |
| gateway-service        | 8082 | / |
| keycloak               | 8080 | / |

## Security
To properly use the application a Bearer token is required, it's acquired by exchanging credentials with the user service. 

## Database 
This application uses a dockerized MySQL database. 

## Configuration

#### Keycloak 
If You have a custom keycloak realm file that You want to import, replace the default one within the "keycloak-imports" folder. Docker will pick it up.

#### Environment Variables
Those are the variables listed in docker-compose.yml.

##### MySQL
```
MYSQL_ROOT_PASSWORD     Your root password
MYSQL_DATABASE          The name of Your database
MYSQL_USER              Your username
MYSQL_PASSWORD          Your password
```

##### Keycloak
```
KEYCLOAK_USER           Admin username
KEYCLOAK_PASSWORD       Admin password
KEYCLOAK_IMPORT         Import command for the realm.json file, best kept unchanged
```

#### Auth service
```
KEYCLOAK_USERNAME       Username of the user manager keycloak account
KEYCLOAK_PASSWORD       Password of said account
```

#### Services
```
EUREKA_DEFAULTZONE      Eureka discovery service URL

KEYCLOAK_AUTH           Keycloak service URL
KEYCLOAK_REALM          Realm name
KEYCLOAK_CLIENT         Client name
KEYCLOAK_CLIENT_SECRET  Client secret

DATABASE_HOST           URL of Your databse
DATABASE_NAME           Name of Your database
DATABASE_USER           Db username
DATABASE_PASSWORD       Db password
```


## Setup
We'll build the images, and execute them with docker-compose. The linux base this configuration uses is openjdk:11-slim.

First, build the application. The jars that are generated with this command are going to be used to build our custom docker images.
```
$ mvnw install
```
After that, run the application within docker using the following command. Any missing images such as jboss/keycloak or mysql/mysql-server will be pulled automatically from the docker hub.
```
$ docker-compose up
```



