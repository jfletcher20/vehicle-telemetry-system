package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;

public class PosluziteljZaRegistracijuRadara implements Runnable {
  private int mreznaVrata;
  private CentralniSustav centralniSustav;
  private Pattern predlozakRegistracijeRadara = Pattern.compile(
      "^RADAR (?<id>\\d+) (?<adresa>\\w+) (?<mreznaVrata>\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>-?\\d+?) $");

  private Matcher poklapanjeRegistracijeRadara;

  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }


  @Override
  public void run() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        pisac.println(obradaZahtjeva(redak));

        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }

  }

  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 10 Neispravna sintaksa komande.";
  }

  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    this.poklapanjeRegistracijeRadara = this.predlozakRegistracijeRadara.matcher(zahtjev);
    var statusKazna = poklapanjeRegistracijeRadara.matches();
    if (statusKazna) {
      var radar = new PodaciRadara(Integer.valueOf(this.poklapanjeRegistracijeRadara.group("id")),
          this.poklapanjeRegistracijeRadara.group("adresa"),
          Integer.valueOf(this.poklapanjeRegistracijeRadara.group("mreznaVrata")), -1, -1,
          Integer.valueOf(this.poklapanjeRegistracijeRadara.group("maksUdaljenost")), null, -1,
          null, -1, null, Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsSirina")),
          Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsDuzina")));

      this.centralniSustav.sviRadari.put(radar.id(), radar);

      return "OK";
    }
    return null;
  }

}
