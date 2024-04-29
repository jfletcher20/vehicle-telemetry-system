package edu.unizg.foi.nwtis.jfletcher20.vjezba_06.podaci;

import edu.unizg.foi.nwtis.jfletcher20.vjezba_06.pomocnici.Parsiraj;

/**
 * Zapis BrzoVozilo.
 *
 * @param id identifikator e-vozila
 * @param broj broj retka iz datoteke podataka koju SimulatorVozila koristi u radu
 * @param vrijeme vrijeme nastanka podatka u retku
 * @param brzina brzina e-vozila u vrijeme
 * @param gpsSirina gps širina pozicije e-vozila u vrijeme
 * @param gpsDuzina gps dužina pozicije e-vozila u vrijeme
 * @param status status zapisa. true početak brze vožnje, false nije brza vožnja
 */
public record BrzoVozilo(int id, int broj, long vrijeme, double brzina, double gpsSirina,
    double gpsDuzina, boolean status) {

  /**
   * Konstruktor koji parsira podatke iz stringa.
   * 
   * @param id identifikator e-vozila
   * @param broj broj retka iz datoteke podataka koju SimulatorVozila koristi u radu
   * @param vrijeme vrijeme nastanka podatka u retku
   * @param brzina brzina e-vozila u vrijeme
   * @param gpsSirina gps širina pozicije e-vozila u vrijeme
   * @param gpsDuzina gps dužina pozicije e-vozila u vrijeme
   * @param status status zapisa. true početak brze vožnje, false nije brza vožnja
   */
  public BrzoVozilo(String id, int broj, String vrijeme, String brzina, String gpsSirina,
      String gpsDuzina, boolean status) {
    this(Parsiraj.pi(id), broj, Parsiraj.pl(vrijeme), Parsiraj.pd(brzina), Parsiraj.pd(gpsSirina),
        Parsiraj.pd(gpsDuzina), status);
  }

  /**
   * Postavi status.
   *
   * @param status status zapisa
   * @return novo brzo vozilo
   */
  public BrzoVozilo postaviStatus(Boolean status) {
    return new BrzoVozilo(id(), broj(), vrijeme(), brzina(), gpsSirina(), gpsDuzina(), status);
  }

}
