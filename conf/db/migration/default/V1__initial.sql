-- !Ups
 
DROP TABLE IF EXISTS users, categories, boards, board_shared_with_user, favorites, comments, board_chat;

-- Users, their emails, names and passwords
CREATE TABLE users
(
	id SERIAL PRIMARY KEY,
	email TEXT UNIQUE NOT NULL,
	username TEXT UNIQUE NOT NULL,
	password TEXT NOT NULL,
	auth_token TEXT,
	creation_time TIMESTAMP NOT NULL,
	last_login TIMESTAMP
);

-- Categories hold which boards are where in relation to user
CREATE TABLE categories
(
	id SERIAL PRIMARY KEY,
	user_id INT REFERENCES users,
	name TEXT NOT NULL	
);

-- Boards hold multiple stickies
CREATE TABLE boards
(
	id SERIAL PRIMARY KEY,
	category_id INT REFERENCES categories,
	name TEXT NOT NULL,
	creation_time TIMESTAMP NOT NULL,
	last_modified TIMESTAMP	
);

CREATE TABLE board_shared_with_user
(
	board_id INT REFERENCES boards,
	user_id INT REFERENCES users,
	edit_privileges BOOLEAN
);

-- Stickies hold information
CREATE TABLE stickies
(
	id SERIAL PRIMARY KEY,
	board_id INT REFERENCES boards,
	name TEXT,
	content TEXT NOT NULL,
	x INT,
	y INT,
	creation_time TIMESTAMP NOT NULL,
	last_modified TIMESTAMP
);

CREATE TABLE favorites
(
	user_id INT REFERENCES users,
	sticky_id INT REFERENCES stickies
);

CREATE TABLE comments
(
	user_id INT REFERENCES users,
	sticky_id INT REFERENCES stickies,
	comment TEXT NOT NULL
);

CREATE TABLE board_chat
(
	user_id INT REFERENCES users,
	board_id INT REFERENCES boards,
	message TEXT NOT NULL,
	creation_time TIMESTAMP NOT NULL
);


