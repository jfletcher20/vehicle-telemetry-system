package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciKazne;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/*
 * Pokretanje programa PosluziteljKazni sadrži jedan argument: datoteka.(txt | xml | bin | json)
 * Npr. NWTiS_DZ1_PK.txt Otvara se mrežna utičnica na zadanim mrežnim vratima te se čeka na
 * klijente. Klijent se spaja na PosluziteljKazni putem mrežne utičnice i šalje komandu (završava s
 * \n) poslužitelju na temelju postavki i traži izvršavanja određene akcije i vraća odgovor
 * (završava s \n): • VOZILO id vrijemePocetak vrijemeKraj brzina gpsSirina gpsDuzina gpsSi
 * rinaRadara gpsDuzinaRadara o npr. VOZILO 1 1711348009 1711368009 21.767 46.286608 16.353131
 * 46.286602 16.353136 o Provjera da li ispravni podaci. Ako su ispravni u evidenciju kazni upisuje
 * podatke za e-vozilo, ispisuje na ekran podatke o kazni i vraća OK. o Npr. OK • VOZILO id
 * vrijemeOd vrijemeDo o npr. VOZILO 1 1711348009 1711355209 o Provjera da li ispravni podaci. Ako
 * su ispravni u evidenciji kazni traži podatke o e-vozilu unutar zadanog vremena od-do. Ako postoje
 * kazne vraća zadnju (najsvježiju) kaznu OK vri jeme brzina gpsSirinaRadar gpsDuzinaRadar. o Npr.
 * OK 1711348009 21.767 46.286602 16.353136 • STATISTIKA vrijemeOd vrijemeDo o npr. STATISTIKA
 * 1711348009 1711355209 o Provjera da li ispravni podaci. Ako su ispravni u evidenciji prekršaja
 * traži podatke o broju kazni unutar zadanog vremena od-do. Ako postoje kazne vraća OK idVozilo
 * brojKazni; id Vozilo brojKazni; idVozilo brojKazni;… o Npr. OK 1 0; 2 7; 3 2; 4 5; Kodovi
 * pogrešaka su: o Kada format komande nije ispravan, vraća odgovor ERROR 40 tekst (tekst objašnjava
 * razlog pogreške) o Kada e-vozilo id nema kazne u zadanom vremenu, vraća odgovor ERROR 41 tekst
 * (tekst objašnjava razlog pogreške). o Kada nešto drugo nije u redu vraća odgovor ERROR 49 tekst
 * (tekst objašnjava razlog po greške).
 */

public class PosluziteljKazni {
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  int mreznaVrata;
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
  private Pattern predlozakDohvatiKaznu = Pattern.compile( //
      "^VOZILO " //
          + "(?<id>\\d+) " //
          + "(?<vrijemeOd>\\d+) " //
          + "(?<vrijemeDo>\\d+)$");
  private Pattern predlozakStatistika = Pattern.compile( //
      "^STATISTIKA " //
          + "(?<vrijemeOd>\\d+) " //
          + "(?<vrijemeDo>\\d+)$");

  private Matcher poklapanjeKazna;
  private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }
    System.out.println("Pokretanje posluzitelja kazni...");
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
    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(mreznaVrata)) {
      System.out.println("Posluzitelj kazni pokrenut na portu: " + mreznaVrata);
      while (true) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "UTF-8"));
        PrintWriter pisac = new PrintWriter(
            new OutputStreamWriter(mreznaUticnica.getOutputStream(), "UTF-8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        var obrada = obradaZahtjeva(redak);
        System.out.println(obrada);
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
    System.out.println("Zahtjev: " + zahtjev);
    if (zahtjev == null)
      return "ERROR 40 Neispravna sintaksa naredbe.\n";
    else if (predlozakKazna.matcher(zahtjev).matches()) {
      var odgovor = obradaZahtjevaKazna(zahtjev);
      if (odgovor != null)
        return odgovor;
    } else if (predlozakDohvatiKaznu.matcher(zahtjev).matches()) {
      var odgovor = obradaZahtjevaDohvatiKaznu(zahtjev);
      if (odgovor != null)
        return odgovor;
    } else if (predlozakStatistika.matcher(zahtjev).matches()) {
      var odgovor = obradaZahtjevaStatistika(zahtjev);
      if (odgovor != null)
        return odgovor;
    }
    return "ERROR 49 Nešto je pošlo po zlu.\n";
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

  public String obradaZahtjevaKazna(String zahtjev) {
    poklapanjeKazna = predlozakKazna.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var kazna = new PodaciKazne(_i(poklapanjeKazna.group("id")), //
          _l(poklapanjeKazna.group("vrijemePocetak")), //
          _l(poklapanjeKazna.group("vrijemeKraj")), //
          _d(poklapanjeKazna.group("brzina")), //
          _d(poklapanjeKazna.group("gpsSirina")), //
          _d(poklapanjeKazna.group("gpsDuzina")), //
          _d(poklapanjeKazna.group("gpsSirinaRadar")), _d(poklapanjeKazna.group("gpsDuzinaRadar")));
      sveKazne.add(kazna);
      System.out.println("Id: " + kazna.id() + " Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
          + "  Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + " Brzina: " + kazna.brzina()
          + " GPS: " + kazna.gpsSirina() + ", " + kazna.gpsDuzina());
      return "OK\n";
    }
    return null;
  }

  public String obradaZahtjevaDohvatiKaznu(String zahtjev) {
    poklapanjeKazna = predlozakDohvatiKaznu.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var id = _i(poklapanjeKazna.group("id"));
      var vrijemeOd = _l(poklapanjeKazna.group("vrijemeOd"));
      var vrijemeDo = _l(poklapanjeKazna.group("vrijemeDo"));
      try {
        var kazna = sveKazne.stream().filter(
            k -> k.id() == id && k.vrijemePocetak() >= vrijemeOd && k.vrijemeKraj() <= vrijemeDo)
            .sorted((k1, k2) -> Long.compare(k2.vrijemePocetak(), k1.vrijemePocetak())).findFirst();
        if (kazna.isPresent()) {
          var k = kazna.get();
          return "OK " + k.vrijemePocetak() + " " + k.brzina() + " " + k.gpsSirinaRadar() + " "
              + k.gpsDuzinaRadar() + "\n"; // TODO: verify extra information is allowed/necessary
        }
      } catch (Exception e) {
        return "ERROR 41 Nema kazne za vozilo " + id + " u zadanom vremenu.\n";
      }
      return "ERROR 41 Nema kazne za vozilo " + id + " u zadanom vremenu.\n";
    }
    return null;
  }

  public String obradaZahtjevaStatistika(String zahtjev) {
    poklapanjeKazna = predlozakStatistika.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var vrijemeOd = _l(poklapanjeKazna.group("vrijemeOd"));
      var vrijemeDo = _l(poklapanjeKazna.group("vrijemeDo"));
      Stream<PodaciKazne> kazne = sveKazne.stream()
          .filter(k -> k.vrijemePocetak() >= vrijemeOd && k.vrijemeKraj() <= vrijemeDo);
      int[] uniqueIds = kazne.mapToInt(k -> k.id()).distinct().toArray();
      if (uniqueIds.length > 0) {
        StringBuilder output = new StringBuilder("OK ");
        for (int id : uniqueIds) {
          long brojKazni = sveKazne.stream().filter(k -> k.id() == id).count();
          output.append(id).append(" ").append(brojKazni).append("; ");
        }
        return output.toString().trim() + "\n"; // TODO: test
      }
    }
    return null;
  }

  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    mreznaVrata = _i(konfig.dajPostavku("mreznaVrataKazne"));
  }
}
