package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.CentralniSustav;

public class RadnikZaVozila implements Runnable {

  private AsynchronousSocketChannel kanalKlijenta;
  private CentralniSustav cs;

  private Pattern predlozakVozilo = Pattern.compile(
      "^VOZILO (?<id>-?\\d+) (?<broj>-?\\d+) (?<vrijeme>-?\\d+) (?<brzina>-?\\d+(?:\\.\\d+)?) (?<snaga>-?\\d+(?:\\.\\d+)?) (?<struja>-?\\d+(?:\\.\\d+)?) (?<visina>-?\\d+(?:\\.\\d+)?) (?<gpsBrzina>-?\\d+(?:\\.\\d+)?) (?<tempVozila>-?\\d+) (?<postotakBaterija>-?\\d+(?:\\.\\d+)?) (?<naponBaterija>-?\\d+(?:\\.\\d+)?) (?<kapacitetBaterija>-?\\d+(?:\\.\\d+)?) (?<tempBaterija>-?\\d+(?:\\.\\d+)?) (?<preostaloKm>-?\\d+(?:\\.\\d+)?) (?<ukupnoKm>-?\\d+(?:\\.\\d+)?) (?<gpsSirina>-?\\d+(?:\\.\\d+)?) (?<gpsDuzina>-?\\d+(?:\\.\\d+)?)$");

  private Matcher poklapanjeVozila;

  public RadnikZaVozila(AsynchronousSocketChannel klijent, CentralniSustav cs) {
    this.kanalKlijenta = klijent;
    this.cs = cs;
  }

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
              /* var odgovor = */obradaZahtjeva(r);
            bb.clear();
            // bb.put(odgovor.getBytes()); // TODO: no response is needed from RadnikZaVozila
            // bb.flip();
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
    if (zahtjev == null || zahtjev.length() == 0)
      return "ERROR 20 Neispravna sintaksa komande " + zahtjev + "";
    zahtjev = zahtjev.trim();
    var odgovor = obradaZahtjevaVozila(zahtjev);
    return odgovor != null ? odgovor
        : "ERROR 29 Nije moguće obraditi zahtjev ( + " + zahtjev.length() + ": " + zahtjev + "";
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
    try {
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
          provjeriVoziloUOkoliniRadara(vozilo, r);
        }
        return "OK";
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR 29 Nije moguće obraditi zahtjev ( + " + zahtjev.length() + ": " + zahtjev + "";
    }
  }

  private void provjeriVoziloUOkoliniRadara(PodaciVozila vozilo, PodaciRadara r) {
    if (r.jeUnutarDosega(vozilo)) {
      String cmd = "VOZILO " + vozilo.id() + " " + vozilo.vrijeme() + " " + vozilo.brzina() + " "
          + vozilo.gpsSirina() + " " + vozilo.gpsDuzina() + "";
      MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaRadara(), r.mreznaVrataRadara(), cmd);
    }
  }

}
