package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;

/**
 * Klasa PosluziteljZaRegistracijuRadara
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {

  /**
   * Mrežna vrata poslužitelja
   */
  private int mreznaVrata;
  /**
   * Referenca na centralni sustav
   */
  private CentralniSustav centralniSustav;

  /**
   * Predložak za registraciju radara
   */
  private Pattern predlozakRegistracijeRadara = Pattern.compile(//
      "^RADAR " //
          + "(?<id>\\d+) " //
          + "(?<adresa>\\([\\w.-]+)|(\\d+\\.\\d+\\.\\d+\\.\\d+)) " //
          + "(?<mreznaVrata>\\d+) " //
          + "(?<gpsSirina>\\d+[.]\\d+) " //
          + "(?<gpsDuzina>\\d+[.]\\d+) " //
          + "(?<maksUdaljenost>-?\\d+?)$");
  /**
   * Predložak za dohvaćanje svih radara
   */
  private Pattern predlozakDohvacanjaSvihRadara = Pattern.compile("^RADAR SVI$");
  /**
   * Predložak za resetiranje radara
   */
  private Pattern predlozakResetiranjaRadara = Pattern.compile("^RADAR RESET$");
  /**
   * Predložak za provjeru postojanja radara
   */
  private Pattern predlozakPostojiRadar = Pattern.compile("^RADAR (?<id>\\d+)$");
  /**
   * Predložak za brisanje radara
   */
  private Pattern predlozakBrisanjaRadara = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");
  /**
   * Predložak za brisanje svih radara
   */
  private Pattern predlozakBrisanjaSvihRadara = Pattern.compile("^RADAR OBRIŠI SVE$");

  /**
   * Matcher za poklapanje registracije radara
   */
  private Matcher poklapanjeRegistracijeRadara;

  /**
   * Konstruktor klase PosluziteljZaRegistracijuRadara
   * 
   * @param mreznaVrata Mrežna vrata poslužitelja
   * @param centralniSustav Referenca na centralni sustav
   */
  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }


  /**
   * Pokreće poslužitelj za registraciju radara
   */
  @Override
  public void run() {

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (true) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader bb =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "UTF-8"));
        PrintWriter pisac = new PrintWriter(
            new OutputStreamWriter(mreznaUticnica.getOutputStream(), "UTF-8"), true);
        var redak = bb.readLine();
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

  /**
   * Metoda za obradu zahtjeva poslužitelja.
   * 
   * @param zahtjev Zahtjev
   * @return Odgovor na zahtjev
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null)
      return "ERROR 10 Neispravna sintaksa komande.\n";
    try {
      var odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
      return odgovor != null ? odgovor : "ERROR 10 Neispravna sintaksa komande.\n";
    } catch (Exception e) {
      return "ERROR 10 Neispravna sintaksa komande (krivi parametar proslijeden).\n";
    }
  }

  /**
   * Metoda za obradu zahtjeva nad radarom.
   * 
   * @param zahtjev Zahtjev
   * @return Odgovor na zahtjev
   */
  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    poklapanjeRegistracijeRadara = predlozakRegistracijeRadara.matcher(zahtjev);
    if (poklapanjeRegistracijeRadara.matches())
      return registrirajRadar();
    poklapanjeRegistracijeRadara = predlozakBrisanjaRadara.matcher(zahtjev);
    if (poklapanjeRegistracijeRadara.matches())
      return obrisiRadar();
    poklapanjeRegistracijeRadara = predlozakBrisanjaSvihRadara.matcher(zahtjev);
    if (poklapanjeRegistracijeRadara.matches())
      return obrisiSveRadare();
    poklapanjeRegistracijeRadara = predlozakPostojiRadar.matcher(zahtjev);
    if (poklapanjeRegistracijeRadara.matches())
      return provjeriPostojanjeRadar();
    poklapanjeRegistracijeRadara = predlozakResetiranjaRadara.matcher(zahtjev);
    if (poklapanjeRegistracijeRadara.matches())
      return resetirajRadare();
    poklapanjeRegistracijeRadara = predlozakDohvacanjaSvihRadara.matcher(zahtjev);
    if (poklapanjeRegistracijeRadara.matches())
      return dohvatiSveRadare();
    return null;
  }

  /**
   * Metoda za registraciju novoga radara.
   * 
   * @return Odgovor na zahtjev
   */
  public String registrirajRadar() {
    var radar = new PodaciRadara(Parsiraj.pi(poklapanjeRegistracijeRadara.group("id")),
        poklapanjeRegistracijeRadara.group("adresa"),
        Parsiraj.pi(poklapanjeRegistracijeRadara.group("mreznaVrata")), -1, -1,
        Parsiraj.pi(poklapanjeRegistracijeRadara.group("maksUdaljenost")), null, -1, null, -1, null,
        Parsiraj.pd(poklapanjeRegistracijeRadara.group("gpsSirina")),
        Parsiraj.pd(poklapanjeRegistracijeRadara.group("gpsDuzina")));
    if (centralniSustav.sviRadari.containsKey(radar.id())) {
      return "ERROR 11 Radar s ID-em " + radar.id() + " već postoji.\n";
    } else
      centralniSustav.sviRadari.put(radar.id(), radar);
    return "OK\n";
  }

  /**
   * Metoda za brisanje radara
   * 
   * @return Odgovor na zahtjev
   */
  public String obrisiRadar() {
    var id = Parsiraj.pi(poklapanjeRegistracijeRadara.group("id"));
    if (centralniSustav.sviRadari.containsKey(id)) {
      centralniSustav.sviRadari.remove(id);
      return "OK\n";
    } else
      return "ERROR 12 Radar s ID-em " + id + " ne postoji.\n";
  }

  /**
   * Metoda za brisanje svih radara
   * 
   * @return Odgovor na zahtjev
   */
  public String obrisiSveRadare() {
    centralniSustav.sviRadari.clear();
    return "OK\n";
  }

  /**
   * Metoda za provjeru postojanja radara
   * 
   * @return Odgovor na zahtjev
   */
  public String provjeriPostojanjeRadar() {
    var id = Parsiraj.pi(poklapanjeRegistracijeRadara.group("id"));
    if (centralniSustav.sviRadari.containsKey(id))
      return "OK\n";
    else // TODO: provjeriti je li ovo potrebno (u zadatku nije navedeno)
      return "ERROR 12 Radar koji ima ID " + id + " ne postoji.\n";
  }

  /**
   * Metoda za dohvaćanje svih radara
   * 
   * @return Odgovor na zahtjev
   */
  public String dohvatiSveRadare() {
    var odgovor = "OK {";
    for (var radar : centralniSustav.sviRadari.values())
      odgovor +=
          "[" + radar.id() + " " + radar.adresaRadara() + " " + radar.mreznaVrataRadara() + " "
              + radar.gpsSirina() + " " + radar.gpsDuzina() + " " + radar.maksUdaljenost() + "], ";
    return odgovor.contains("]") ? odgovor.substring(0, odgovor.length() - 2) + "}\n"
        : odgovor + "}\n";
  }

  /**
   * Metoda za resetiranje radara
   * 
   * @return Odgovor na zahtjev
   */
  public String resetirajRadare() {
    var brojRadara = centralniSustav.sviRadari.size();
    var brojObrisanihRadara = 0;
    for (var radar : centralniSustav.sviRadari.values()) {
      if (!MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(),
          radar.mreznaVrataRadara(), "RADAR " + radar.id()).equals("OK"))
        centralniSustav.sviRadari.remove(radar.id());
      brojObrisanihRadara++;
    }
    return "OK " + brojRadara + " " + brojObrisanihRadara + "\n";
  }

}
