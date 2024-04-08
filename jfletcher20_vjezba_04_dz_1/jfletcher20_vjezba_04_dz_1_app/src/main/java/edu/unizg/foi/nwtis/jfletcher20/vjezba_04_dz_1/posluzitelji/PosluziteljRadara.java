package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici.RadnikZaRadare;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

public class PosluziteljRadara {

  private ThreadFactory tf = Thread.ofVirtual().factory();
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  private int mreznaVrata;
  private Pattern predlozakKazna = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  private Matcher poklapanjeKazna;
  private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();
  private PodaciRadara podaciRadara;

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

  private int _pi(String arg) {
    return Integer.parseInt(arg);
  }

  public void preuzmiPostavke(String[] args)
      throws unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija, NumberFormatException,
      UnknownHostException {
    unizg.foi.nwtis.konfiguracije.Konfiguracija konfig =
        unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.podaciRadara = new PodaciRadara(_pi(konfig.dajPostavku("id")),
        InetAddress.getLocalHost().getHostName(), _pi(konfig.dajPostavku("mreznaVrataRadara")),
        _pi(konfig.dajPostavku("maksBrzina")), _pi(konfig.dajPostavku("maksTrajanje")),
        _pi(konfig.dajPostavku("maksUdaljenost")), konfig.dajPostavku("adresaRegistracije"),
        _pi(konfig.dajPostavku("mreznaVrataRegistracije")), konfig.dajPostavku("adresaKazne"),
        _pi(konfig.dajPostavku("mreznaVrataKazne")), konfig.dajPostavku("postanskaAdresaRadara"),
        Double.parseDouble(konfig.dajPostavku("gpsSirina")),
        Double.parseDouble(konfig.dajPostavku("gpsDuzina")));

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }

  private boolean registrirajPosluzitelja() {
    var s = new StringBuilder();
    s.append("RADAR").append(" ").append(this.podaciRadara.id()).append(" ")
        .append(this.podaciRadara.adresaRadara()).append(" ")
        .append(this.podaciRadara.mreznaVrataRadara()).append(" ")
        .append(this.podaciRadara.gpsSirina()).append(" ").append(this.podaciRadara.gpsDuzina())
        .append(" ").append(this.podaciRadara.maksUdaljenost());
    return MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), s.toString()) != null;
  }

  public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja =
        new ServerSocket(this.podaciRadara.mreznaVrataRadara())) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        RadnikZaRadare rr = new RadnikZaRadare(mreznaUticnica, podaciRadara, this);
        var dretva = tf.newThread(rr);
        dretva.start();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaKazna(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 10 Neispravna sintaksa komande.";
  }

  public String obradaZahtjevaKazna(String zahtjev) {
    this.poklapanjeKazna = this.predlozakKazna.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var kazna = new edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciKazne(
          Integer.valueOf(this.poklapanjeKazna.group("id")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
          Double.valueOf(this.poklapanjeKazna.group("brzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

      this.sveKazne.add(kazna);
      System.out.println("Id: " + kazna.id() + " Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
          + "  Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + " Brzina: " + kazna.brzina()
          + " GPS: " + kazna.gpsSirina() + ", " + kazna.gpsDuzina());

      return "OK";
    }
    return null;
  }
}
