/*

create table kazne (
	rb int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	id int NOT NULL,
	vrijemePocetak bigint NOT NULL,
	vrijemeKraj bigint NOT NULL,
	brzina double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL,
	gpsSirinaRadar double NOT NULL,
	gpsDuzinaRadar double NOT NULL
); 

create table voznje (
	rb int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	id int NOT NULL,
	broj int NOT NULL,
	vrijeme bigint NOT NULL,
	brzina double NOT NULL,
	snaga double NOT NULL,
	struja double NOT NULL,
	visina double NOT NULL,
	gpsBrzina double NOT NULL,
	tempVozila int NOT NULL,
	postotakBaterija int NOT NULL,
	naponBaterija double NOT NULL,
	kapacitetBaterija int NOT NULL,
	tempBaterija int NOT NULL,
	preostaloKm double NOT NULL,
	ukupnoKm double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL
); 

create table pracenevoznje (
	rb int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	id int NOT NULL,
	broj int NOT NULL,
	vrijeme bigint NOT NULL,
	brzina double NOT NULL,
	snaga double NOT NULL,
	struja double NOT NULL,
	visina double NOT NULL,
	gpsBrzina double NOT NULL,
	tempVozila int NOT NULL,
	postotakBaterija int NOT NULL,
	naponBaterija double NOT NULL,
	kapacitetBaterija int NOT NULL,
	tempBaterija int NOT NULL,
	preostaloKm double NOT NULL,
	ukupnoKm double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL
);

*/

/* CHECK ALL tables created BY USER */
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC';