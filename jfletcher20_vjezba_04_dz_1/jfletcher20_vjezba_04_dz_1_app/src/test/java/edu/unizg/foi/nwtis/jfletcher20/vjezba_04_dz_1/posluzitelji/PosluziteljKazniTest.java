package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciKazne;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Testovi za klasu PosluziteljKazni.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PosluziteljKazniTest {
  /**
   * Poslužitelj kazni.
   */
  PosluziteljKazni posluziteljKazni;

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
    posluziteljKazni = new PosluziteljKazni();
  }

  /**
   * Završetak testiranja.
   * 
   * @throws Exception
   */
  @AfterEach
  void tearDown() throws Exception {
    posluziteljKazni = null;
  }

  /**
   * Testira metodu main.
   */
  @Test
  @Order(5)
  void testMain() {
    var status = false;
    var mreznaVrata = 8020;
    this.posluziteljKazni.mreznaVrata = mreznaVrata;
    try {
      InetSocketAddress isa = new InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata);
      Socket s = new Socket();
      s.connect(isa, 70);
      s.close();
    } catch (Exception e) {
      status = true;
    }
    assertTrue(status);

    var nazivDatoteke = "PosluziteljKazni.txt";
    String[] argumenti = {nazivDatoteke};
    this.posluziteljKazni.mreznaVrata = mreznaVrata;
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrataKazne", Integer.toString(this.posluziteljKazni.mreznaVrata));
      k.spremiKonfiguraciju();
    } catch (NeispravnaKonfiguracija | NumberFormatException e) {
      e.printStackTrace();
    }

    var dretva = Thread.ofVirtual().factory().newThread(() -> PosluziteljKazni.main(argumenti));
    dretva.start();

    status = true;
    this.posluziteljKazni.mreznaVrata = mreznaVrata;
    try {
      Thread.sleep(100);
      InetSocketAddress isa = new InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata);
      Socket s = new Socket();
      s.connect(isa, 70);
      s.close();
    } catch (Exception e) {
      status = false;
    }
    assertTrue(status);
    dretva.interrupt();
    this.obrisiDatoteku(nazivDatoteke);
  }

  /**
   * Testira metodu pokreniPosluzitelja.
   */
  @Test
  @Order(4)
  void testPokreniPosluzitelja() {
    try {
      var dretva =
          Thread.ofVirtual().factory().newThread(() -> posluziteljKazni.pokreniPosluzitelja());
      dretva.start();
      dretva.interrupt();
    } catch (Exception e) {
      fail("Exception je uhvaćen.");
    }
    assertTrue(true);
  }

  /**
   * Testira metodu obradaZahtjeva.
   */
  @Test
  @Order(7)
  void testObradaZahtjevaDohvatiKaznu() {
    var testZahtjev = "VOZILO 1 1620000000 1620000001 100.0 45.0 45.0 45.0 45.0";
    posluziteljKazni.obradaZahtjevaDohvatiKaznu(testZahtjev);
    testZahtjev = "VOZILO 2 1619999999 1620000002";
    assertTrue(posluziteljKazni.obradaZahtjeva(testZahtjev).contains("ERROR"));
  }

  /**
   * Testira metodu obradaZahtjeva.
   */
  @Test
  @Order(6)
  void testObradaZahtjevaStatistike() {
    var testZahtjev = "VOZILO 1 1620000000 1620000001 100.0 45.0 45.0 45.0 45.0";
    posluziteljKazni.obradaZahtjeva(testZahtjev);
    testZahtjev = "STATISTIKA 1619999999 1620000002";
    assertTrue(posluziteljKazni.obradaZahtjevaStatistika(testZahtjev).contains("OK"));
  }

  /**
   * Testira metodu obradaZahtjeva.
   */
  @Test
  @Order(3)
  void testObradaZahtjeva() {
    var testZahtjev = "VOZILO 1 1620000000 1620000001 100.0 45.0 45.0 45.0 45.0";
    var testOdgovor = "OK";
    assertTrue(posluziteljKazni.obradaZahtjeva(testZahtjev).contains(testOdgovor));
  }

  /**
   * Testira metodu obradaZahtjeva za kaznu.
   */
  @Test
  @Order(2)
  void testObradaZahtjevaKazna() {
    var kazna = new PodaciKazne(1, 1620000000, 1620000001, 100.0, 45.0, 45.0, 45.0, 45.0);
    var testZahtjev = "VOZILO 1 1620000000 1620000001 100.0 45.0 45.0 45.0 45.0";
    posluziteljKazni.obradaZahtjeva(testZahtjev);
    assertEquals(kazna.id() + kazna.brzina() + kazna.vrijemeKraj() + kazna.vrijemePocetak(),
        posluziteljKazni.sveKazne.peek().id() + posluziteljKazni.sveKazne.peek().brzina()
            + posluziteljKazni.sveKazne.peek().vrijemeKraj()
            + posluziteljKazni.sveKazne.peek().vrijemePocetak());
  }

  /**
   * Testira metodu preuzmiPostavke.
   */
  @Test
  @Order(1)
  void testPreuzmiPostavke() {
    var nazivDatoteke = "PosluziteljKazni.txt";
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrataKazne", "8020");
      k.spremiKonfiguraciju();
      String[] argumenti = {nazivDatoteke};
      this.posluziteljKazni.preuzmiPostavke(argumenti);
      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrataKazne")).intValue(),
          this.posluziteljKazni.mreznaVrata);
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
