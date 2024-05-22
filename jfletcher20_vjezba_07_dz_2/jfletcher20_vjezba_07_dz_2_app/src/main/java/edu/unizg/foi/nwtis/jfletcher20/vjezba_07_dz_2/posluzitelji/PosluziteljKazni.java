package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji;

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
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa poslužitelj kazni.
 */
public class PosluziteljKazni {
  /**
   * Format datuma.
   */
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  /**
   * Mrežna vrata poslužitelja.
   */
  int mreznaVrata;
  /**
   * Predložak za kaznu.
   */
  private Pattern predlozakKazna = Pattern.compile( //
      "^VOZILO " //
          + "(?<id>\\d+) " //
          + "(?<vrijemePocetak>\\d+) " //
          + "(?<vrijemeKraj>\\d+) " //
          + "(?<brzina>-?\\d+([.]\\d+)?) " //
          + "(?<gpsSirina>\\d+[.]\\d+) " //
          + "(?<gpsDuzina>\\d+[.]\\d+) " //
          + "(?<gpsSirinaRadara>\\d+[.]\\d+) " //
          + "(?<gpsDuzinaRadara>\\d+[.]\\d+)$");
  /**
   * Predložak za dohvaćanje kazne.
   */
  private Pattern predlozakDohvatiKaznu = Pattern.compile( //
      "^VOZILO " //
          + "(?<id>\\d+) " //
          + "(?<vrijemeOd>\\d+) " //
          + "(?<vrijemeDo>\\d+)$");
  /**
   * Predložak za statistiku.
   */
  private Pattern predlozakStatistika = Pattern.compile( //
      "^STATISTIKA " //
          + "(?<vrijemeOd>\\d+) " //
          + "(?<vrijemeDo>\\d+)$");
  /**
   * Predložak za TEST naredbu.
   */
  private Pattern predlozakTest = Pattern.compile("^TEST$");

  /**
   * Matcher za poklapanje uzorka.
   */
  private Matcher poklapanjeKazna;

  /**
   * Sve kazne koje su zabilježene.
   */
  public volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

  /**
   * Pokretanje poslužitelja.
   * 
   * @param args Argumenti komandne linije.
   */
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
      e.printStackTrace();
      return;
    }
  }

  /**
   * Metoda za inicijalizaciju rada poslužitelja.
   */
  public void pokreniPosluzitelja() {
    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(mreznaVrata)) {
      while (true) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "UTF-8"));
        PrintWriter pisac = new PrintWriter(
            new OutputStreamWriter(mreznaUticnica.getOutputStream(), "UTF-8"), true);
        var redak = citac.readLine();
        mreznaUticnica.shutdownInput();
        // var obrada = obradaZahtjeva(redak); // TODO: verify that removing this line doesn't break
        // anything
        pisac.println(obradaZahtjeva(redak));
        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva.
   * 
   * @param zahtjev Zahtjev koji se obrađuje.
   * @return Odgovor na zahtjev.
   */
  public String obradaZahtjeva(String zahtjev) {
    var odgovor = "";
    if (zahtjev == null)
      odgovor = "ERROR 40 Neispravna sintaksa naredbe.\n";
    else if (predlozakKazna.matcher(zahtjev).matches())
      odgovor = obradaZahtjevaKazna(zahtjev);
    else if (predlozakDohvatiKaznu.matcher(zahtjev).matches())
      odgovor = obradaZahtjevaDohvatiKaznu(zahtjev);
    else if (predlozakStatistika.matcher(zahtjev).matches())
      odgovor = obradaZahtjevaStatistika(zahtjev);
    else if (predlozakTest.matcher(zahtjev).matches())
      return "OK\n";
    return odgovor != null ? odgovor : "ERROR 40 Neispravna sintaksa naredbe.\n";
  }

  /**
   * Obrada zahtjeva za upisivanjem kazne.
   * 
   * @param zahtjev Zahtjev za kaznu.
   * @return Odgovor na zahtjev za kaznu.
   */
  public String obradaZahtjevaKazna(String zahtjev) {
    poklapanjeKazna = predlozakKazna.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var kazna = new PodaciKazne(Parsiraj.pi(poklapanjeKazna.group("id")),
          Parsiraj.pl(poklapanjeKazna.group("vrijemePocetak")),
          Parsiraj.pl(poklapanjeKazna.group("vrijemeKraj")),
          Parsiraj.pd(poklapanjeKazna.group("brzina")),
          Parsiraj.pd(poklapanjeKazna.group("gpsSirina")),
          Parsiraj.pd(poklapanjeKazna.group("gpsDuzina")),
          Parsiraj.pd(poklapanjeKazna.group("gpsSirinaRadara")),
          Parsiraj.pd(poklapanjeKazna.group("gpsDuzinaRadara")));
      sveKazne.add(kazna);
      System.out.println("Id: " + kazna.id() + " Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
          + "  Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + " Brzina: " + kazna.brzina()
          + " GPS: " + kazna.gpsSirina() + ", " + kazna.gpsDuzina());

      // TODO: poslati POST zahtjev RESTful web servisu za evidenciju kazni e-vozila

      return "OK\n";
    }
    return null;
  }

  /**
   * Obrada zahtjeva za dohvaćanje kazne.
   * 
   * @param zahtjev Zahtjev za dohvaćanje kazne.
   * @return Odgovor na zahtjev za dohvaćanje kazne.
   */
  public String obradaZahtjevaDohvatiKaznu(String zahtjev) {
    poklapanjeKazna = predlozakDohvatiKaznu.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var id = Parsiraj.pi(poklapanjeKazna.group("id"));
      var vrijemeOd = Parsiraj.pl(poklapanjeKazna.group("vrijemeOd"));
      var vrijemeDo = Parsiraj.pl(poklapanjeKazna.group("vrijemeDo"));
      try {
        var kazna = sveKazne.stream().filter(
            k -> k.id() == id && k.vrijemePocetak() >= vrijemeOd && k.vrijemeKraj() <= vrijemeDo)
            .sorted((k1, k2) -> Long.compare(k2.vrijemePocetak(), k1.vrijemePocetak())).findFirst();
        if (kazna.isPresent()) {
          var k = kazna.get();
          return "OK " + k.vrijemePocetak() + " " + k.brzina() + " " + k.gpsSirinaRadar() + " "
              + k.gpsDuzinaRadar() + "\n";
        }
      } catch (Exception e) {
        return "ERROR 41 Nema kazne za vozilo " + id + " u zadanom vremenu.\n";
      }
      return "ERROR 41 Nema kazne za vozilo " + id + " u zadanom vremenu.\n";
    }
    return null;
  }

  /**
   * Obrada zahtjeve za statistikom.
   * 
   * @param zahtjev Zahtjev za statistikom.
   * @return Odgovor na zahtjev za statistikom.
   */
  public String obradaZahtjevaStatistika(String zahtjev) {
    poklapanjeKazna = predlozakStatistika.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var vrijemeOd = Parsiraj.pl(poklapanjeKazna.group("vrijemeOd"));
      var vrijemeDo = Parsiraj.pl(poklapanjeKazna.group("vrijemeDo"));
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

  /**
   * Preuzimanje postavki iz konfiguracije.
   * 
   * @param args Argumenti komandne linije.
   * @throws NeispravnaKonfiguracija U slučaju neispravne konfiguracije.
   * @throws NumberFormatException U slučaju neispravnog formata broja.
   * @throws UnknownHostException U slučaju nepostojeće adrese.
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    mreznaVrata = Parsiraj.pi(konfig.dajPostavku("mreznaVrataKazne"));
  }
}
