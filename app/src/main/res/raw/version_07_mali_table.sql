CREATE TABLE mali (
    yearId_FK   INTEGER        REFERENCES yearMali (id) ON DELETE NO ACTION
                                                        ON UPDATE CASCADE
                                                        MATCH SIMPLE
                               NOT NULL,
    id          INTEGER        PRIMARY KEY AUTOINCREMENT,
    num         INTEGER        NOT NULL,
    mali_type   VARCHAR (10)   NOT NULL,
    personBedId_FK INTEGER        REFERENCES person (id) ON DELETE NO ACTION
                                                      ON UPDATE CASCADE
                                                      MATCH SIMPLE,
    personBesdId_FK INTEGER        REFERENCES person (id) ON DELETE NO ACTION
                                                      ON UPDATE CASCADE
                                                      MATCH SIMPLE,
    mali_date VARCHAR (10)   NOT NULL,
    description NVARCHAR (200),
    vcheck_sarresid_date VARCHAR (10),
    vcheck_bank     NVARCHAR (200),
    vcheck_serial   NVARCHAR (200),
    amount          LONG     NOT NULL,
    sync_date   VARCHAR (10),
    atf_num     INTEGER,
    lat         DOUBLE,
    lon         DOUBLE,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE UNIQUE INDEX mali_unique_num_index ON mali (
    num,
    yearId_FK
);