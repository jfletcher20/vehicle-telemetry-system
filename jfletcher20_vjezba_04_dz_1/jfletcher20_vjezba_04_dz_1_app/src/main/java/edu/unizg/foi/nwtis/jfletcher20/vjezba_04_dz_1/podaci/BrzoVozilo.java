package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci;

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

  private static Integer _pi(String value) {
    return Integer.parseInt(value);
  }

  private static Double _pd(String value) {
    return Double.parseDouble(value);
  }

  private static Long _pl(String value) {
    return Long.parseLong(value);
  }

  public BrzoVozilo(String group, int i, String group2, String group3, String group4, String group5,
      boolean b) {
    this(_pi(group), i, _pl(group2), _pd(group3), _pd(group4), _pd(group5), b);
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
