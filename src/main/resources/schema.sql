DROP TABLE USERS;
CREATE TABLE ROLE (
                      ID BIGINT PRIMARY KEY,
                      ROLENAME TEXT
);

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (1, 'Unconfirmed');

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (2, 'Confirmed');

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (3, 'Admin');

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (4, 'Moderator');

CREATE TABLE USERS (
                       USERID SERIAL PRIMARY KEY,
                       EMAIL TEXT UNIQUE,
                       NICKNAME TEXT,
                       PASSWORD TEXT,
                       ROLEID INTEGER REFERENCES ROLE(ID),
                       ACTIVATION TEXT,
                       ISACTIVE BOOLEAN
);

CREATE TABLE USERS (
                       USERID BIGINT PRIMARY KEY,
                       EMAIL TEXT UNIQUE,
                       PASSWORD TEXT,
                       STATUS INT,
                       ACTIVATION TEXT,
                       ISACTIVE BOOLEAN
);
