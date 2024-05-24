package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici;

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
  public static int i(String s) {
    return Integer.parseInt(s);
  }

  /**
   * Parsira broj iz stringa.
   * 
   * @param s string
   * @return decimalni broj
   */
  public static double d(String s) {
    return Double.parseDouble(s);
  }

  /**
   * Parsira broj iz stringa.
   * 
   * @param s string
   * @return cijeli long broj
   */
  public static long l(String s) {
    return Long.parseLong(s);
  }

}
