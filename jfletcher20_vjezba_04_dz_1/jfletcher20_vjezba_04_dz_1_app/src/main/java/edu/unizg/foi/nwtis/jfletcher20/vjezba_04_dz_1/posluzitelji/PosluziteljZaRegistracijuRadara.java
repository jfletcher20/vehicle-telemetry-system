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

/*
 * Klijent se spaja na PosluziteljZaRegistracijuRadara putem mrežne utičnice te šalje komandu
 * (završava s \n) poslužitelju na temelju postavki i traži izvršavanja određene akcije i vraća
 * odgovor (završava s \n) • RADAR id adresa mreznaVrata gpsSirina gpsDuzina maksUdaljenost o npr.
 * RADAR 1 localhost 8010 46.29950 16.33001 100 o Provjera da li ispravni podaci. Ako su ispravni
 * provjerava da li su podaci tog zahtjeva spremljeni u memoriji u kolekciji R. Ako nisu, upisuje ih
 * u kolekciju R (tj. registracija radara) i vraća OK. o Npr. OK • RADAR OBRIŠI id o npr. RADAR
 * OBRIŠI 1 o Provjera da li ispravni podaci. Ako su ispravni provjerava da li su podaci tog
 * zahtjeva spremljeni u memoriji u kolekciji R. Ako jesu, briše ih iz kolekcije R (tj.
 * deregistracija radara) i vraća OK. o Npr. OK • RADAR OBRIŠI SVE o npr. RADAR OBRIŠI SVE o
 * Provjera da li ispravni podaci. Ako su ispravni briše sve radare iz kolekcije R (tj.
 * deregistracija svih radara) i vraća OK. o Npr. OK
 */

public class PosluziteljZaRegistracijuRadara implements Runnable {
  private int mreznaVrata;
  private CentralniSustav centralniSustav;
  private Pattern predlozakRegistracijeRadara = Pattern.compile(//
      "^RADAR " //
          + "(?<id>\\d+) " //
          + "(?<adresa>\\w+) " //
          + "(?<mreznaVrata>\\d+) " //
          + "(?<gpsSirina>\\d+[.]\\d+) " //
          + "(?<gpsDuzina>\\d+[.]\\d+) " //
          + "(?<maksUdaljenost>-?\\d+?)$");
  private Pattern predlozakBrisanjaRadara = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");
  private Pattern predlozakBrisanjaSvihRadara = Pattern.compile("^RADAR OBRIŠI SVE$");

  private Matcher poklapanjeRegistracijeRadara;

  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }

  private Double _d(String value) {
    return Double.valueOf(value);
  }

  private Integer _i(String value) {
    return Integer.valueOf(value);
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
        pisac.println(obradaZahtjeva(redak)); // TODO: maybe remove this println? need to check if
                                              // it's necessary
        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }

  }

  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null)
      return "ERROR 10 Neispravna sintaksa komande.\n";
    var odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
    return odgovor != null ? odgovor : "ERROR 10 Neispravna sintaksa komande.\n";
  }

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
    return null;
  }

  public String registrirajRadar() {
    var radar = new PodaciRadara(_i(poklapanjeRegistracijeRadara.group("id")),
        poklapanjeRegistracijeRadara.group("adresa"),
        _i(poklapanjeRegistracijeRadara.group("mreznaVrata")), -1, -1,
        _i(poklapanjeRegistracijeRadara.group("maksUdaljenost")), null, -1, null, -1, null,
        _d(poklapanjeRegistracijeRadara.group("gpsSirina")),
        _d(poklapanjeRegistracijeRadara.group("gpsDuzina")));
    if (centralniSustav.sviRadari.containsKey(radar.id())) {
      return "ERROR 11 Radar s ID-em " + radar.id() + " već postoji.\n";
    } else
      centralniSustav.sviRadari.put(radar.id(), radar);
    return "OK\n";
  }

  public String obrisiRadar() {
    var id = _i(poklapanjeRegistracijeRadara.group("id"));
    if (centralniSustav.sviRadari.containsKey(id)) {
      centralniSustav.sviRadari.remove(id);
      return "OK\n";
    } else
      return "ERROR 12 Radar s ID-em " + id + " ne postoji.\n";
  }

  public String obrisiSveRadare() {
    centralniSustav.sviRadari.clear();
    return "OK\n";
  }

}
