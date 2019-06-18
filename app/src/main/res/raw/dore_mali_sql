PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM yearMali;

DROP TABLE yearMali;

CREATE TABLE yearMali (
    id            INTEGER      PRIMARY KEY AUTOINCREMENT,
    userId_FK     INTEGER      REFERENCES user (userId) MATCH SIMPLE,
    daftar        INTEGER,
    year          INTEGER,
    company       VARCHAR (70),
    database_name VARCHAR (30),
    is_current    BOOLEAN,
    dore_start    VARCHAR (10),
    dore_end      VARCHAR (10) 
);

INSERT INTO yearMali (
                         id,
                         userId_FK,
                         daftar,
                         year,
                         company,
                         database_name,
                         is_current
                     )
                     SELECT id,
                            userId_FK,
                            daftar,
                            year,
                            company,
                            database_name,
                            is_current
                       FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;
