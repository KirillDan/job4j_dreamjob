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