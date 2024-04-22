package edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.podaci.DnevnikRada;

public class DatotecniDnevnik implements ZapisivacRada {

  public DatotecniDnevnik() {}

  public DatotecniDnevnik(String nazivDatoteke, boolean brisanjeDatoteke) {
    if (brisanjeDatoteke && Files.exists(Paths.get(nazivDatoteke))) {
      try {
        Files.delete(Paths.get(nazivDatoteke));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
    return true;
  }

  @Override
  public boolean trebaPodatkeBazaPodataka() {
    // TODO Auto-generated method stub
    return false;
  }

}
