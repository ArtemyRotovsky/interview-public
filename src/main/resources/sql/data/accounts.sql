CREATE TABLE ACCOUNTS (
   ID VARCHAR PRIMARY KEY,
   FIRST_NAME VARCHAR (128) NOT NULL,
   LAST_NAME VARCHAR (128) NOT NULL,
   BALANCE NUMERIC NOT NULL DEFAULT 0
);