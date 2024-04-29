package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

public class CentralniSustav {

  /**
   * Mrežna vrata radara.
   */
  public int mreznaVrataRadara;

  /**
   * Mrežna vrata vozila.
   */
  public int mreznaVrataVozila;

  /**
   * Mrežna vrata nadzora.
   */
  private int mreznaVrataNadzora;

  /**
   * Maksimalan broj vozila.
   */
  private int maksVozila;

  /**
   * Tvornica dretvi.
   */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /**
   * Svi radari.
   */
  public ConcurrentHashMap<Integer, PodaciRadara> sviRadari =
      new ConcurrentHashMap<Integer, PodaciRadara>();

  /**
   * Sva vozila.
   */
  public ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();

  /**
   * Main metoda.
   * 
   * @param args argumenti
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }
    CentralniSustav centralniSustav = new CentralniSustav();
    try {
      centralniSustav.preuzmiPostavke(args);
      centralniSustav.pokreniPosluzitelje();
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreće poslužitelje.
   */
  public void pokreniPosluzitelje() {
    var srr = new PosluziteljZaRegistracijuRadara(mreznaVrataRadara, this);
    var sv = new PosluziteljZaVozila(mreznaVrataVozila, this);
    var dretva1 = tvornicaDretvi.newThread(srr);
    var dretva2 = tvornicaDretvi.newThread(sv);
    dretva1.start();
    dretva2.start();
    try {
      dretva1.join();
      dretva2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  /**
   * Preuzima postavke iz konfiguracije.
   * 
   * @param args argumenti komandne linije
   * @throws NeispravnaKonfiguracija baca se ako konfiguracija nije ispravna
   * @throws NumberFormatException baca se ako broj nije ispravan
   * @throws UnknownHostException baca se ako host nije poznat
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    mreznaVrataRadara = Parsiraj.pi(konfig.dajPostavku("mreznaVrataRadara"));
    mreznaVrataVozila = Parsiraj.pi(konfig.dajPostavku("mreznaVrataVozila"));
    mreznaVrataNadzora = Parsiraj.pi(konfig.dajPostavku("mreznaVrataNadzora"));
    maksVozila = Parsiraj.pi(konfig.dajPostavku("maksVozila"));
  }

}
