CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   post_name TEXT,
   description TEXT,
   created TEXT
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   firstname TEXT,
   lastname TEXT,
   position TEXT
);

CREATE TABLE phote (
	id SERIAL PRIMARY KEY,
	candidate_id SERIAL,
	FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);