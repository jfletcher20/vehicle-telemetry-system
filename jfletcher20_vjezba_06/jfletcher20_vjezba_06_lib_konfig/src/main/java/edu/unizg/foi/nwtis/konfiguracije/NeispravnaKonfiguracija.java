package edu.unizg.foi.nwtis.konfiguracije;

/**
 * Iznimka za slučaj kada je neispravna konfiguracija s postavkama
 */
public class NeispravnaKonfiguracija extends Exception {

  /**
   * Serijski id verzije
   */
  private static final long serialVersionUID = 8075964301691709607L;

  /**
   * Kreira instancu <code>NeispravnaKonfiguracija</code> bez detalja poruke.
   */
  public NeispravnaKonfiguracija() {}

  /**
   * Kreira instancu <code>NeispravnaKonfiguracija</code> s pridruženim tekstom poruke.
   *
   * @param msg razloga nastanka iznimke
   */
  public NeispravnaKonfiguracija(String msg) {
    super(msg);
  }
}
