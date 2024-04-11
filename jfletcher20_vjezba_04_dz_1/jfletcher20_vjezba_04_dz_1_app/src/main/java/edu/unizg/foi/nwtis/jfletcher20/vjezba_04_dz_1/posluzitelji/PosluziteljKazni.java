package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciKazne;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

public class PosluziteljKazni {
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  int mreznaVrata;
  private Pattern predlozakKazna = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  private Matcher poklapanjeKazna;
  private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    try {
      posluziteljKazni.preuzmiPostavke(args);

      posluziteljKazni.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  public void pokreniPosluzitelja() {
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

  public void preuzmiPostavke(String[] args)
      throws unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija, NumberFormatException,
      UnknownHostException {
    unizg.foi.nwtis.konfiguracije.Konfiguracija konfig =
        unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }
}
