package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.radnici;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.CentralniSustav;

/**
 * Radnik za vozila.
 */
public class RadnikZaVozila implements Runnable {

  /**
   * Uticnica klijenta.
   */
  private AsynchronousSocketChannel kanalKlijenta;
  /**
   * Referenca na centralni sustav.
   */
  private CentralniSustav cs;

  /**
   * Predložak za naredbu s podacima o vozilu.
   */
  private Pattern predlozakVozilo = Pattern.compile(
      "^VOZILO (?<id>-?\\d+) (?<broj>-?\\d+) (?<vrijeme>-?\\d+) (?<brzina>-?\\d+(?:\\.\\d+)?) (?<snaga>-?\\d+(?:\\.\\d+)?) (?<struja>-?\\d+(?:\\.\\d+)?) (?<visina>-?\\d+(?:\\.\\d+)?) (?<gpsBrzina>-?\\d+(?:\\.\\d+)?) (?<tempVozila>-?\\d+) (?<postotakBaterija>-?\\d+(?:\\.\\d+)?) (?<naponBaterija>-?\\d+(?:\\.\\d+)?) (?<kapacitetBaterija>-?\\d+(?:\\.\\d+)?) (?<tempBaterija>-?\\d+(?:\\.\\d+)?) (?<preostaloKm>-?\\d+(?:\\.\\d+)?) (?<ukupnoKm>-?\\d+(?:\\.\\d+)?) (?<gpsSirina>-?\\d+(?:\\.\\d+)?) (?<gpsDuzina>-?\\d+(?:\\.\\d+)?)$");

  /**
   * Matcher za provjeru poklapanja naredbi vozila.
   */
  private Matcher poklapanjeVozila;

  /**
   * Konstruktor za radnika vozila.
   * 
   * @param klijent utičnica klijenta
   * @param cs referenca na centralni sustav
   */
  public RadnikZaVozila(AsynchronousSocketChannel klijent, CentralniSustav cs) {
    this.kanalKlijenta = klijent;
    this.cs = cs;
  }

  /**
   * Metoda koja pokreće radnika.
   */
  @Override
  public void run() {
    try {
      try {
        while (true) {
          if (kanalKlijenta != null && kanalKlijenta.isOpen()) {
            ByteBuffer bb = ByteBuffer.allocate(2048);
            Future<Integer> citac = kanalKlijenta.read(bb);
            citac.get();
            String r = new String(bb.array()).trim();
            if (r.length() > 0)
              obradaZahtjeva(r);
            bb.clear();
          } else
            break;
        }
      } finally {
        kanalKlijenta.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda za obradu zahtjeva.
   * 
   * @param zahtjev Zahtjev za obradu
   * @return Odgovor na zahtjev
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null || zahtjev.length() == 0)
      return "ERROR 20 Neispravna sintaksa komande " + zahtjev + "\n";
    zahtjev = zahtjev.trim();
    var odgovor = obradaZahtjevaVozila(zahtjev);
    return odgovor != null ? odgovor
        : "ERROR 29 Nije moguće obraditi zahtjev " + zahtjev.length() + ": " + zahtjev + "\n";
  }

  /**
   * Metoda za obradu zahtjeva vozila.
   * 
   * @param zahtjev Zahtjev za obradu
   * @return Odgovor na zahtjev
   */
  public String obradaZahtjevaVozila(String zahtjev) {
    try {
      poklapanjeVozila = predlozakVozilo.matcher(zahtjev);
      if (poklapanjeVozila.matches()) {
        var vozilo = new PodaciVozila(Parsiraj.pi(poklapanjeVozila.group("id")),
            Parsiraj.pi(poklapanjeVozila.group("broj")),
            Parsiraj.pl(poklapanjeVozila.group("vrijeme")),
            Parsiraj.pd(poklapanjeVozila.group("brzina")),
            Parsiraj.pd(poklapanjeVozila.group("snaga")),
            Parsiraj.pd(poklapanjeVozila.group("struja")),
            Parsiraj.pd(poklapanjeVozila.group("visina")),
            Parsiraj.pd(poklapanjeVozila.group("gpsBrzina")),
            Parsiraj.pi(poklapanjeVozila.group("tempVozila")),
            Parsiraj.pi(poklapanjeVozila.group("postotakBaterija")),
            Parsiraj.pd(poklapanjeVozila.group("naponBaterija")),
            Parsiraj.pi(poklapanjeVozila.group("kapacitetBaterija")),
            Parsiraj.pi(poklapanjeVozila.group("tempBaterija")),
            Parsiraj.pd(poklapanjeVozila.group("preostaloKm")),
            Parsiraj.pd(poklapanjeVozila.group("ukupnoKm")),
            Parsiraj.pd(poklapanjeVozila.group("gpsSirina")),
            Parsiraj.pd(poklapanjeVozila.group("gpsDuzina")));
        for (PodaciRadara r : cs.sviRadari.values())
          provjeriVoziloUOkoliniRadara(vozilo, r);
        return "OK\n";
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR 29 Nije moguće obraditi zahtjev " + zahtjev.length() + ": " + zahtjev + "\n";
    }
  }

  /**
   * Metoda za provjeru vozila u okolini radara.
   * 
   * @param vozilo Vozilo za provjeru
   * @param r Radar za provjeru
   */
  private void provjeriVoziloUOkoliniRadara(PodaciVozila vozilo, PodaciRadara r) {
    if (r.jeUnutarDosega(vozilo)) {
      String cmd = "VOZILO " + vozilo.id() + " " + vozilo.vrijeme() + " " + vozilo.brzina() + " "
          + vozilo.gpsSirina() + " " + vozilo.gpsDuzina() + "\n";
      MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaRadara(), r.mreznaVrataRadara(), cmd);
    }
  }

}
