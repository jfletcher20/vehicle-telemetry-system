package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.radnici;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.CentralniSustav;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.rest.RestKlijentVozila;

/*
 * PosluziteljZaVozila ima dodatne komande: ● VOZILO START id o npr.VOZILO START 1 o Provjera da li
 * ispravni podaci. Ako su ispravni, provjerava postoji li e-vozilo s id u kolekciji e-vozila čiji
 * se podaci o vožnji šalju na RESTful web servis za praćenje odabranih e-vozila. Ako ne postoji,
 * dodaje e-vozilo s id u kolekciju e-vozila čiji se podaci o vožnji šalju na RESTful webservis za
 * praćenje odabranih e-vozila. Vraća OK. o Npr.OK ● VOZILO STOP id o npr.VOZILO STOP 1 o Provjera
 * da li ispravni podaci. Ako su ispravni, provjerava postoji li e-vozilo s id u kolekciji e-vozila
 * čiji se podaci o vožnji šalju na RESTful web servis za praćenje odabranih e-vozila. Ako postoji,
 * briše e-vozilo s id iz kolekcije e-vozila čiji se podaci o vožnji šalju na RESTful webservis za
 * praćenje odabranih e-vozila. Vraća OK. o Npr.OK
 */

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
  private Pattern predlozakVozilo = Pattern.compile("^VOZILO (?<id>-?\\d+) " //
      + "(?<broj>-?\\d+) " //
      + "(?<vrijeme>-?\\d+) " //
      + "(?<brzina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<snaga>-?\\d+(?:\\.\\d+)?) " //
      + "(?<struja>-?\\d+(?:\\.\\d+)?) " //
      + "(?<visina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsBrzina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<tempVozila>-?\\d+) " //
      + "(?<postotakBaterija>-?\\d+(?:\\.\\d+)?) " //
      + "(?<naponBaterija>-?\\d+(?:\\.\\d+)?) " //
      + "(?<kapacitetBaterija>-?\\d+(?:\\.\\d+)?) " //
      + "(?<tempBaterija>-?\\d+(?:\\.\\d+)?) " //
      + "(?<preostaloKm>-?\\d+(?:\\.\\d+)?) " //
      + "(?<ukupnoKm>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsSirina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsDuzina>-?\\d+(?:\\.\\d+)?)$");
  /**
   * Predložak za naredbu vozilo start.
   */
  private Pattern predlozakVoziloStart = Pattern.compile("^VOZILO START (?<id>-?\\d+)$");
  /**
   * Predložak za naredbu vozilo stop.
   */
  private Pattern predlozakVoziloStop = Pattern.compile("^VOZILO STOP (?<id>-?\\d+)$");
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
            System.out.println("Primljeni podaci: " + r);
            if (r.length() > 0)
              obradaZahtjeva(r);
            bb.clear();
            kanalKlijenta.close();
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
    System.out.println("Primljen je zahtjev! Zahtjev je " + zahtjev);
    if (zahtjev == null || zahtjev.length() == 0)
      return "ERROR 20 Neispravna sintaksa komande " + zahtjev + "\n";
    zahtjev = zahtjev.trim();
    var odgovor = "";
    poklapanjeVozila = predlozakVoziloStart.matcher(zahtjev);
    if (poklapanjeVozila.matches()) {
      odgovor = obradaVozilaStart(zahtjev);
    } else if ((poklapanjeVozila = predlozakVoziloStop.matcher(zahtjev)).matches()) {
      odgovor = obradaVozilaStop(zahtjev);
    } else {
      odgovor = obradaZahtjevaVozila(zahtjev);
    }
    return odgovor != null ? odgovor
        : "ERROR 29 Nije moguće obraditi zahtjev " + zahtjev.length() + ": " + zahtjev + "\n";
  }

  /**
   * Metoda za obradu zahtjeva vozila start.
   * 
   * @param zahtjev Zahtjev za obradu
   * @return Odgovor na zahtjev
   */
  public String obradaVozilaStart(String zahtjev) {
    try {
      poklapanjeVozila = predlozakVoziloStart.matcher(zahtjev);
      if (poklapanjeVozila.matches()) {
        System.out.println("Vozilo start");
        var id = Parsiraj.i(poklapanjeVozila.group("id"));
        cs.svaVozila.putIfAbsent(id, new RedPodaciVozila(id));
        return "OK\n";
      } else
        return null;
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR 29 Nije moguće obraditi zahtjev " + zahtjev.length() + ": " + zahtjev + "\n";
    }
  }

  /**
   * Metoda za obradu zahtjeva vozila stop.
   * 
   * @param zahtjev Zahtjev za obradu
   * @return Odgovor na zahtjev
   */
  public String obradaVozilaStop(String zahtjev) {
    try {
      poklapanjeVozila = predlozakVoziloStop.matcher(zahtjev);
      System.out.println("Vozilo stop probanje");
      if (poklapanjeVozila.matches()) {
        var id = Parsiraj.i(poklapanjeVozila.group("id"));
        cs.svaVozila.remove(id);
        return "OK\n";
      } else
        return null;
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR 29 Nije moguće obraditi zahtjev " + zahtjev.length() + ": " + zahtjev + "\n";
    }
  }

  /**
   * Metoda za obradu zahtjeva vozila.
   * 
   * @param zahtjev Zahtjev za obradu
   * @return Odgovor na zahtjev
   */
  public String obradaZahtjevaVozila(String zahtjev) {
    try {
      if ((poklapanjeVozila = predlozakVozilo.matcher(zahtjev)).matches()) {
        PodaciVozila vozilo = new PodaciVozila(Parsiraj.i(poklapanjeVozila.group("id")),
            Parsiraj.i(poklapanjeVozila.group("broj")),
            Parsiraj.l(poklapanjeVozila.group("vrijeme")),
            Parsiraj.d(poklapanjeVozila.group("brzina")),
            Parsiraj.d(poklapanjeVozila.group("snaga")),
            Parsiraj.d(poklapanjeVozila.group("struja")),
            Parsiraj.d(poklapanjeVozila.group("visina")),
            Parsiraj.d(poklapanjeVozila.group("gpsBrzina")),
            Parsiraj.i(poklapanjeVozila.group("tempVozila")),
            Parsiraj.i(poklapanjeVozila.group("postotakBaterija")),
            Parsiraj.d(poklapanjeVozila.group("naponBaterija")),
            Parsiraj.i(poklapanjeVozila.group("kapacitetBaterija")),
            Parsiraj.i(poklapanjeVozila.group("tempBaterija")),
            Parsiraj.d(poklapanjeVozila.group("preostaloKm")),
            Parsiraj.d(poklapanjeVozila.group("ukupnoKm")),
            Parsiraj.d(poklapanjeVozila.group("gpsSirina")),
            Parsiraj.d(poklapanjeVozila.group("gpsDuzina")));
        if (cs.svaVozila.containsKey(vozilo.id())) {
          cs.svaVozila.get(vozilo.id()).dodajPodatakVozila(vozilo);
          var rs = new RestKlijentVozila();
          if(!rs.postVoznjaJSON(vozilo)) return "ERROR 21 POST nije uspješan.\n";
        }
        for (PodaciRadara r : cs.sviRadari.values())
          provjeriVoziloUOkoliniRadara(vozilo, r);
        return "OK\n";
      }
      return null;
    } catch (Exception e) {
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
