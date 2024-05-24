package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klijent za slanje zahtjeva posluzitelju kazni.
 */
public class Klijent {

  /**
   * ID vozila.
   */
  private int id = -1;
  /**
   * Vrijeme od.
   */
  private long vrijemeOd;
  /**
   * Vrijeme do.
   */
  private long vrijemeDo;

  /**
   * Adresa posluzitelja za kazne.
   */
  private String adresaKazne;
  /**
   * Mrezna vrata posluzitelja za kazne.
   */
  private int mreznaVrataKazne;

  /**
   * Metoda za pokretanje klijenta.
   * 
   * @param args argumenti komandne linije
   */
  public static void main(String[] args) {
    if (args.length < 3 || args.length > 4) {
      System.out.println("Broj argumenata nije 3 ili 4.");
      return;
    }
    Klijent klijent = new Klijent();
    try {
      klijent.preuzmiPostavke(args);
      klijent.posaljiZahtjev();
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Preuzima postavke iz konfiguracije i argumenta komandne linije.
   * 
   * @param args argumenti komandne linije
   * @throws NeispravnaKonfiguracija baca se ako konfiguracija nije ispravna
   * @throws NumberFormatException baca se ako brojevi nisu ispravni
   * @throws UnknownHostException baca se ako adresa nije ispravna
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    if (args.length == 4) {
      id = Integer.parseInt(args[1]);
      vrijemeOd = Long.parseLong(args[2]);
      vrijemeDo = Long.parseLong(args[3]);
    } else {
      vrijemeOd = Long.parseLong(args[1]);
      vrijemeDo = Long.parseLong(args[2]);
    }
    adresaKazne = konfig.dajPostavku("adresaKazne");
    mreznaVrataKazne = Parsiraj.i(konfig.dajPostavku("mreznaVrataKazne"));
  }

  /**
   * Konstruira zahtjev za posluzitelj kazni.
   * 
   * @return Zahtjev za posluzitelj kazni
   */
  public String konstruirajZahtjev() {
    return id != -1 ? // ako je ID definiran vrati podatke o kaznama vozila
        "VOZILO " + id + " " + vrijemeOd + " " + vrijemeDo : // u protivnom vrati statistiku
        "STATISTIKA " + vrijemeOd + " " + vrijemeDo;
  }

  /**
   * Salje zahtjev posluzitelju kazni.
   * 
   * @return Odgovor posluzitelja kazni
   */
  public String posaljiZahtjev() {
    return MrezneOperacije.posaljiZahtjevPosluzitelju(adresaKazne, mreznaVrataKazne,
        konstruirajZahtjev());
  }

}
