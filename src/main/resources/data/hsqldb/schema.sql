DROP TABLE Greeting IF EXISTS;

CREATE TABLE Greeting (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL
	, text VARCHAR(100) NOT NULL
	, PRIMARY KEY(id) 
);