#!/bin/bash

# CentralniSustav server
java -cp target/jfletcher20_vjezba_04_dz_1_app-1.0.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &

# PosluziteljRadara R1
java -cp target/jfletcher20_vjezba_04_dz_1_app-1.0.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &

# PosluziteljRadara R2
java -cp target/jfletcher20_vjezba_04_dz_1_app-1.0.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &

# SimulatorVozila V1
java -cp target/jfletcher20_vjezba_04_dz_1_app-1.0.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.klijenti.SimulatorVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 &

# SimulatorVozila V2
java -cp target/jfletcher20_vjezba_04_dz_1_app-1.0.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.klijenti.SimulatorVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V2.csv 2 &