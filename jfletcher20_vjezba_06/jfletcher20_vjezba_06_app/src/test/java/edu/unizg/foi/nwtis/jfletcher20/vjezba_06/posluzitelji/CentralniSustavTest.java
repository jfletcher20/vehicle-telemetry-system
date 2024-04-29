package edu.unizg.foi.nwtis.jfletcher20.vjezba_06.posluzitelji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.net.UnknownHostException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_06.posluzitelji.CentralniSustav;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Testovi za klasu PosluziteljKazni.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CentralniSustavTest {
  /**
   * Centralni sustav.
   */
  CentralniSustav centralniSustav;

  /**
   * Priprema za testiranje.
   * 
   * @throws Exception
   */
  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  /**
   * Završetak testiranja.
   * 
   * @throws Exception
   */
  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  /**
   * Priprema za testiranje.
   * 
   * @throws Exception
   */
  @BeforeEach
  void setUp() throws Exception {
    centralniSustav = new CentralniSustav();
  }

  /**
   * Završetak testiranja.
   * 
   * @throws Exception
   */
  @AfterEach
  void tearDown() throws Exception {
    centralniSustav = null;
  }

  /**
   * Testiranje metode main.
   */
  @Test
  @Order(3)
  void testMain() {
    Thread dretva = null;
    try {
      dretva = new Thread(() -> CentralniSustav.main(new String[] {"NWTiS_DZ1_CS.txt"}));
      dretva.start();
    } catch (Exception e) {
      dretva.interrupt();
      fail("Exception je uhvacen.");
    }
    assertTrue(true);
    dretva.interrupt();
  }

  /**
   * Testiranje metode pokreniPosluzitelja.
   */
  @Test
  @Order(2)
  void testPokreniPosluzitelja() {

    Thread dretva = null;
    try {
      dretva = new Thread(() -> centralniSustav.pokreniPosluzitelje());
      dretva.start();
    } catch (Exception e) {
      dretva.interrupt();
      fail("Exception je uhvacen.");
    }
    assertTrue(true);
    dretva.interrupt();
  }

  /**
   * Testiranje metode preuzmiPostavke.
   */
  @Test
  @Order(1)
  void testPreuzmiPostavke() {
    var nazivDatoteke = "CentralniPosluzitelj.txt";
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrata", "8000");
      k.spremiKonfiguraciju();
      String[] argumenti = {nazivDatoteke};
      this.centralniSustav.preuzmiPostavke(argumenti);
      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrata")).intValue(), 8000);
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      e.printStackTrace();
    }
    this.obrisiDatoteku(nazivDatoteke);
  }

  /**
   * Briše datoteku ako postoji.
   * 
   * @param nazivDatoteke Naziv datoteke koja se briše.
   */
  private boolean obrisiDatoteku(String nazivDatoteke) {
    File f = new File(nazivDatoteke);

    if (!f.exists()) {
      return true;
    } else if (f.exists() && f.isFile()) {
      f.delete();
      if (!f.exists()) {
        return true;
      }
    }
    return false;
  }

}
