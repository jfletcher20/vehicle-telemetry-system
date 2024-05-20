package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.pomocnici;

/**
 * Klasa za parsiranje brojeva.
 */
public class Parsiraj {

  /**
   * Parsira broj iz stringa.
   * 
   * @param s string
   * @return cijeli broj
   */
  public static int pi(String s) {
    return Integer.parseInt(s);
  }

  /**
   * Parsira broj iz stringa.
   * 
   * @param s string
   * @return decimalni broj
   */
  public static double pd(String s) {
    return Double.parseDouble(s);
  }

  /**
   * Parsira broj iz stringa.
   * 
   * @param s string
   * @return cijeli long broj
   */
  public static long pl(String s) {
    return Long.parseLong(s);
  }

}
