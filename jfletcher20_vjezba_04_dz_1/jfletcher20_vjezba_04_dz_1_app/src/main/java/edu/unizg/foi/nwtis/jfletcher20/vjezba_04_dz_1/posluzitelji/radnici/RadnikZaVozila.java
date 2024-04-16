package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.CentralniSustav;

public class RadnikZaVozila implements Runnable {

  private AsynchronousSocketChannel kanalKlijenta;
  private CentralniSustav cs;

  private Pattern predlozakVozilo = Pattern.compile( //
      "^VOZILO " // VOZILO
          + "(?<id>\\d+) " // int id vozila
          + "(?<broj>\\d+) " // int broj
          + "(?<vrijeme>\\d+) " // long vrijeme
          + "(?<brzina>\\d+(?:\\.\\d+)?) " // double brzina
          + "(?<snaga>\\d+(?:\\.\\d+)?) " // double snaga
          + "(?<struja>\\d+(?:\\.\\d+)?) " // double struja
          + "(?<visina>\\d+(?:\\.\\d+)?) " // double visina
          + "(?<gpsBrzina>\\d+(?:\\.\\d+)?) " // double gpsBrzina
          + "(?<tempVozila>\\d+) " // int tempVozila
          + "(?<postotakBaterija>\\d+(?:\\.\\d+)?) " // double postotakBaterija
          + "(?<naponBaterija>\\d+(?:\\.\\d+)?) " // double naponBaterija
          + "(?<kapacitetBaterija>\\d+(?:\\.\\d+)?) " // double kapacitetBaterija
          + "(?<tempBaterija>\\d+(?:\\.\\d+)?) " // double tempBaterija
          + "(?<preostaloKm>\\d+(?:\\.\\d+)?) " // double preostaloKm
          + "(?<ukupnoKm>\\d+(?:\\.\\d+)?) " // double ukupnoKm
          + "(?<gpsSirina>\\d+(?:\\.\\d+)?) " // double gpsSirina
          + "(?<gpsDuzina>\\d+(?:\\.\\d+)?)$"); // double gpsDuzina

  private Matcher poklapanjeVozila;

  public RadnikZaVozila(AsynchronousSocketChannel klijent, CentralniSustav cs) {
    this.kanalKlijenta = klijent;
    this.cs = cs;
  }

  private Pattern predlozakBrzine = Pattern.compile( //
      "^VOZILO " // VOZILO
          + "(?<id>\\d+)" // int id vozila
          + "(?<vrijeme>\\d+)" // long vrijeme
          + "(?<brzina>-?\\d+([.]\\d+)?) " // double brzina
          + "(?<gpsSirina>\\d+[.]\\d+) " // double gpsSirina
          + "(?<gpsDuzina>\\d+[.]\\d+)$"); // double gpsDuzina

  @Override
  public void run() {
    try {
      try {
        while (true) {
          if (kanalKlijenta != null && kanalKlijenta.isOpen()) {
            ByteBuffer bb = ByteBuffer.allocate(2048);
            var citac = kanalKlijenta.read(bb);
            citac.get();
            String r = new String(bb.array()).trim();
            var odgovor = obradaZahtjeva(r);
            if (!odgovor.contains("OK")) {
              bb.clear();
              bb.put(odgovor.getBytes()); // TODO: wtf is even happening here?
              bb.flip();
              kanalKlijenta.write(bb);
            }
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

  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null)
      return "ERROR 20 Neispravna sintaksa komande.\n";
    var odgovor = obradaZahtjevaVozila(zahtjev);
    return odgovor != null ? odgovor : "ERROR 29 Nije moguće obraditi zahtjev.\n";
  }

  private Double _d(String value) {
    return Double.valueOf(value);
  }

  private Long _l(String value) {
    return Long.valueOf(value);
  }

  private Integer _i(String value) {
    return Integer.valueOf(value);
  }

  public String obradaZahtjevaVozila(String zahtjev) {
    poklapanjeVozila = predlozakVozilo.matcher(zahtjev);
    if (poklapanjeVozila.matches()) {
      var vozilo = new PodaciVozila(_i(poklapanjeVozila.group("id")), //
          _i(poklapanjeVozila.group("broj")), //
          _l(poklapanjeVozila.group("vrijeme")), //
          _d(poklapanjeVozila.group("brzina")), //
          _d(poklapanjeVozila.group("snaga")), //
          _d(poklapanjeVozila.group("struja")), //
          _d(poklapanjeVozila.group("visina")), //
          _d(poklapanjeVozila.group("gpsBrzina")), //
          _i(poklapanjeVozila.group("tempVozila")), //
          _i(poklapanjeVozila.group("postotakBaterija")), //
          _d(poklapanjeVozila.group("naponBaterija")), //
          _i(poklapanjeVozila.group("kapacitetBaterija")), //
          _i(poklapanjeVozila.group("tempBaterija")), //
          _d(poklapanjeVozila.group("preostaloKm")), //
          _d(poklapanjeVozila.group("ukupnoKm")), //
          _d(poklapanjeVozila.group("gpsSirina")), //
          _d(poklapanjeVozila.group("gpsDuzina")));
      for (PodaciRadara r : cs.sviRadari.values()) {
        if (r.jeUnutarDosega(vozilo)) {
          String cmd = "VOZILO " + vozilo.id() + " " + vozilo.vrijeme() + " " + vozilo.brzina()
              + " " + vozilo.gpsSirina() + " " + vozilo.gpsDuzina() + "\n";
          MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaRadara(), r.mreznaVrataRadara(), cmd);
        }
      }
      return "OK\n";
    }
    return null;
  }

}
