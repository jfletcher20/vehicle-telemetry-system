package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

public class Kazna {
  
  private int id;
  private long vrijemePocetak, vrijemeKraj;
  private double brzina, gpsSirina, gpsDuzina;
  private double gpsSirinaRadar, gpsDuzinaRadar;
  
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

  public int getId() {
    return id;
  }

  public long getVrijemePocetak() {
    return vrijemePocetak;
  }

  public long getVrijemeKraj() {
    return vrijemeKraj;
  }

  public double getBrzina() {
    return brzina;
  }

  public double getGpsSirina() {
    return gpsSirina;
  }

  public double getGpsDuzina() {
    return gpsDuzina;
  }

  public double getGpsSirinaRadar() {
    return gpsSirinaRadar;
  }

  public double getGpsDuzinaRadar() {
    return gpsDuzinaRadar;
  }

}
