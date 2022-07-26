-- Active: 1658855706107@@127.0.0.1@5432@postgres
create table VIDEO (
    ID_VIDEO SERIAL PRIMARY KEY,
    TITLE varchar(255) UNIQUE
);