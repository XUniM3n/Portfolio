CREATE TABLE IF NOT EXISTS users
(
	id serial not null,
	username varchar(50),
	password varchar(64),
	cat_url varchar(200),
	role varchar(20)
);