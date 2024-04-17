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
      System.out.println("Primljena naredba: " + redak);
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
    var v = p.vrijemeIzmeduPodataka(vozilo);
    return vozilo.brzina() > r.maksBrzina() && v >= r.maksTrajanje() && v < r.maksTrajanje() * 2;
  }

  private String obradaZahtjevaBrzine(String zahtjev) {
    var podaciVozila = predlozakBrzine.matcher(zahtjev);
    if (!podaciVozila.matches())
      return null;
    // parsiranje zahtjeva u objekt
    System.out.println("Radnik radi na zahtjevu: " + zahtjev);
    var vozilo = new BrzoVozilo(podaciVozila.group("id"), -1, podaciVozila.group("vrijeme"),
        podaciVozila.group("brzina"), //
        podaciVozila.group("gpsSirina"), //
        podaciVozila.group("gpsDuzina"), //
        false);
    // provjera udaljenosti
    var udaljenost = GpsUdaljenostBrzina.udaljenostKm(r.gpsSirina(), r.gpsDuzina(),
        vozilo.gpsSirina(), vozilo.gpsDuzina());
    if (GpsUdaljenostBrzina.udaljenostKm(r.gpsSirina(), r.gpsDuzina(), vozilo.gpsSirina(),
        vozilo.gpsDuzina()) * 1000 < r.maksUdaljenost()) {
      // provjera brzine i trajanja brze vožnje
      System.out.println("Vozilo " + vozilo.id() + " je u dosegu radara.");
      if (jeBrzaVoznja(vozilo)) {
        String cmd = "VOZILO " + vozilo.id() + " " + p.prviZapisOVozilu(vozilo.id()).vrijeme() + " "
            + vozilo.vrijeme() + " " + vozilo.brzina() + " " + vozilo.gpsSirina() + " "
            + vozilo.gpsDuzina() + " " + r.gpsSirina() + " " + r.gpsDuzina() + "\n";
        MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaKazne(), r.mreznaVrataKazne(), cmd);
        // TODO: test, but also ensure that this is what's actually expected of this method
        System.out.print("is indeed a brza voznja -- ");
        System.out.println(p.brzaVozila.size());
        return "OK\n";
      } else if (p.vrijemeIzmeduPodataka(vozilo) < r.maksTrajanje() * 2) {
        return "ERROR 39 Došlo je do pogreške u radu radara.\n";
      }
    }
    System.out.println(p.brzaVozila.size());
    return "OK\n";
  }

}
