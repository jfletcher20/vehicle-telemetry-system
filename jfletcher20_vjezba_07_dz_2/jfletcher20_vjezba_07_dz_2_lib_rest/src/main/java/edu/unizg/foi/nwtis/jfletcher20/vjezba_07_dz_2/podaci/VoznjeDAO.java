package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa VoznjeDAO za rad s tablicom Voznje
 *
 * @author Joshua Lee Fletcher
 */
public class VoznjeDAO {

  /**
   * Veza na bazu podataka.
   */
  private Connection vezaBP;

  /**
   * Instantiates a new voznje DAO.
   *
   * @param vezaBP veza na bazu podataka
   */
  public VoznjeDAO(Connection vezaBP) {
    super();
    this.vezaBP = vezaBP;
  }
  
  /**
   * Dohvati sve voznje.
   * 
   * @return lista voznji
   */
  public List<Voznja> dohvatiSveVoznje() {
    String upit = "SELECT * FROM voznje";

    List<Voznja> voznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVoznja(rs);
        voznje.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(VoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return voznje;
  }

  /**
   * Dohvati voznje u intervalu.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return lista voznji
   */
  public List<Voznja> dohvatiVoznje(long odVremena, long doVremena) {
    String upit = "SELECT * FROM voznje WHERE vrijeme >= ? AND vrijeme <= ?";

    List<Voznja> voznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setLong(1, odVremena);
      s.setLong(2, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVoznja(rs);
        voznje.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(VoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return voznje;
  }

  /**
   * Dohvati kazne vozila.
   *
   * @param id id vozila
   * @return lista kazni
   */
  public List<Voznja> dohvatiVoznjeVozila(int id) {
    String upit = "SELECT * FROM voznje WHERE id = ?";

    List<Voznja> voznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVoznja(rs);
        voznje.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(VoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return voznje;
  }


  /**
   * Dohvati voznje s konkretnim identifikatorom u intervalu.
   *
   * @param odVremena početak interva
   * @param doVremena kraj interva
   * @return lista kazni
   */
  public List<Voznja> dohvatiVoznjeVozila(int id, long odVremena, long doVremena) {
    String upit = "SELECT * FROM voznje WHERE id = ? AND vrijeme >= ? AND vrijeme <= ?";

    List<Voznja> voznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      s.setLong(2, odVremena);
      s.setLong(3, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVoznja(rs);
        voznje.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(VoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return voznje;
  }

  /**
   * Dodaj voznju.
   *
   * @param voznja voznja
   * @return true, ako je uspješno dodavanje
   */
  public boolean dodajVoznju(Voznja voznja) {
    String upit = "INSERT INTO voznje (id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina) "
         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {

      s.setInt(1, voznja.getId());
      s.setInt(2, voznja.getBroj());
      s.setLong(3, voznja.getVrijeme());
      s.setDouble(4, voznja.getBrzina());
      s.setDouble(5, voznja.getSnaga());
      s.setDouble(6, voznja.getStruja());
      s.setDouble(7, voznja.getVisina());
      s.setDouble(8, voznja.getGpsBrzina());
      s.setInt(9, voznja.getTempVozila());
      s.setInt(10, voznja.getPostotakBaterija());
      s.setDouble(11, voznja.getNaponBaterija());
      s.setInt(12, voznja.getKapacitetBaterija());
      s.setInt(13, voznja.getTempBaterija());
      s.setDouble(14, voznja.getPreostaloKm());
      s.setDouble(15, voznja.getUkupnoKm());
      s.setDouble(16, voznja.getGpsSirina());
      s.setDouble(17, voznja.getGpsDuzina());
      
      int brojAzuriranja = s.executeUpdate();

      return brojAzuriranja == 1;

    } catch (Exception ex) {
      Logger.getLogger(VoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  /**
   * Kreiraj objekt voznja.
   *
   * @param rs skup rezultata SQL upisa
   * @return voznja
   * @throws SQLException SQL iznimka
   */
  private Voznja kreirajObjektVoznja(ResultSet rs) throws SQLException {

    int id = rs.getInt("id");
    int broj = rs.getInt("broj");
    long vrijeme = rs.getLong("vrijeme");
    double brzina = rs.getDouble("brzina");
    double snaga = rs.getDouble("snaga");
    double struja = rs.getDouble("struja");
    double visina = rs.getDouble("visina");
    double gpsBrzina = rs.getDouble("gpsBrzina");
    int tempVozila = rs.getInt("tempVozila");
    int postotakBaterija = rs.getInt("postotakBaterija");
    double naponBaterija = rs.getDouble("naponBaterija");
    int kapacitetBaterija = rs.getInt("kapacitetBaterija");
    int tempBaterija = rs.getInt("tempBaterija");
    double preostaloKm = rs.getDouble("preostaloKm");
    double ukupnoKm = rs.getDouble("ukupnoKm");
    double gpsSirina = rs.getDouble("gpsSirina");
    double gpsDuzina = rs.getDouble("gpsDuzina");

    Voznja v = new Voznja(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,
        postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,
        gpsSirina, gpsDuzina);

    return v;
  }

}
