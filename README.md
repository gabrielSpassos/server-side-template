# Spring Boot + Thymeleaf POC

Just some example of simple CRUD with Spring Boot MVC and Thymeleaf to render the web pages.

### Features
- [X] List Users
- [X] Create User
- [X] Update User
- [X] Delete User

### Project
[Home](http://localhost:8080)
 
### Database
[H2 Console](http://localhost:8080/h2-console)

```
JDBC URL: jdbc:h2:mem:userdb
User Name: sa
Password: password
```

### Usage 

* Build project
```
./gradlew clean build
```

* Build docker image
```
docker build -t sst .
```

* Run docker image
```
docker run -p 8080:8080 sst:latest
```