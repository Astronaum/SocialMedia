-- Insert initial data into Person
INSERT INTO person (nom, prenom, datenaissance, description) VALUES ('John', 'Doe', '1990-01-15', 'Software Developer');
INSERT INTO person (nom, prenom, datenaissance, description) VALUES ('Jane', 'Doe', '1985-07-20', 'Graphic Designer');
INSERT INTO person (nom, prenom, datenaissance, description) VALUES ('Alice', 'Smith', '1992-03-10', 'Teacher');
INSERT INTO person (nom, prenom, datenaissance, description) VALUES ('Bob', 'Johnson', '1988-11-30', 'Data Analyst');
INSERT INTO person (nom, prenom, datenaissance, description) VALUES ('Charlie', 'Brown', '1995-08-25', 'Entrepreneur');
INSERT INTO person (nom, prenom, datenaissance, description) VALUES ('Barry', 'Alan', '1970-01-25', 'Super Hero');

-- Insert initial data into Relationship
INSERT INTO relationship (persona_id, personb_id, typerelation) VALUES (1, 2, 'AMI');
INSERT INTO relationship (persona_id, personb_id, typerelation) VALUES (1, 3, 'COLLEGUE');
INSERT INTO relationship (persona_id, personb_id, typerelation) VALUES (2, 4, 'AMI');
INSERT INTO relationship (persona_id, personb_id, typerelation) VALUES (3, 5, 'COLLEGUE');
INSERT INTO relationship (persona_id, personb_id, typerelation) VALUES (4, 5, 'AMI');
