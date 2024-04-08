package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.PosluziteljRadara;

public class RadnikZaRadare implements Runnable {

  private Socket s;
  private PodaciRadara rad;
  private PosluziteljRadara p;

  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      PosluziteljRadara posluziteljRadara) {
    super();
    this.s = mreznaUticnica;
    this.rad = podaciRadara;
    this.p = posluziteljRadara;
  }

  private Pattern predlozakBrzine = Pattern.compile("^VOZILO (?<id>\\d+)" + "(?<vrijeme>\\d+)"
      + "(?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  @Override
  public void run() {

    try {


      BufferedReader citac = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf8"));
      OutputStream out = s.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      var redak = citac.readLine();

      s.shutdownInput();
      pisac.println(obradaZahtjeva(redak));

      pisac.flush();
      s.shutdownOutput();
      s.close();
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  private String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null)
      return "ERROR 10 Neispravna sintaksa naredbe.";
    var r = obradaZahtjevaBrzine(zahtjev);
    if (r != null)
      return r;
    else
      return "ERROR 10 Neispravna sintaksa komande.";
  }

  private String obradaZahtjevaBrzine(String zahtjev) {
    var kazna = this.predlozakBrzine.matcher(zahtjev).matches();
    return kazna ? "OK" : null; // TODO dovrsiti sami
  }

}
