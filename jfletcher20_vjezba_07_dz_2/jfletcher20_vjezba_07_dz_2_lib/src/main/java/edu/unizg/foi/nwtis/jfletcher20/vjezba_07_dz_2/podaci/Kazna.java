package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

public class Kazna {
  
  int id;
  long vrijemePocetak, vrijemeKraj;
  double brzina, gpsSirina, gpsDuzina;
  double gpsSirinaRadar, gpsDuzinaRadar;
  
  public Kazna(int id, long vrijemePocetak, long vrijemeKraj, double brzina, double gpsSirina,
      double gpsDuzina, double gpsSirinaRadar, double gpsDuzinaRadar) {
    this.id = id;
    this.vrijemePocetak = vrijemePocetak;
    this.vrijemeKraj = vrijemeKraj;
    this.brzina = brzina;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
    this.gpsSirinaRadar = gpsSirinaRadar;
    this.gpsDuzinaRadar = gpsDuzinaRadar;
  }

}
