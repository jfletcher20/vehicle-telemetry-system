package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

/**
 * Zapis Kazna.
 * 
 * @param id identifikator kazne
 * @param vrijemePocetak vrijeme kada je radar uočio početak brze vožnju
 * @param vrijemeKraj vrijeme kada je radar uočio brzu vožnju dulju od dozvoljenog vremena
 * @param brzina brzina vozila u vrijemeKraj
 * @param gpsSirina gps širina pozicije vozila u vrijeme
 * @param gpsDuzina gps dužina pozicije vozila u vrijeme
 * @param gpsSirinaRadar gps širina pozicije radara
 * @param gpsDuzinaRadar gps dužina pozicije radara
 */

public class Kazna {

  private int id;
  private long vrijemePocetak, vrijemeKraj;
  private double brzina, gpsSirina, gpsDuzina;
  private double gpsSirinaRadar, gpsDuzinaRadar;

  /**
   * Instancira kaznu.
   */
  public Kazna() {

  }

  /**
   * Instancira kaznu s podacima.
   * 
   * @param id identifikator kazne
   * @param vrijemePocetak vrijeme kada je radar uočio početak brze vožnju
   * @param vrijemeKraj vrijeme kada je radar uočio brzu vožnju dulju od dozvoljenog vremena
   * @param brzina brzina vozila u vrijemeKraj
   * @param gpsSirina gps širina pozicije vozila u vrijeme
   * @param gpsDuzina gps dužina pozicije vozila u vrijeme
   * @param gpsSirinaRadar gps širina pozicije radara
   * @param gpsDuzinaRadar gps dužina pozicije radara
   */
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

  /**
   * Dohvati identifikator kazne.
   * 
   * @return identifikator kazne
   */
  public int getId() {
    return id;
  }

  /**
   * Dohvati vrijeme početak.
   * 
   * @return vrijeme početak
   */
  public long getVrijemePocetak() {
    return vrijemePocetak;
  }

  /**
   * Dohvati vrijeme kraj.
   * 
   * @return vrijeme kraj
   */
  public long getVrijemeKraj() {
    return vrijemeKraj;
  }

  /**
   * Dohvati brzinu.
   * 
   * @return brzina
   */
  public double getBrzina() {
    return brzina;
  }

  /**
   * Dohvati gps širinu.
   * 
   * @return gps širina
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Dohvati gps dužinu.
   * 
   * @return gps dužina
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Dohvati gps širinu radara.
   * 
   * @return gps širina radara
   */
  public double getGpsSirinaRadar() {
    return gpsSirinaRadar;
  }

  /**
   * Dohvati gps dužinu radara.
   * 
   * @return gps dužina radara
   */
  public double getGpsDuzinaRadar() {
    return gpsDuzinaRadar;
  }

  /**
   * Postavi identifikator kazne.
   * 
   * @param id identifikator kazne
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Postavi vrijeme početak.
   * 
   * @param vrijemePocetak vrijeme početak
   */
  public void setVrijemePocetak(long vrijemePocetak) {
    this.vrijemePocetak = vrijemePocetak;
  }

  /**
   * Postavi vrijeme kraj.
   * 
   * @param vrijemeKraj vrijeme kraj
   */
  public void setVrijemeKraj(long vrijemeKraj) {
    this.vrijemeKraj = vrijemeKraj;
  }

  /**
   * Postavi brzinu.
   * 
   * @param brzina brzina
   */
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  /**
   * Postavi gps širinu.
   * 
   * @param gpsSirina gpsSirina
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Postavi gps dužinu.
   * 
   * @param gpsDuzina gpsDuzina
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Postavi gps širinu radara.
   * 
   * @param gpsSirinaRadar gpsSirinaRadar
   */
  public void setGpsSirinaRadar(double gpsSirinaRadar) {
    this.gpsSirinaRadar = gpsSirinaRadar;
  }

  /**
   * Postavi gps dužinu radara.
   * 
   * @param gpsDuzinaRadar gpsDuzinaRadar
   */
  public void setGpsDuzinaRadar(double gpsDuzinaRadar) {
    this.gpsDuzinaRadar = gpsDuzinaRadar;
  }

}
