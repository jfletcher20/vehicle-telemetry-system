SELECT * FROM PUBLIC.INFORMATION_SCHEMA;

delete from uloge;
delete from grupe;
delete from korisnici;
delete from vozila;

INSERT INTO korisnici (korisnik, ime, prezime, lozinka, email) VALUES
	 ('jdean','James','Dean','123456','jdean@foi.hr'),
	 ('pgreen','Peter','Green','123456','pgreen@foi.hr'),
	 ('tjones','Tom','Jones','123456','tjones@foi.hr'),
	 ('jkostelic','Janica','Kostelic','123456','janica@foi.hr'),
	 ('jlennon','John','Lennon','123456','john@foi.hr'),
	 ('pmccartney','Paul','McCartney','123456','paul@foi.hr'),
	 ('gharrison','George','Harrison','123456','george@foi.hr'),
	 ('rstar','Ringo','Star','123456','ringo@foi.hr'),
	 ('joplin','Janis','Joplin','123456','janis@foi.hr'),
	 ('jlopez','Jennifer','Lopez','123456','jlo@foi.hr'),
	 ('pkos','Pero','Kos','123456','pero@foi.hr'),
	 ('mmedved','Mato','Medved','123456','mmedved@foi.hr'),
	 ('ivuk','Ivo','Vuk','123456','ivuk@foi.hr'),
	 ('fvrana','Fran','Vrana','123456','fvrana@foi.hr'),
	 ('hgavran','Helga','Gavran','123456','hgavran@foi.hr'),
	 ('lris','Lara','Ris','123456','lris@foi.hr'),
	 ('ssokol','Sonja','Sokol','123456','ssokol@foi.hr'),
	 ('dlasta','Dunja','Lasta','123456','dlasta@foi.hr'),
	 ('lfazan','Luka','Fazan','123456','lfazan@foi.hr'),
	 ('vtovar','Vice','Tovar','123456','ssokol@foi.hr');

INSERT INTO grupe (grupa, naziv) VALUES
	 ('manager-gui','Manager GUI'),
	 ('manager-script','Manager Script'),
	 ('manager-jmx','Manager JMX'),
	 ('manager-status','Manager Status'),
	 ('admin-gui','Admin GUI'),
	 ('admin-script','Admin Script'),
	 ('nwtis','NWTiS korisnik'),
	 ('admin','NWTiS Asmin');
	 
INSERT INTO uloge (korisnik, grupa) VALUES
	 ('pkos', 'manager-gui'),
	 ('pkos', 'manager-script'),
	 ('pkos', 'manager-jmx'),
	 ('pkos', 'manager-status'),
	 ('mmedved', 'admin-gui'),
	 ('mmedved', 'admin-script'),
	 ('pkos', 'admin'),
	 ('mmedved', 'nwtis'),
	 ('ivuk', 'nwtis'),
	 ('fvrana', 'nwtis'),
	 ('hgavran', 'nwtis'),
	 ('lris', 'nwtis'),
	 ('ssokol', 'nwtis'),
	 ('dlasta', 'nwtis'),
	 ('lfazan', 'nwtis'),
	 ('vtovar', 'nwtis');
	 
INSERT INTO vozila (vozilo, proizvodac, model, vlasnik) VALUES
	 (1, 'Tesla', 'Model 3', 'pkos'),
	 (2, 'Tesla', 'Model S', 'pkos'),
	 (3, 'Tesla', 'Model X', 'mmedved'),
	 (4, 'Tesla', 'Model Y', 'ssokol'),	 
	 (5, 'Tesla', 'Cybertruck', 'ivuk');
