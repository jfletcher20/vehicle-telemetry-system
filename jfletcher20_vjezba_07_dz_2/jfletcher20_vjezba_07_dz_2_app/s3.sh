#!/bin/bash

# PosluziteljRadara R1
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &

# PosluziteljRadara R2
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &
