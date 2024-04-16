package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.RedPodaciVozila;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

public class CentralniSustav {

  public int mreznaVrataRadara;
  public int mreznaVrataVozila;
  private int mreznaVrataNadzora;
  private int maksVozila;
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();
  public ConcurrentHashMap<Integer, PodaciRadara> sviRadari =
      new ConcurrentHashMap<Integer, PodaciRadara>();
  public ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();

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

  private Integer _i(String value) {
    return Integer.valueOf(value);
  }

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

  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    mreznaVrataRadara = _i(konfig.dajPostavku("mreznaVrataRadara"));
    mreznaVrataVozila = _i(konfig.dajPostavku("mreznaVrataVozila"));
    mreznaVrataNadzora = _i(konfig.dajPostavku("mreznaVrataNadzora"));
    maksVozila = _i(konfig.dajPostavku("maksVozila"));
  }

}
