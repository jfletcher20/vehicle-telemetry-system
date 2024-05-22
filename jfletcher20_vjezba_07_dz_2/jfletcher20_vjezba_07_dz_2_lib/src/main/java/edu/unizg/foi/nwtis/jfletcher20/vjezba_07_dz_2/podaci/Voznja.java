package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

/**
 * Zapis Voznje.
 *
 * @param id identifikator e-vozila
 * @param broj broj retka iz datoteke podataka koju SimulatorVozila koristi u radu
 * @param vrijeme vrijeme nastanka podatka u retku
 * @param brzina brzina e-vozila u vrijeme
 * @param snaga snaga koju troši e-vozilo u vrijeme
 * @param struja napon koji troši e-vozilo u vrijeme
 * @param visina nadmorska visina e-vozilo u vrijeme
 * @param gpsBrzina gps brzina e-vozilo u vrijeme
 * @param tempVozila temperatura e-vozila u vrijeme
 * @param postotakBaterija postotak baterija e-vozila u vrijeme
 * @param naponBaterija napon baterija e-vozila u vrijeme
 * @param kapacitetBaterija kapacitet baterija e-vozila u vrijeme
 * @param tempBaterija temperatura baterija e-vozila u vrijeme
 * @param preostaloKm procjena preostalih km e-vozila u vrijeme
 * @param ukupnoKm ukupno prijeđeno km e-vozila u vrijeme
 * @param gpsSirina gps širina pozicije e-vozila u vrijeme
 * @param gpsDuzina gps dužina pozicije e-vozila u vrijemezina
 */

public class Voznja {

  private int id, broj;
  private long vrijeme;
  private double brzina, snaga, struja, visina, gpsBrzina, naponBaterija;
  private int tempVozila, postotakBaterija, kapacitetBaterija, tempBaterija;
  private double preostaloKm, ukupnoKm, gpsSirina, gpsDuzina;

