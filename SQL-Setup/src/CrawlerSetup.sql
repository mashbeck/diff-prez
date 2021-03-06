CREATE DATABASE TrumpGov;

CREATE DATABASE ObamaGov;

USE TrumpGov;

CREATE TABLE trump(
    url VARCHAR(256),
    description TEXT(65535),
    id INT
);

CREATE TABLE trumpWords(
    word VARCHAR(64),
    id INT
);

USE ObamaGov;

CREATE TABLE obama(
    url VARCHAR(256),
    description TEXT(65535),
    id INT
);

CREATE TABLE obamaWords(
    word VARCHAR(64),
    id INT
);