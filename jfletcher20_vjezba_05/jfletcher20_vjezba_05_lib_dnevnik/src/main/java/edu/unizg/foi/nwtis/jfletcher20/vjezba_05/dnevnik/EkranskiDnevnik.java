package edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.podaci.DnevnikRada;

public class EkranskiDnevnik implements ZapisivacRada {
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  private List<DnevnikRada> sveZapisiDnevnikaRada;

  @Override
  public boolean upisiDnevnik(DnevnikRada dnevnikRada) {
    var vrijeme = sdf.format(new Date(dnevnikRada.vrijeme()));
    System.out.println("%s %s %s %s %s %s %s".formatted(vrijeme, dnevnikRada.korisnickoIme(),
        dnevnikRada.adresaRacunala(), dnevnikRada.ipAdresaRacunala(), dnevnikRada.nazivOS(),
        dnevnikRada.verzijaVM(), dnevnikRada.opisRada()));

    this.sveZapisiDnevnikaRada.add(dnevnikRada);

    return true;
  }

  @Override
  public List<DnevnikRada> dohvatiDnevnik(long vrijemeOd, long vrijemeDo, String korisnickoIme) {

    var odgovor = this.sveZapisiDnevnikaRada.stream().filter(dr -> dr.vrijeme() >= vrijemeOd
        && dr.vrijeme() <= vrijemeDo && dr.korisnickoIme().compareTo(korisnickoIme) == 0).toList();

    return odgovor;
  }

  @Override
  public boolean pripremiResurs() throws Exception {
    this.sveZapisiDnevnikaRada = new ArrayList<>();
    return true;
  }

  @Override
  public boolean otpustiResurs() throws Exception {
    return true;
  }

  @Override
  public boolean trebaDatoteku() {
    return false;
  }

  @Override
  public boolean trebaPodatkeBazaPodataka() {
    return false;
  }

}
