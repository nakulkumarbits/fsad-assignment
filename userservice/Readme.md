**UserService**

This service does following:

Implement a secure user authentication system to allow users to register, log in, and manage their accounts.

Pre-requisites

- maven - 3.8.1
- jdk - 17
- docker - to run mysql 8.3.0

Steps to run `userservice`

1. Build the service using `mvn clean install`. First run might take time depending on the network connection and available mirrors.

2. Run `mysql` via docker using following command: `docker run -p 3306:3306 --name mysqluserservice -e MYSQL_PASSWORD=sa -e MYSQL_USER=sa -e MYSQL_ROOT_PASSWORD=sa -e MYSQL_DATABASE=userdb  mysql:8.3.0`

3. Run the `userservice` either through IntelliJ Idea or through commandline by navigating to the folder in which `pom.xml` is present: `mvn spring-boot:run`. The service will run on port `9000`.

4. To access database from terminal run the command : `docker exec -it mysqluserservice bash` to access the container followed by `mysql -u sa userdb -p` this will prompt to enter password.