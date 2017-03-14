# --- !Ups

CREATE TABLE examples (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  created_at TIMESTAMP
);

# --- !Downs

DROP TABLE examples;
