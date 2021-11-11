CREATE TABLE users
(
    id serial,
    email varchar(100) NOT NULL,
    password varchar(40) NOT NULL,
    PRIMARY KEY (id)
);