  /**
   * Zapis Voznje.
   *
   * @param id identifikator e-vozila
   * @param broj broj retka iz datoteke podataka koju SimulatorVozila koristi u radu
   * @param vrijeme vrijeme nastanka podatka u retku
   * @param brzina brzina e-vozila u vrijeme
   * @param snaga snaga koju troši e-vozilo u vrijeme
   * @param struja napon koji troši e-vozilo u vrijeme
   * @param visina nadmorska visina e-vozilo u vrijeme
   * @param gpsBrzina gps brzina e-vozilo u vrijeme
   * @param tempVozila temperatura e-vozila u vrijeme
   * @param postotakBaterija postotak baterija e-vozila u vrijeme
   * @param naponBaterija napon baterija e-vozila u vrijeme
   * @param kapacitetBaterija kapacitet baterija e-vozila u vrijeme
   * @param tempBaterija temperatura baterija e-vozila u vrijeme
   * @param preostaloKm procjena preostalih km e-vozila u vrijeme
   * @param ukupnoKm ukupno prijeđeno km e-vozila u vrijeme
   * @param gpsSirina gps širina pozicije e-vozila u vrijeme
   * @param gpsDuzina gps dužina pozicije e-vozila u vrijemezina
   */
  public Voznja(int id, int broj, long vrijeme, double brzina, double snaga, double struja,
      double visina, double gpsBrzina, int tempVozila, int postotakBaterija, double naponBaterija,
      int kapacitetBaterija, int tempBaterija, double preostaloKm, double ukupnoKm,
      double gpsSirina, double gpsDuzina) {
    this.setId(id);
    this.broj = broj;
    this.vrijeme = vrijeme;
    this.brzina = brzina;
    this.snaga = snaga;
    this.struja = struja;
    this.visina = visina;
    this.gpsBrzina = gpsBrzina;
    this.tempVozila = tempVozila;
    this.postotakBaterija = postotakBaterija;
    this.kapacitetBaterija = kapacitetBaterija;
    this.tempBaterija = tempBaterija;
    this.preostaloKm = preostaloKm;
    this.ukupnoKm = ukupnoKm;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Instancira vožnju.
   */
  public Voznja() {

  }


  /**
   * Dohvati identifikator e-vozila.
   * 
   * @return id identifikator e-vozila
   */
  public int getId() {
    return id;
  }

  /**
   * Postavi identifikator e-vozila.
   * 
   * @param id identifikator e-vozila
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Dohvati broj vožnje.
   * 
   * @return broj broj vožnje
   */
  public int getBroj() {
    return broj;
  }

  /**
   * Postavi broj vožnje.
   * 
   * @param broj broj vožnje
   */
  public void setBroj(int broj) {
    this.broj = broj;
  }

  /**
   * Dohvati vrijeme vožnje.
   * 
   * @return vrijeme vrijeme vožnje
   */
  public long getVrijeme() {
    return vrijeme;
  }

  /**
   * Postavi vrijeme vožnje.
   * 
   * @param vrijeme vrijeme vožnje
   */
  public void setVrijeme(long vrijeme) {
    this.vrijeme = vrijeme;
  }

  /**
   * Dohvati brzinu vožnje.
   * 
   * @return brzina brzina vožnje
   */
  public double getBrzina() {
    return brzina;
  }

  /**
   * Postavi brzinu vožnje.
   * 
   * @param brzina brzina vožnje
   */
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  /**
   * Dohvati snagu e-vozila.
   * 
   * @return snaga snaga e-vozila
   */
  public double getSnaga() {
    return snaga;
  }

  /**
   * Postavi snagu e-vozila.
   * 
   * @param snaga snaga e-vozila
   */
  public void setSnaga(double snaga) {
    this.snaga = snaga;
  }

  /**
   * Dohvati struju e-vozila.
   * 
   * @return struja struja e-vozila
   */
  public double getStruja() {
    return struja;
  }

  /**
   * Postavi struju e-vozila.
   * 
   * @param struja struja e-vozila
   */
  public void setStruja(double struja) {
    this.struja = struja;
  }

  /**
   * Dohvati visinu e-vozila.
   * 
   * @return visina visina e-vozila
   */
  public double getVisina() {
    return visina;
  }

  /**
   * Postavi visinu e-vozila.
   * 
   * @param visina visina e-vozila
   */
  public void setVisina(double visina) {
    this.visina = visina;
  }

  /**
   * Dohvati GPS brzinu e-vozila.
   * 
   * @return gpsBrzina GPS brzina e-vozila
   */
  public double getGpsBrzina() {
    return gpsBrzina;
  }

  /**
   * Postavi GPS brzinu e-vozila.
   * 
   * @param gpsBrzina GPS brzina e-vozila
   */
  public void setGpsBrzina(double gpsBrzina) {
    this.gpsBrzina = gpsBrzina;
  }

  /**
   * Dohvati temperaturu vozila.
   * 
   * @return tempVozila temperatura vozila
   */
  public int getTempVozila() {
    return tempVozila;
  }

  /**
   * Postavi temperaturu vozila.
   * 
   * @param tempVozila temperatura vozila
   */
  public void setTempVozila(int tempVozila) {
    this.tempVozila = tempVozila;
  }

  /**
   * Dohvati postotak baterije.
   * 
   * @return postotakBaterija postotak baterije
   */
  public int getPostotakBaterija() {
    return postotakBaterija;
  }

  /**
   * Postavi postotak baterije.
   * 
   * @param postotakBaterija postotak baterije
   */
  public void setPostotakBaterija(int postotakBaterija) {
    this.postotakBaterija = postotakBaterija;
  }

  /**
   * Dohvati napon baterije.
   * 
   * @return naponBaterija napon baterije
   */
  public double getNaponBaterija() {
    return naponBaterija;
  }

  /**
   * Postavi napon baterije.
   * 
   * @param naponBaterija napon baterije
   */
  public void setNaponBaterija(double naponBaterija) {
    this.naponBaterija = naponBaterija;
  }

  /**
   * Dohvati kapacitet baterije.
   * 
   * @return kapacitetBaterija kapacitet baterije
   */
  public int getKapacitetBaterija() {
    return kapacitetBaterija;
  }

  /**
   * Postavi kapacitet baterije.
   * 
   * @param kapacitetBaterija kapacitet baterije
   */
  public void setKapacitetBaterija(int kapacitetBaterija) {
    this.kapacitetBaterija = kapacitetBaterija;
  }

  /**
   * Dohvati temperaturu baterije.
   * 
   * @return tempBaterija temperatura baterije
   */
  public int getTempBaterija() {
    return tempBaterija;
  }

  /**
   * Postavi temperaturu baterije.
   * 
   * @param tempBaterija temperatura baterije
   */
  public void setTempBaterija(int tempBaterija) {
    this.tempBaterija = tempBaterija;
  }

  /**
   * Dohvati preostale kilometre.
   * 
   * @return preostaloKm preostali kilometri
   */
  public double getPreostaloKm() {
    return preostaloKm;
  }

  /**
   * Postavi preostale kilometre.
   * 
   * @param preostaloKm preostali kilometri
   */
  public void setPreostaloKm(double preostaloKm) {
    this.preostaloKm = preostaloKm;
  }

  /**
   * Dohvati ukupne kilometre.
   * 
   * @return ukupnoKm ukupni kilometri
   */
  public double getUkupnoKm() {
    return ukupnoKm;
  }

  /**
   * Postavi ukupne kilometre.
   * 
   * @param ukupnoKm ukupni kilometri
   */
  public void setUkupnoKm(double ukupnoKm) {
    this.ukupnoKm = ukupnoKm;
  }

  /**
   * Dohvati GPS širinu.
   * 
   * @return gpsSirina GPS širina
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Postavi GPS širinu.
   * 
   * @param gpsSirina GPS širina
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Dohvati GPS dužinu.
   * 
   * @return gpsDuzina GPS dužina
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Postavi GPS dužinu.
   * 
   * @param gpsDuzina GPS dužina
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }

}
