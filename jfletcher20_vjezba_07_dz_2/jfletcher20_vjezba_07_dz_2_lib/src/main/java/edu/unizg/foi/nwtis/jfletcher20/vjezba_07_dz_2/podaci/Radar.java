package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;


/**
 * Zapis PodaciRadara.
 *
 * @param id identifikator radara
 * @param adresaRadara adresa poslužitelja radara (logička ili IP)
 * @param mreznaVrataRadara mrežna vrata poslužitelja radara
 * @param maksBrzina maksimalna dopuštena brzina
 * @param maksTrajanje maksimalno trajanje maksBrzina
 * @param maksUdaljenost maksimalna udaljenost do koje radar mjeri brzinu
 * @param adresaRegistracije adresa poslužitelja za registraciju radara
 * @param mreznaVrataRegistracije mrezna vrata poslužitelja za registraciju radara
 * @param adresaKazne adresa poslužitelja za kazne
 * @param mreznaVrataKazne mrezna vrata poslužitelja za kazne
 * @param postanskaAdresaRadara poštanska adresa radara
 * @param gpsSirina gps širina pozicije radara
 * @param gpsDuzina gps dužina pozicije radara
 */

public class Radar {
  
  private int id;
  private String adresaRadara;
  private int mreznaVrataRadara;
  private int maksBrzina;
  private int maksTrajanje;
  private int maksUdaljenost;
  private String adresaRegistracije;
  private int mreznaVrataRegistracije;
  private String adresaKazne;
  private int mreznaVrataKazne;
  private String postanskaAdresaRadara;
  private double gpsSirina;
  private double gpsDuzina;

  /**
   * Zapis PodaciRadara.
   *
   * @param id identifikator radara
   * @param adresaRadara adresa poslužitelja radara (logička ili IP)
   * @param mreznaVrataRadara mrežna vrata poslužitelja radara
   * @param maksBrzina maksimalna dopuštena brzina
   * @param maksTrajanje maksimalno trajanje maksBrzina
   * @param maksUdaljenost maksimalna udaljenost do koje radar mjeri brzinu
   * @param adresaRegistracije adresa poslužitelja za registraciju radara
   * @param mreznaVrataRegistracije mrezna vrata poslužitelja za registraciju radara
   * @param adresaKazne adresa poslužitelja za kazne
   * @param mreznaVrataKazne mrezna vrata poslužitelja za kazne
   * @param postanskaAdresaRadara poštanska adresa radara
   * @param gpsSirina gps širina pozicije radara
   * @param gpsDuzina gps dužina pozicije radara
   */
  public Radar(int id, String adresaRadara, int mreznaVrataRadara, int maksBrzina,
               int maksTrajanje, int maksUdaljenost, String adresaRegistracije, int mreznaVrataRegistracije,
               String adresaKazne, int mreznaVrataKazne, String postanskaAdresaRadara, double gpsSirina,
               double gpsDuzina) {
    this.id = id;
    this.adresaRadara = adresaRadara;
    this.mreznaVrataRadara = mreznaVrataRadara;
    this.maksBrzina = maksBrzina;
    this.maksTrajanje = maksTrajanje;
    this.maksUdaljenost = maksUdaljenost;
    this.adresaRegistracije = adresaRegistracije;
    this.mreznaVrataRegistracije = mreznaVrataRegistracije;
    this.adresaKazne = adresaKazne;
    this.mreznaVrataKazne = mreznaVrataKazne;
    this.postanskaAdresaRadara = postanskaAdresaRadara;
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
   * Dohvati adresu radara.
   * 
   * @return adresaRadara adresa poslužitelja radara
   */
  public String getAdresaRadara() {
    return adresaRadara;
  }

  /**
   * Postavi adresu radara.
   * 
   * @param adresaRadara adresa poslužitelja radara
   */
  public void setAdresaRadara(String adresaRadara) {
    this.adresaRadara = adresaRadara;
  }

  /**
   * Dohvati mrežna vrata radara.
   * 
   * @return mreznaVrataRadara mrežna vrata poslužitelja radara
   */
  public int getMreznaVrataRadara() {
    return mreznaVrataRadara;
  }

  /**
   * Postavi mrežna vrata radara.
   * 
   * @param mreznaVrataRadara mrežna vrata poslužitelja radara
   */
  public void setMreznaVrataRadara(int mreznaVrataRadara) {
    this.mreznaVrataRadara = mreznaVrataRadara;
  }

  /**
   * Dohvati maksimalnu dopuštenu brzinu.
   * 
   * @return maksBrzina maksimalna dopuštena brzina
   */
  public int getMaksBrzina() {
    return maksBrzina;
  }

  /**
   * Postavi maksimalnu dopuštenu brzinu.
   * 
   * @param maksBrzina maksimalna dopuštena brzina
   */
  public void setMaksBrzina(int maksBrzina) {
    this.maksBrzina = maksBrzina;
  }

  /**
   * Dohvati maksimalno trajanje.
   * 
   * @return maksTrajanje maksimalno trajanje maksBrzina
   */
  public int getMaksTrajanje() {
    return maksTrajanje;
  }

  /**
   * Postavi maksimalno trajanje.
   * 
   * @param maksTrajanje maksimalno trajanje maksBrzina
   */
  public void setMaksTrajanje(int maksTrajanje) {
    this.maksTrajanje = maksTrajanje;
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
   * Dohvati adresu za kazne.
   * 
   * @return adresaKazne adresa poslužitelja za kazne
   */
  public String getAdresaKazne() {
    return adresaKazne;
  }

  /**
   * Postavi adresu za kazne.
   * 
   * @param adresaKazne adresa poslužitelja za kazne
   */
  public void setAdresaKazne(String adresaKazne) {
    this.adresaKazne = adresaKazne;
  }

  /**
   * Dohvati mrežna vrata za kazne.
   * 
   * @return mreznaVrataKazne mrežna vrata poslužitelja za kazne
   */
  public int getMreznaVrataKazne() {
    return mreznaVrataKazne;
  }

  /**
   * Postavi mrežna vrata za kazne.
   * 
   * @param mreznaVrataKazne mrežna vrata poslužitelja za kazne
   */
  public void setMreznaVrataKazne(int mreznaVrataKazne) {
    this.mreznaVrataKazne = mreznaVrataKazne;
  }

  /**
   * Dohvati poštansku adresu radara.
   * 
   * @return postanskaAdresaRadara poštanska adresa radara
   */
  public String getPostanskaAdresaRadara() {
    return postanskaAdresaRadara;
  }

  /**
   * Postavi poštansku adresu radara.
   * 
   * @param postanskaAdresaRadara poštanska adresa radara
   */
  public void setPostanskaAdresaRadara(String postanskaAdresaRadara) {
    this.postanskaAdresaRadara = postanskaAdresaRadara;
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
