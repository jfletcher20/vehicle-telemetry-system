package edu.unizg.foi.nwtis.jfletcher20.vjezba_05.dnevnik;

import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_05.dnevnik.podaci.DnevnikRada;

public class BazaPodatakaDnevnik implements ZapisivacRada {

  public String korime, lozinka, url_bp, upravljac_bp;
  public BazaPodatakaDnevnik(String korime, String lozinka, String url_bp, String upravljac_bp) {
    this.korime = korime;
    this.lozinka = lozinka;
    this.url_bp = url_bp;
    this.upravljac_bp = upravljac_bp;
  }

  @Override
  public boolean pripremiResurs() throws Exception {
    Class.forName(this.upravljac_bp);
    var sqlDodavanje = "INSERT INTO dnevnik_rada (korisnicko_ime, datum_vrijeme, radnja, status) VALUES (?, ?, ?, ?)";
    return false;
  }

  @Override
  public boolean otpustiResurs() throws Exception {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean upisiDnevnik(DnevnikRada dnevnikRada) throws Exception {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<DnevnikRada> dohvatiDnevnik(long vrijemeOd, long vrijemeDo, String korisnickoIme)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean trebaDatoteku() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean trebaPodatkeBazaPodataka() {
    // TODO Auto-generated method stub
    return false;
  }

}
