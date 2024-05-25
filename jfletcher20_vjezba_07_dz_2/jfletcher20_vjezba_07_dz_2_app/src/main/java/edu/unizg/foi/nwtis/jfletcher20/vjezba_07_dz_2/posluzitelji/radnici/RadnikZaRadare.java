package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara;

/**
 * Radnik za radare.
 */
public class RadnikZaRadare implements Runnable {

  /**
   * Mrežna utičnica.
   */
  private Socket s;
  /**
   * Podaci o radaru.
   */
  private PodaciRadara r;
  /**
   * Poslužitelj radara.
   */
  private PosluziteljRadara p;

  /**
   * Konstruktor za radnika radara.
   * 
   * @param mreznaUticnica
   * @param podaciRadara
   * @param posluziteljRadara
   */
  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      PosluziteljRadara posluziteljRadara) {
    super();
    this.s = mreznaUticnica;
    this.r = podaciRadara;
    this.p = posluziteljRadara;
  }

  /**
   * Predložak za naredbu s podacima o vozilu i brzini.
   */
  private Pattern predlozakBrzine = Pattern.compile("^VOZILO " //
      + "(?<id>-?\\d+) " //
      + "(?<vrijeme>-?\\d+) " //
      + "(?<brzina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsSirina>-?\\d+(?:\\.\\d+)?) " //
      + "(?<gpsDuzina>-?\\d+(?:\\.\\d+)?)$"); // npr. VOZILO 1 1711348009 21.767 46.286608 16.353131
  /**
   * Predložak za naredbu za resetiranje radara.
   */
  private Pattern predlozakReset = Pattern.compile("^RADAR RESET$");

  /**
   * Predložak za naredbu za provjeru radara.
   */
  private Pattern predlozakProvjera = Pattern.compile("^RADAR (?<id>\\d+)$");


  /**
   * Pokreće radnika.
   */
  @Override
  public void run() {
    try {
      BufferedReader citac = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf8"));
      OutputStream out = s.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      var redak = citac.readLine();
      s.shutdownInput();
      var odgovor = obradaZahtjeva(redak);
      pisac.println(odgovor);
      pisac.flush();
      s.shutdownOutput();
      s.close();
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva posluzitelja.
   * 
   * @param zahtjev zahtjev
   */
  private String obradaZahtjeva(String zahtjev) {
    var odgovor = "";
    if (zahtjev == null)
      odgovor = "ERROR 30 Neispravna sintaksa naredbe.\n";
    if (predlozakReset.matcher(zahtjev).matches())
      odgovor = obradaZahtjevaReset();
    else if (predlozakProvjera.matcher(zahtjev).matches())
      odgovor = obradaZahtjevaProvjera(zahtjev);
    else if (predlozakBrzine.matcher(zahtjev).matches())
      odgovor = obradaZahtjevaBrzine(zahtjev);
    return odgovor != null ? odgovor : "ERROR 30 Neispravna sintaksa naredbe.\n";
  }

  /**
   * Obrada zahtjeva za resetiranje radara.
   * 
   * @return odgovor na zahtjev
   */
  private String obradaZahtjevaReset() {
    try {
      var provjeraSveOk = MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaRegistracije(),
          r.mreznaVrataRegistracije(), "RADAR " + r.id() + "\n");
      if (provjeraSveOk.contains("OK"))
        return "OK\n";
      else if (provjeraSveOk.contains("ERROR 12")) {
        if (MrezneOperacije
            .posaljiZahtjevPosluzitelju(r.adresaRegistracije(), r.mreznaVrataRegistracije(),
                "RADAR " + r.id() + " " + r.adresaRadara() + " " + r.mreznaVrataRadara() + " "
                    + r.gpsSirina() + " " + r.gpsDuzina() + " " + r.maksUdaljenost() + "\n")
            .contains("OK"))
          return "OK\n";
      }
    } catch (Exception e) {
      return "ERROR 32 Posluzitelj PosluziteljZaRegistracijuRadara nije dostupan.\n";
    }
    return null;
  }

  /**
   * Obrada zahtjeva za provjeru radara.
   * 
   * @param zahtjev zahtjev
   * @return odgovor na zahtjev
   */
  private String obradaZahtjevaProvjera(String zahtjev) {
    var poklapanje = predlozakProvjera.matcher(zahtjev);
    if (!poklapanje.matches())
      return null;
    try {
      Integer.parseInt(poklapanje.group("id"));
    } catch (NumberFormatException e) {
      return "ERROR 30 Neispravna sintaksa naredbe.\n";
    }
    if (Integer.parseInt(poklapanje.group("id")) != r.id())
      return "ERROR 33 Identifikator radara ne odgovara.\n";
    try {
      var provjeraOk = MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaKazne(),
          r.mreznaVrataKazne(), "TEST\n");
      if (provjeraOk.contains("OK"))
        return "OK\n";
    } catch (Exception e) {
      return "ERROR 34 Posluzitelj kazni nije dostupan.\n";
    }
    return null;
  }

  /**
   * Obrada zahtjeva za potencijalno kaznjavanje brzine vozila.
   * 
   * @param zahtjev zahtjev s brzinom vozila
   * @return odgovor na zahtjev
   */
  private String obradaZahtjevaBrzine(String zahtjev) {
    var podaciVozila = predlozakBrzine.matcher(zahtjev);
    if (!podaciVozila.matches())
      return null;
    var vozilo = new BrzoVozilo(podaciVozila.group("id"), -1, podaciVozila.group("vrijeme"),
        podaciVozila.group("brzina"), podaciVozila.group("gpsSirina"),
        podaciVozila.group("gpsDuzina"), true);
    var poh = p.brzaVozila.get(vozilo.id());
    if (poh != null && !(r.maksBrzina() < vozilo.brzina())) {
      p.brzaVozila.put(vozilo.id(), poh.postaviStatus(false));
      return "OK\n";
    } else {
      if (poh != null && poh.status() == true) {
        if (kaznjivoVrijeme(vozilo, poh)) {
          String cmd = "VOZILO " + vozilo.id() + " " + poh.vrijeme() + " " + vozilo.vrijeme() + " "
              + vozilo.brzina() + " " + vozilo.gpsSirina() + " " + vozilo.gpsDuzina() + " "
              + r.gpsSirina() + " " + r.gpsDuzina() + "\n";
          try {
            var resp = MrezneOperacije.posaljiZahtjevPosluzitelju(r.adresaKazne(),
                r.mreznaVrataKazne(), cmd);
            if (resp == null)
              return "ERROR 34 Posluzitelj za kazne nije dostupan.\n";
          } catch (Exception e) {
            return "ERROR 34 PosluziteljKazni nije dostupan.\n";
          }
          p.brzaVozila.put(vozilo.id(), vozilo.postaviStatus(false));
        } else if (r.maksTrajanje() * 1000 + 1 > razlikaVremena(vozilo, poh))
          return "OK\n";
        return "OK\n";
      }
      p.brzaVozila.put(vozilo.id(), vozilo);
      return "OK\n";
    }
  }

  /**
   * Provjerava je li vrijeme između dva brza vozila kaznjivo.
   * 
   * @param a
   * @param b
   * @return razlika vremena je kaznjiva ili ne
   */
  private boolean kaznjivoVrijeme(BrzoVozilo a, BrzoVozilo b) {
    var maks = r.maksTrajanje() * 1000;
    return razlikaVremena(a, b) > maks && razlikaVremena(a, b) < maks * 2;
  }

  /**
   * Računa razliku u vremenu između dva brza vozila.
   * 
   * @param a
   * @param b
   * @return razlika u vremenu
   */
  private long razlikaVremena(BrzoVozilo a, BrzoVozilo b) {
    return Math.abs(a.vrijeme() - b.vrijeme());
  }

}
