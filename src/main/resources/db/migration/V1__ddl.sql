-- Active: 1658855706107@@127.0.0.1@5432@postgres
create table VIDEO (
    ID_VIDEO SERIAL PRIMARY KEY,
    TITLE varchar(255) UNIQUE
);

create table USERS (
    ID_USER SERIAL PRIMARY KEY,
    LOGIN_USER varchar(255) UNIQUE
);

-- create table WATCHEDVIDEO (
--     ID_USER INT,
--     ID_VIDEO INT,
--     COMPLETIONPERCENTAGE REAL,
--     WATCHED_TIME TIMESTAMP,
--     CONSTRAINT fk_users
--         FOREIGN KEY(ID_USER)
--         REFERENCES USERS(ID_USER)
--         ON DELETE CASCADE,
--     CONSTRAINT fk_video
--         FOREIGN KEY(ID_VIDEO)
--         REFERENCES VIDEO(ID_VIDEO)
--         ON DELETE CASCADE,
--     CONSTRAINT pk_watchedvideo
--         PRIMARY KEY (ID_USER, ID_VIDEO, WATCHED_TIME)
-- );