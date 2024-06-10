package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.podaci;

import java.io.Serializable;

public class Kazna implements Serializable {
  
  private static final long serialVersionUID = -6130742431018181852L;
  private int id;
  private long vrijemePocetak, vrijemeKraj;
  private double brzina, gpsSirina, gpsDuzina;
  private double gpsSirinaRadar, gpsDuzinaRadar;
  
  public Kazna() {
    
  }
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
  
  // create all setters
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setVrijemePocetak(long vrijemePocetak) {
    this.vrijemePocetak = vrijemePocetak;
  }
  
  public void setVrijemeKraj(long vrijemeKraj) {
    this.vrijemeKraj = vrijemeKraj;
  }
  
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }
  
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }
  
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
  
  public void setGpsSirinaRadar(double gpsSirinaRadar) {
    this.gpsSirinaRadar = gpsSirinaRadar;
  }
  
  public void setGpsDuzinaRadar(double gpsDuzinaRadar) {
    this.gpsDuzinaRadar = gpsDuzinaRadar;
  }

}
