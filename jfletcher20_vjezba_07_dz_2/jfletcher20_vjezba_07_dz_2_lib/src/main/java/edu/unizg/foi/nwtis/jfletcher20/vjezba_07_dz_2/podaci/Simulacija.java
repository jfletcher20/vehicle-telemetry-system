package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

/**
 * Zapis Simulacije.
 *
 * @param idVozila identifikator e-vozila
 */

public class Simulacija {

  Voznja voznja;
  private int idVozila, trajanjeSek, trajanjePauze;

  public Simulacija(Voznja voznja, int idVozila, int trajanjeSek, int trajanjePauze) {
    this.voznja = voznja;
    this.idVozila = idVozila;
    this.trajanjeSek = trajanjeSek;
    this.trajanjePauze = trajanjePauze;
  }

  /**
   * Instancira vožnju.
   */
  public Simulacija() {

  }

  /**
   * Dohvati datoteku s podacima o vožnji.
   * 
   * @return podaciVozila podaci o vožnji
   */
  public Voznja getPodaciVozila() {
    return voznja;
  }
  
  /**
   * Postavi datoteku s podacima o vožnji.
   * 
   * @param podaciVoznjeDatoteka podaci o vožnji
   */
  public void setPodaciVozila(Voznja podaciVoznjeDatoteka) {
    this.voznja = podaciVoznjeDatoteka;
  }
  
  /**
   * Dohvati identifikator e-vozila.
   * 
   * @return id identifikator e-vozila
   */
  public int getId() {
    return idVozila;
  }
  
  /**
   * Postavi identifikator e-vozila.
   * 
   * @param id identifikator e-vozila
   */
  public void setId(int idVozila) {
    this.idVozila = idVozila;
  }
  
  /**
   * Dohvati trajanje sekunde.
   * 
   * @return trajanjeSek trajanje sekunde
   */
  
  public int getTrajanjeSek() {
    return trajanjeSek;
  }
  
  /**
   * Postavi trajanje sekunde.
   * 
   * @param trajanjeSek trajanje sekunde
   */
  public void setTrajanjeSek(int trajanjeSek) {
    this.trajanjeSek = trajanjeSek;
  }
  
  /**
   * Dohvati trajanje pauze.
   * 
   * @return trajanjePauze trajanje pauze
   */
  public int getTrajanjePauze() {
    return trajanjePauze;
  }
  
  /**
   * Postavi trajanje pauze.
   * 
   * @param trajanjePauze trajanje pauze
   */
  public void setTrajanjePauze(int trajanjePauze) {
    this.trajanjePauze = trajanjePauze;
  }

}
