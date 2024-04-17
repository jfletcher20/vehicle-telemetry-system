package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.PosluziteljRadara;

public class RadnikZaRadare implements Runnable {

  private Socket s;
  private PodaciRadara r;
  private PosluziteljRadara p;

  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      PosluziteljRadara posluziteljRadara) {
    super();
    this.s = mreznaUticnica;
    this.r = podaciRadara;
    this.p = posluziteljRadara;
  }

  private Pattern predlozakBrzine = Pattern.compile("^VOZILO " //
      + "(?<id>-?\\d+) " //
      + "(?<vrijeme>-?\\d+) " //
      + "(?<brzina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsSirina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsDuzina>-?\\d+(?:\\.\\d+)?)$"); //

  @Override
  public void run() {

    try {
      BufferedReader citac = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf8"));
      OutputStream out = s.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      var redak = citac.readLine();
      s.shutdownInput();
      var odgovor = obradaZahtjeva(redak);
      pisac.println(odgovor); // TODO: maybe remove this println? need to check if
                              // it's necessary
      System.out.println(odgovor);
      pisac.flush();
      s.shutdownOutput();
      s.close();
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }

  }

  private String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null)
      return "ERROR 30 Neispravna sintaksa naredbe.\n";
    var r = obradaZahtjevaBrzine(zahtjev);
    return r != null ? r : "ERROR 39 Nešto je pošlo po zlu.\n";
  }

  private boolean jeBrzaVoznja(BrzoVozilo vozilo) {
    return vozilo.brzina() > r.maksBrzina();
  }

  private String obradaZahtjevaBrzine(String zahtjev) {
    var podaciVozila = predlozakBrzine.matcher(zahtjev);
    if (!podaciVozila.matches())
      return null;
    var vozilo = new BrzoVozilo(podaciVozila.group("id"), -1, podaciVozila.group("vrijeme"),
        podaciVozila.group("brzina"), podaciVozila.group("gpsSirina"),
        podaciVozila.group("gpsDuzina"), false);
    // provjera udaljenosti
    var udaljenost = GpsUdaljenostBrzina.udaljenostKm(r.gpsSirina(), r.gpsDuzina(),
        vozilo.gpsSirina(), vozilo.gpsDuzina());
    if (GpsUdaljenostBrzina.udaljenostKm(r.gpsSirina(), r.gpsDuzina(), vozilo.gpsSirina(),
        vozilo.gpsDuzina()) * 1000 < r.maksUdaljenost()) {
      // provjera brzine i trajanja brze vožnje
      if (jeBrzaVoznja(vozilo)) {
        var prvi = p.brzaVozila.get(vozilo.id());
        String cmd = "VOZILO " + vozilo.id() + " " + (prvi == null ? vozilo : prvi).vrijeme() + " "
            + vozilo.vrijeme() + " " + vozilo.brzina() + " " + vozilo.gpsSirina() + " "
            + vozilo.gpsDuzina() + " " + r.gpsSirina() + " " + r.gpsDuzina() + "\n";
        MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaKazne(), r.mreznaVrataKazne(), cmd);
        if (p.brzaVozila.get(vozilo.id()) == null) {
          p.brzaVozila.put(vozilo.id(), vozilo);
        } else {
          p.brzaVozila.put(vozilo.id(), p.brzaVozila.get(vozilo.id()).postaviStatus(true));
        }
      }
      if (p.vrijemeIzmeduPodataka(vozilo) / 1000 > r.maksTrajanje() * 2) {
        p.brzaVozila.put(vozilo.id(), vozilo.postaviStatus(false));
        return "ERROR 39 Došlo je do pogreške u radu radara. status: "
            + p.brzaVozila.get(vozilo.id()).status() + "\n";
      }
    }
    return "OK\n";
  }

}
