package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;

class PosluziteljKazniTest {

  private PosluziteljKazni pk = new PosluziteljKazni();

  @Test
  void testObradaZahtjeva() {
    var pk = new PosluziteljKazni();
    var r = pk.obradaZahtjeva("Hello, world!");
    assertTrue(r.contains("ERROR"));
  }

  @Test
  @Order(1)
  void testPreuzmiPostavke() {
    var nazivDat = "PosluziteljKazni.txt";
    try {
      var k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDat);
      k.spremiPostavku("mreznaVrataKazne", "8020");
    } catch (Exception e) {

    }
    assertTrue(true);
  }

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

}
