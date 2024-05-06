package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Poslužitelj radara.
 */
public class PosluziteljRadara {

  /**
   * Dretva za radnike.
   */
  private ThreadFactory tf = Thread.ofVirtual().factory();
  /**
   * Format datuma.
   */
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  /**
   * Mrežna vrata.
   */
  private int mreznaVrata;
  /**
   * Red svih kazni.
   */
  public volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();
  /**
   * Red brzih vozila.
   */
  public volatile ConcurrentHashMap<Integer, BrzoVozilo> brzaVozila =
      new ConcurrentHashMap<Integer, BrzoVozilo>();
  /**
   * Podaci radara.
   */
  private PodaciRadara r;

  /**
   * Pokreće poslužitelj radara.
   * 
   * @param args argumenti naredbenog retka
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
    try {
      posluziteljRadara.preuzmiPostavke(args);
      posluziteljRadara.registrirajPosluzitelja();
      posluziteljRadara.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Pretvara string u integer.
   * 
   * @param value tekst
   * @return parsirani integer
   */
  private int _ip(String value) {
    return Integer.parseInt(value);
  }

  /**
   * Pretvara string u double.
   * 
   * @param value tekst
   * @return parsirani double
   */
  private double _dp(String value) {
    return Double.parseDouble(value);
  }

  /**
   * Dohvaca vrijednost integera iz stringa.
   * 
   * @param value tekst
   * @return parsirani integer
   */
  private Integer _i(String value) {
    return Integer.valueOf(value);
  }

  /**
   * Dohvaca vrijednosti postavki iz konfiguracije.
   * 
   * @param args argumenti naredbenog retka
   * @throws NeispravnaKonfiguracija baca se ako je konfiguracija neispravna
   * @throws NumberFormatException baca se ako je broj neispravan
   * @throws UnknownHostException baca se ako je adresa nepoznata
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    r = new PodaciRadara(_ip(konfig.dajPostavku("id")), InetAddress.getLocalHost().getHostName(),
        _ip(konfig.dajPostavku("mreznaVrataRadara")), _ip(konfig.dajPostavku("maksBrzina")),
        _ip(konfig.dajPostavku("maksTrajanje")), _ip(konfig.dajPostavku("maksUdaljenost")),
        konfig.dajPostavku("adresaRegistracije"),
        _ip(konfig.dajPostavku("mreznaVrataRegistracije")), konfig.dajPostavku("adresaKazne"),
        _ip(konfig.dajPostavku("mreznaVrataKazne")), konfig.dajPostavku("postanskaAdresaRadara"),
        _dp(konfig.dajPostavku("gpsSirina")), _dp(konfig.dajPostavku("gpsDuzina")));

    mreznaVrata = _i(konfig.dajPostavku("mreznaVrataKazne"));
  }

  /**
   * Registrira radara.
   * 
   * @return true ako je registracija uspješna, inače false
   */
  private boolean registrirajPosluzitelja() {
    var s = new StringBuilder();
    s.append("RADAR").append(" ").append(r.id()).append(" ").append(r.adresaRadara()).append(" ")
        .append(r.mreznaVrataRadara()).append(" ").append(r.gpsSirina()).append(" ")
        .append(r.gpsDuzina()).append(" ").append(r.maksUdaljenost()).append("\n");
    return MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaRegistracije(),
        r.mreznaVrataRegistracije(), s.toString()) != null;
  }

  /**
   * Pokreće poslužitelja.
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;
    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.r.mreznaVrataRadara())) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        RadnikZaRadare rr = new RadnikZaRadare(mreznaUticnica, r, this);
        var dretva = tf.newThread(rr);
        dretva.start();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

}
