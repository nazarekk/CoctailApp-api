CREATE TABLE ROLE (
    ID SERIAL PRIMARY KEY,
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
    ROLEID BIGINT REFERENCES ROLE(ID),
    ACTIVATION TEXT,
    ISACTIVE BOOLEAN
);

CREATE TABLE FRIENDSSTATUS (
    ID SERIAL PRIMARY KEY,
    STATUSNAME TEXT
);

INSERT INTO
    FRIENDSSTATUS (ID, STATUSNAME)
VALUES
    (1, 'None');

INSERT INTO
    FRIENDSSTATUS (ID, STATUSNAME)
VALUES
    (2, 'Awaiting confirmation');

INSERT INTO
    FRIENDSSTATUS (ID, STATUSNAME)
VALUES
    (3, 'Waiting for response');

INSERT INTO
    FRIENDSSTATUS (ID, STATUSNAME)
VALUES
    (4, 'Friends');

CREATE TABLE FRIENDLIST (
    ID SERIAL PRIMARY KEY,
    OWNERID BIGINT REFERENCES USERS(USERID),
    FRIENDID BIGINT REFERENCES USERS(USERID),
    STATUSID BIGINT REFERENCES FRIENDSSTATUS(ID)
);

CREATE TABLE INGREDIENTS (
    ID SERIAL PRIMARY KEY,
    INGRNAME TEXT
);

CREATE TABLE USERTOSTOCK (
    ID SERIAL PRIMARY KEY,
    USERID BIGINT REFERENCES USERS(USERID),
    INGREDIENTID BIGINT REFERENCES INGREDIENTS(ID),
    QUANTITY BIGINT
);

CREATE TABLE RECIPES (
    ID SERIAL PRIMARY KEY,
    RECIPE TEXT,
    RATING BIGINT
);

CREATE TABLE RECIPESTOINGR (
    ID SERIAL PRIMARY KEY,
    RECIPEID BIGINT REFERENCES RECIPES(ID),
    INGREDIENTID BIGINT REFERENCES INGREDIENTS(ID)
);

CREATE TABLE USERTORECIPES (
    ID SERIAL PRIMARY KEY,
    USERID BIGINT REFERENCES USERS(USERID),
    RECIPEID BIGINT REFERENCES RECIPES(ID)
);

CREATE TABLE EVENTS (
    ID SERIAL PRIMARY KEY,
    CREATEID BIGINT REFERENCES USERS(USERID),
    EVENTTIME TIMESTAMP,
    EVENTNAME TEXT
);

CREATE TABLE EVENTSTORECIPES (
    ID SERIAL PRIMARY KEY,
    EVENTID BIGINT REFERENCES EVENTS(ID),
    RECIPEID BIGINT REFERENCES RECIPES(ID)
);

CREATE TABLE EVENTSTOUSERS (
    ID SERIAL PRIMARY KEY,
    EVENTID BIGINT REFERENCES EVENTS(ID),
    USERID BIGINT REFERENCES USERS(USERID)
);

CREATE TABLE KITCHENWARE (
    ID SERIAL PRIMARY KEY,
    KITCHNAME TEXT
);

CREATE TABLE KITCHENWARETORECIPES (
    ID SERIAL PRIMARY KEY,
    KITCHENWAREID BIGINT REFERENCES KITCHENWARE(ID),
    RECIPEID BIGINT REFERENCES RECIPES(ID)
);
