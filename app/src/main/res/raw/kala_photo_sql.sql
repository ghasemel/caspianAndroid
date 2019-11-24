create table kalaPhoto
(
	yearId_FK integer references yearMali(id) ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE,
	id integer PRIMARY KEY AUTOINCREMENT,
	sourceId integer not null,
	code varchar(30) not null,
	fileName nvarchar(500) not null,
	title nvarchar(500)
);