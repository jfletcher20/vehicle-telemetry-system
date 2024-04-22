package edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik;

import java.util.List;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.podaci.DnevnikRada;

public class BazaPodatakaDnevnik implements ZapisivacRada {

  public BazaPodatakaDnevnik(String korime, String lozinka, String url_bp, String upravljac_bp) {

  }

  @Override
  public boolean pripremiResurs() throws Exception {
    // TODO Auto-generated method stub
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
