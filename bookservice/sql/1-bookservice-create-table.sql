DROP TABLE Book
DROP TABLE User

CREATE TABLE User(
    id BIGINT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    userName VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    createdDate DATETIME,
    modifiedDate DATETIME,
    version BIGINT,
    address_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id) REFERENCES Address(id)
);

CREATE TABLE Book(
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(300) NOT NULL,
    author VARCHAR(200) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    bookCondition VARCHAR(10) NOT NULL,
    createdDate TIMESTAMP,
    modifiedDate TIMESTAMP,
    version BIGINT,
    userID BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (userID) REFERENCES User(id)
);