package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

/**
 * Zapis PodaciRadara.
 *
 * @param id identifikator radara
 * @param adresaRegistracije adresa poslužitelja za registraciju radara
 * @param mreznaVrataRegistracije mrežna vrata poslužitelja za registraciju radara
 * @param maksUdaljenost maksimalna udaljenost do koje radar mjeri brzinu
 * @param gpsSirina GPS širina pozicije radara
 * @param gpsDuzina GPS dužina pozicije radara
 */

public class Radar {

  private int id;
  private String adresaRegistracije;
  private int mreznaVrataRegistracije;
  private int maksUdaljenost;
  private double gpsSirina;
  private double gpsDuzina;

  public Radar(int id, String adresa, int mreznaVrata, double gpsSirina, double gpsDuzina,
      int maksUdaljenost) {
    this.id = id;
    this.adresaRegistracije = adresa;
    this.mreznaVrataRegistracije = mreznaVrata;
    this.maksUdaljenost = maksUdaljenost;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Instancira radara.
   */
  public Radar() {

  }

  /**
   * Dohvati identifikator radara.
   * 
   * @return id identifikator radara
   */
  public int getId() {
    return id;
  }

  /**
   * Postavi identifikator radara.
   * 
   * @param id identifikator radara
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Dohvati maksimalnu udaljenost.
   * 
   * @return maksUdaljenost maksimalna udaljenost do koje radar mjeri brzinu
   */
  public int getMaksUdaljenost() {
    return maksUdaljenost;
  }

  /**
   * Postavi maksimalnu udaljenost.
   * 
   * @param maksUdaljenost maksimalna udaljenost do koje radar mjeri brzinu
   */
  public void setMaksUdaljenost(int maksUdaljenost) {
    this.maksUdaljenost = maksUdaljenost;
  }

  /**
   * Dohvati adresu registracije radara.
   * 
   * @return adresaRegistracije adresa poslužitelja za registraciju radara
   */
  public String getAdresaRegistracije() {
    return adresaRegistracije;
  }

  /**
   * Postavi adresu registracije radara.
   * 
   * @param adresaRegistracije adresa poslužitelja za registraciju radara
   */
  public void setAdresaRegistracije(String adresaRegistracije) {
    this.adresaRegistracije = adresaRegistracije;
  }

  /**
   * Dohvati mrežna vrata registracije radara.
   * 
   * @return mreznaVrataRegistracije mrežna vrata poslužitelja za registraciju radara
   */
  public int getMreznaVrataRegistracije() {
    return mreznaVrataRegistracije;
  }

  /**
   * Postavi mrežna vrata registracije radara.
   * 
   * @param mreznaVrataRegistracije mrežna vrata poslužitelja za registraciju radara
   */
  public void setMreznaVrataRegistracije(int mreznaVrataRegistracije) {
    this.mreznaVrataRegistracije = mreznaVrataRegistracije;
  }

  /**
   * Dohvati GPS širinu pozicije radara.
   * 
   * @return gpsSirina GPS širina pozicije radara
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Postavi GPS širinu pozicije radara.
   * 
   * @param gpsSirina GPS širina pozicije radara
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Dohvati GPS dužinu pozicije radara.
   * 
   * @return gpsDuzina GPS dužina pozicije radara
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Postavi GPS dužinu pozicije radara.
   * 
   * @param gpsDuzina GPS dužina pozicije radara
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
}
