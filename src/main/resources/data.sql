CREATE SEQUENCE SANCTIONED_PEOPLE_SEQ START WITH 11 INCREMENT BY 1;
CREATE TABLE SANCTIONED_PEOPLE(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, person_name VARCHAR(255) UNIQUE);
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('OSAMA BIN LADEN');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('VLADIMIR PUTIN');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('MARIA VORONTSOVA');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('DMITRY MEDVEDEV');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('ALEXANDER LUKASHENKO');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('SERGEY LAVROV');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('VLADIMIR KOZHIN');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('SERGEI IVANOV');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('YEVGENY MIKHAILOV');
INSERT INTO SANCTIONED_PEOPLE (person_name) VALUES ('VALERY KULIKOV');