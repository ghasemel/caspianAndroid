PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM mpfaktor;

DROP TABLE mpfaktor;

CREATE TABLE mpfaktor (
    yearId_FK   INTEGER        REFERENCES yearMali (id) ON DELETE NO ACTION
                                                        ON UPDATE CASCADE
                                                        MATCH SIMPLE
                               NOT NULL,
    id          INTEGER        PRIMARY KEY AUTOINCREMENT,
    num         INTEGER        NOT NULL,
    personId_FK INTEGER        REFERENCES person (id) ON DELETE NO ACTION
                                                      ON UPDATE CASCADE
                                                      MATCH SIMPLE,
    faktor_date VARCHAR (10)   NOT NULL,
    description NVARCHAR (200),
    avance      DOUBLE,
    afzoode     DOUBLE,
    is_synced   INTEGER,
    sync_date   VARCHAR (10),
    atf_num     INTEGER,
    lat         DOUBLE,
    lon         DOUBLE,
    create_date DATETIME
);

INSERT INTO mpfaktor (
                         yearId_FK,
                         id,
                         num,
                         personId_FK,
                         faktor_date,
                         description,
                         avance,
                         afzoode,
                         is_synced,
                         sync_date,
                         atf_num,
                         lat,
                         lon,
                         create_date
                     )
                     SELECT yearId_FK,
                            id,
                            num,
                            personId_FK,
                            faktor_date,
                            description,
                            avance,
                            afzoode,
                            is_synced,
                            sync_date,
                            atf_num,
                            lat,
                            lon,
                            create_date
                       FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

CREATE UNIQUE INDEX mpfaktor_unique_num_index ON mpfaktor (
    num,
    yearId_FK
);

PRAGMA foreign_keys = 1;
