CREATE TABLE ROLE (
                      ID BIGINT PRIMARY KEY,
                      ROLENAME TEXT
);

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (1, 'ROLE_UNCONFIRMED');

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (2, 'ROLE_CONFIRMED');

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (3, 'ROLE_ADMIN');

INSERT INTO
    ROLE (ID, ROLENAME)
VALUES
    (4, 'ROLE_MODERATOR');

CREATE TABLE USERS (
                       USERID SERIAL PRIMARY KEY,
                       EMAIL TEXT UNIQUE,
                       NICKNAME TEXT,
                       PASSWORD TEXT,
                       ROLEID BIGINT REFERENCES ROLE(ID),
                       ACTIVATION TEXT,
                       ISACTIVE BOOLEAN,
                       INFORMATION TEXT,
                       PHOTO TEXT default 'D4q5xVf'
);

CREATE TABLE FRIENDSSTATUS (
                               ID SERIAL PRIMARY KEY,
                               STATUSNAME TEXT
);

CREATE TYPE Alcohol AS ENUM ('NONALCOHOL', 'LOWALCOHOL', 'ALCOHOL');

CREATE TABLE FRIENDLIST (
                            ID SERIAL PRIMARY KEY,
                            OWNERID BIGINT REFERENCES USERS(USERID),
                            FRIENDID BIGINT REFERENCES USERS(USERID),
                            STATUSID BIGINT REFERENCES FRIENDSSTATUS(ID)
);

create table ingredients
(
    id              serial
        constraint ingredients_pkey
            primary key,
    ingredientsname text,
    type            text,
    category        text,
    isactive        boolean,
    image           text default 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fthenounproject.com%2Fterm%2Fundefined-document%2F1738131%2F&psig=AOvVaw2-u4NSaidUOEi1uE2xxZw1&ust=1638920164444000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNiozcWr0PQCFQAAAAAdAAAAABAD'::text
);

alter table ingredients
    owner to postgres;

CREATE TABLE USERTOSTOCK (
                             ID SERIAL PRIMARY KEY,
                             USERID BIGINT REFERENCES USERS(USERID),
                             INGREDIENTID BIGINT REFERENCES INGREDIENTS(ID),
                             QUANTITY BIGINT
);

create table recipes
(
    id        serial
        constraint recipes_pkey
            primary key,
    name      text,
    rating    bigint,
    sugarless boolean,
    isactive  boolean,
    image     text default 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fthenounproject.com%2Fterm%2Fundefined-document%2F1738131%2F&psig=AOvVaw2-u4NSaidUOEi1uE2xxZw1&ust=1638920164444000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNiozcWr0PQCFQAAAAAdAAAAABAD'::text,
    recipe    text,
    alcohol   text
);

alter table recipes
    owner to postgres;

create table recipestoingr
(
    id           serial
        constraint recipestoingr_pkey
            primary key,
    recipeid     bigint
        constraint recipestoingr_recipeid_fkey
            references recipes
            on delete cascade,
    ingredientid bigint
        constraint recipestoingr_ingredientid_fkey
            references ingredients
            on delete cascade
);

alter table recipestoingr
    owner to postgres;

create table usertorecipes
(
    id       serial
        constraint usertorecipes_pkey
            primary key,
    userid   bigint
        constraint usertorecipes_userid_fkey
            references users
            on delete cascade,
    recipeid bigint
        constraint usertorecipes_recipeid_fkey
            references recipes
            on delete cascade,
    liked    boolean
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

create table kitchenware
(
    id              serial
        constraint kitchenware_pkey
            primary key,
    kitchenwarename text,
    type            text,
    category        text,
    isactive        boolean,
    image           text default 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fthenounproject.com%2Fterm%2Fundefined-document%2F1738131%2F&psig=AOvVaw2-u4NSaidUOEi1uE2xxZw1&ust=1638920164444000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNiozcWr0PQCFQAAAAAdAAAAABAD'::text
);

create table kitchenwaretorecipes
(
    id            serial
        constraint kitchenwaretorecipes_pkey
            primary key,
    kitchenwareid bigint
        constraint kitchenwaretorecipes_kitchenwareid_fkey
            references kitchenware
            on delete cascade,
    recipeid      bigint
        constraint kitchenwaretorecipes_recipeid_fkey
            references recipes
            on delete cascade
);