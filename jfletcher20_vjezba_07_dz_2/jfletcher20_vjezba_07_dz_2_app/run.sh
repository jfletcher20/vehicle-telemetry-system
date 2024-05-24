#!/bin/bash

# CentralniSustav server
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &

# PosluziteljRadara R1
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &

# PosluziteljRadara R2
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &

# PosluziteljKazni
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &

# SimulatorVozila V1
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti.SimulatorVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 &

# SimulatorVozila V2
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti.SimulatorVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V2.csv 2 &

# Klijent testira posluziteljkazni
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti.Klijent NWTiS_DZ1_K.txt 1 1708073749078 1708074766471 &
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti.Klijent NWTiS_DZ1_K.txt 1708073749078 1708074766471 &
