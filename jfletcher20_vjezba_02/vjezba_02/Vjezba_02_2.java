package edu.unizg.foi.nwtis.jfletcher20.vjezba_02;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Razred Vjezba_02_2.
 */
public class Vjezba_02_2 {

  /**
   * Stvara jedinku vjezba 02 2.
   */
  public Vjezba_02_2() {}

  /**
   * Glavni postupak.
   *
   * @param args razdvojbe
   */
  public static void main(String[] args) {
    if (args.length < 2 || args.length > 4) {
      System.out.println("Broj argumenata nije u raspornu 1 - 4.");
      return;
    }
    String komanda = args[0];
    switch (komanda) {
      case "L":
        try {
          Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(args[1]);
          Properties postavke = konfig.dajSvePostavke();
          System.out.println("Sve postavke");
          for (Object o : postavke.keySet()) {
            String k = (String) o;
            String v = postavke.getProperty(k);
            System.out.println("k: " + k + " v: " + v);
          }
          System.out.println();
        } catch (NeispravnaKonfiguracija ex) {
          Logger.getLogger(Vjezba_02_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;

      case "P":
        try {
          Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[1]);
          String k = args[2];
          String v = konfig.dajPostavku(k);
          System.out.println("k: " + k + " v: " + v);
        } catch (NeispravnaKonfiguracija ex) {
          Logger.getLogger(Vjezba_02_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;

      case "S":
        try {

          Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[1]);
          String k = args[2];
          String v = args[3];
          konfig.spremiPostavku(k, v);
          konfig.spremiKonfiguraciju();
        } catch (NeispravnaKonfiguracija ex) {
          Logger.getLogger(Vjezba_02_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;

      case "K":
        try {
          Konfiguracija konfigU = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[1]);
          Konfiguracija konfigI = KonfiguracijaApstraktna.kreirajKonfiguraciju(args[2]);
          konfigU.dajSvePostavke().keySet().stream()
              .forEach(k -> konfigI.spremiPostavku((String) k, konfigU.dajPostavku((String) k)));
          konfigI.spremiKonfiguraciju();
        } catch (NeispravnaKonfiguracija ex) {
          Logger.getLogger(Vjezba_02_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;
    }
  }

}
