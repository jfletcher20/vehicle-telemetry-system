#!/bin/bash
# Should be run after the database is run
cd ~/jfletcher20/jfletcher20_vjezba_07_dz_2/jfletcher20_vjezba_07_dz_2_app
#target/maven-jlink/default/bin/java -m jfletcher20_vjezba_05_app/edu.unizg.foi.nwtis.jfletcher20.vjezba_05.IzvrsiteljZadatka NWTiS_05.3.txt jfletcher20 123456
#target/maven-jlink/default/bin/java -m jfletcher20_vjezba_05_app/edu.unizg.foi.nwtis.jfletcher20.vjezba_05.IzvrsiteljZadatka NWTiS_05.1.txt jfletcher20 123456

#target/maven-jlink/default/bin/java -m jfletcher20_vjezba_05_app/edu.unizg.foi.nwtis.jfletcher20.vjezba_05.IzvrsiteljZadatka NWTiS_05.4.txt jfletcher20 123456
java -jar jfletcher20_vjezba_07_dz_2_app/target/jfletcher20_vjezba_07_dz_2_app-1.0.0.jar NWTIS_05.4.txt jfletcher20 123456
