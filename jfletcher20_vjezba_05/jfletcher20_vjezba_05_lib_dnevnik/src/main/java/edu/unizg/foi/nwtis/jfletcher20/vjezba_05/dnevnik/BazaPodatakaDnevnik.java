package edu.unizg.foi.nwtis.jfletcher20.vjezba_05.dnevnik;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_05.dnevnik.podaci.DnevnikRada;

public class BazaPodatakaDnevnik implements ZapisivacRada {

  public String korime, lozinka, url_bp, upravljac_bp;
  private Connection vezaBP;
  private PreparedStatement naredbaDodavanje, naredbaPretrazivanje;

  public BazaPodatakaDnevnik() {}

  public BazaPodatakaDnevnik(String korime, String lozinka, String url_bp, String upravljac_bp) {
    this.korime = korime;
    this.lozinka = lozinka;
    this.url_bp = url_bp;
    this.upravljac_bp = upravljac_bp;
  }

  @Override
  public boolean pripremiResurs() throws Exception {

    Class.forName(this.upravljac_bp);

    var sqlDodavanje = "INSERT INTO dnevnik_Rada " // TODO: maybe will have to change to
                                                   // dnevnik_rada
        + "(vrijeme, korisnickoIme, adresaRacunala, ipAdresaRacunala, nazivOS, verzijaVM, opisRada) " //
        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    var sqlPretrazivanje =
        "SELECT vrijeme, korisnickoIme, adresaRacunala, ipAdresaRacunala, nazivOS, verzijaVM, opisRada " //
            + "FROM dnevnik_Rada " //
            + "WHERE korisnickoIme = ? AND vrijeme BETWEEN ? AND ?";

    vezaBP = java.sql.DriverManager.getConnection(this.url_bp, this.korime, this.lozinka);

    naredbaDodavanje = vezaBP.prepareStatement(sqlDodavanje);
    naredbaPretrazivanje = vezaBP.prepareStatement(sqlPretrazivanje);

    return true;

  }

  @Override
  public boolean otpustiResurs() throws Exception {
    vezaBP.close();
    return true;
  }

  @Override
  public boolean upisiDnevnik(DnevnikRada dnevnikRada) throws Exception {

//    pripremiResurs();

    naredbaDodavanje.setTimestamp(1, new Timestamp(dnevnikRada.vrijeme()));
    naredbaDodavanje.setString(2, dnevnikRada.korisnickoIme());
    naredbaDodavanje.setString(3, dnevnikRada.adresaRacunala());
    naredbaDodavanje.setString(4, dnevnikRada.ipAdresaRacunala());
    naredbaDodavanje.setString(5, dnevnikRada.nazivOS());
    naredbaDodavanje.setString(6, dnevnikRada.verzijaVM());
    naredbaDodavanje.setString(7, dnevnikRada.opisRada());

    boolean res = naredbaDodavanje.executeUpdate() == 1;

//    otpustiResurs();

    return res;

  }

  @Override
  public List<DnevnikRada> dohvatiDnevnik(long vrijemeOd, long vrijemeDo, String korisnickoIme)
      throws Exception {

    pripremiResurs();

    List<DnevnikRada> zapisi = new ArrayList<DnevnikRada>();

    naredbaPretrazivanje.setString(1, korisnickoIme);
    naredbaPretrazivanje.setTimestamp(2, new Timestamp(vrijemeOd));
    naredbaPretrazivanje.setTimestamp(3, new Timestamp(vrijemeDo));

    ResultSet rezultat = naredbaPretrazivanje.executeQuery();
    while (rezultat.next())
      zapisi.add(new DnevnikRada( //
          rezultat.getTimestamp(1).getTime(), //
          rezultat.getString(2), //
          rezultat.getString(3), //
          rezultat.getString(4), //
          rezultat.getString(5), //
          rezultat.getString(6), //
          rezultat.getString(7) //
      ));

    otpustiResurs();

    return zapisi;

  }

  @Override
  public boolean trebaDatoteku() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean trebaPodatkeBazaPodataka() {
    return true;
  }

}
