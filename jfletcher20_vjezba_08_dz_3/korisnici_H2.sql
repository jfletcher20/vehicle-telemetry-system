DROP TABLE IF EXISTS pracenevoznje;
DROP TABLE IF EXISTS kazne;
drop table if exists uloge;
drop table if exists grupe;
drop table if exists vozila;
drop table if exists korisnici;

CREATE TABLE korisnici (
	korisnik VARCHAR(20) NOT NULL,
	ime VARCHAR(25),
	prezime VARCHAR(25),
	lozinka VARCHAR(20),
	email VARCHAR(100),
	PRIMARY KEY (korisnik)
);

CREATE TABLE grupe (
	grupa VARCHAR(20) NOT NULL,
	naziv VARCHAR(55),
	PRIMARY KEY (grupa)
);

CREATE TABLE uloge (
	korisnik VARCHAR(20) NOT NULL,
	grupa VARCHAR(20) NOT NULL,
	PRIMARY KEY (korisnik, grupa),
	FOREIGN KEY (korisnik) REFERENCES korisnici (korisnik),
	FOREIGN KEY (grupa) REFERENCES grupe (grupa)
);

CREATE TABLE vozila (
	vozilo int NOT NULL,
	proizvodac VARCHAR(50) NOT NULL,
	model VARCHAR(20) NOT NULL,
	vlasnik VARCHAR(20) NOT NULL,
	PRIMARY KEY (vozilo),
	FOREIGN KEY (vlasnik) REFERENCES korisnici (korisnik)	
);

ALTER TABLE pracenevoznje ADD FOREIGN KEY (id) REFERENCES vozila (vozilo);
ALTER TABLE kazne ADD FOREIGN KEY (id) REFERENCES vozila (vozilo);

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE korisnici TO "aplikacija";
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE grupe TO "aplikacija";
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE uloge TO "aplikacija";
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE vozila TO "aplikacija";
