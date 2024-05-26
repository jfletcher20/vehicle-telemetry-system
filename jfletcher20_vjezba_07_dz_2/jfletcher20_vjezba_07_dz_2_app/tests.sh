#!/bin/bash

# Klijent testira posluziteljkazni
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti.Klijent NWTiS_DZ1_K.txt 1 1708073749078 1708074766471 &
java -cp target/jfletcher20_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti.Klijent NWTiS_DZ1_K.txt 1708073749078 1708074766471 &
