package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici.RadnikZaRadare;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

public class PosluziteljRadara {

  private ThreadFactory tf = Thread.ofVirtual().factory();
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  private int mreznaVrata;
  private Pattern predlozakKazna = Pattern.compile( //
      "^VOZILO " //
          + "(?<id>\\d+) " //
          + "(?<vrijemePocetak>\\d+) " //
          + "(?<vrijemeKraj>\\d+) " //
          + "(?<brzina>-?\\d+([.]\\d+)?) " //
          + "(?<gpsSirina>\\d+[.]\\d+) " //
          + "(?<gpsDuzina>\\d+[.]\\d+) " //
          + "(?<gpsSirinaRadar>\\d+[.]\\d+) " //
          + "(?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  private Matcher poklapanjeKazna;
  public volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();
  public volatile ConcurrentHashMap<Integer, BrzoVozilo> brzaVozila =
      new ConcurrentHashMap<Integer, BrzoVozilo>();
  private PodaciRadara r;

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
      System.out.println(e.getMessage());
      return;
    }
  }

  private int _ip(String value) {
    return Integer.parseInt(value);
  }

  private double _dp(String value) {
    return Double.parseDouble(value);
  }

  private Integer _i(String value) {
    return Integer.valueOf(value);
  }

  private Double _d(String value) {
    return Double.valueOf(value);
  }

  private Long _l(String value) {
    return Long.valueOf(value);
  }

  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    r = new PodaciRadara(_ip(konfig.dajPostavku("id")), //
        InetAddress.getLocalHost().getHostName(), //
        _ip(konfig.dajPostavku("mreznaVrataRadara")), //
        _ip(konfig.dajPostavku("maksBrzina")), //
        _ip(konfig.dajPostavku("maksTrajanje")), //
        _ip(konfig.dajPostavku("maksUdaljenost")), //
        konfig.dajPostavku("adresaRegistracije"), //
        _ip(konfig.dajPostavku("mreznaVrataRegistracije")), //
        konfig.dajPostavku("adresaKazne"), //
        _ip(konfig.dajPostavku("mreznaVrataKazne")), //
        konfig.dajPostavku("postanskaAdresaRadara"), //
        _dp(konfig.dajPostavku("gpsSirina")), //
        _dp(konfig.dajPostavku("gpsDuzina")));

    mreznaVrata = _i(konfig.dajPostavku("mreznaVrataKazne"));
  }

  private boolean registrirajPosluzitelja() {
    var s = new StringBuilder();
    s.append("RADAR").append(" ") //
        .append(r.id()).append(" ") //
        .append(r.adresaRadara()).append(" ") //
        .append(r.mreznaVrataRadara()).append(" ") //
        .append(r.gpsSirina()).append(" ") //
        .append(r.gpsDuzina()).append(" ") //
        .append(r.maksUdaljenost()).append("\n");
    return MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaRegistracije(),
        r.mreznaVrataRegistracije(), s.toString()) != null;
  }

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

  /**
   * Izračunava razliku u vremenu između podataka vozila i najnovijeg podatka o tom vozilu iz reda.
   * 
   * @param podaci podaci vozila za koju se računa razlika od prethodnog vremena
   * @return razlika u vremenu (pozitivna ako je vozilo najnovije, negativna ako nije)
   */
  public long vrijemeIzmeduPodataka(BrzoVozilo podaci) {
    var poh = brzaVozila.get(podaci.id());
    return Math.abs(podaci.vrijeme() - (poh == null ? podaci : poh).vrijeme());
  }

}
