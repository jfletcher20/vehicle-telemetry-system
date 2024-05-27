#!/bin/bash

# CentralniSustav server
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &

# sleep 2 &

# PosluziteljKazni
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &

# PosluziteljRadara R1
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &

# PosluziteljRadara R2
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt
