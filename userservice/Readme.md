**UserService**
-
This service does following:

Implement a secure user authentication system to allow users to register, log in, and manage their accounts.

Pre-requisites
-
- maven - 3.8.1
- jdk - 17
- docker - to run mysql 8.3.0

Steps to run `userservice`
-
1. Build the service using `mvn clean install`. First run might take time depending on the network connection and available mirrors.

2. Run `mysql` via docker using following command: `docker run -p 3306:3306 --name mysqluserservice -e MYSQL_PASSWORD=sa -e MYSQL_USER=sa -e MYSQL_ROOT_PASSWORD=sa -e MYSQL_DATABASE=bookplatformdb  mysql:8.3.0`. NOTE : running docker db can be skipped if already ran as part of `bookservice`.

3. Run the `userservice` either through IntelliJ Idea or through commandline by navigating to the folder in which `pom.xml` is present: `mvn spring-boot:run`. The service will run on port `9000`.

4. To access database from terminal run the command : `docker exec -it mysqluserservice bash` to access the container followed by `mysql -u sa bookplatformdb -p` this will prompt to enter password.

Swagger
- 
- API docs can be seen in json format at http://localhost:9000/api-docs
- API docs can be seen in yaml format at http://localhost:9000/api-docs.yaml
- Swagger ui can be reached at http://localhost:9000/swagger-ui/index.html#/ 

Postman collection
-
- Postman collection can be imported using `userservice/userservice.postman_collection.json`