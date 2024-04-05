package edu.unizg.foi.nwtis.jfletcher20.vjezba_03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Klasa ObradaDirektorija.
 */
public class ObradaDirektorija implements Runnable {

  /** vjezba 03. */
  private Vjezba_03 vjezba_03;

  /** broj dretvi. */
  private static int brojDretvi = 0;

  /** redni broj dretve. */
  private int redniBrojDretve;

  /** naziv dretve. */
  private String nazivDretve;

  /** razina. */
  private int razina;

  /** pocetna putanja. */
  private Path pocetnaPutanja;

  /** ukupan broj javljanja. */
  private int ukupanBrojJavljanja = 0;

  /**
   * Instancira novu obradu direktorija.
   *
   * @param vjezba_03 vjezba 03
   * @param razina razina
   * @param pocetnaPutanja pocetna putanja
   */
  public ObradaDirektorija(Vjezba_03 vjezba_03, int razina, Path pocetnaPutanja) {
    super();
    this.vjezba_03 = vjezba_03;
    this.redniBrojDretve = brojDretvi++;
    this.nazivDretve = "nmidzic20-" + this.redniBrojDretve;
    this.razina = razina;
    this.pocetnaPutanja = pocetnaPutanja;
    this.vjezba_03.brojAktivnihDretvi.incrementAndGet();

  }

  /**
   * Vraca naziv dretve.
   *
   * @return naziv dretve
   */
  public String getNazivDretve() {
    return nazivDretve;
  }

  /**
   * Postavlja naziv dretve.
   *
   * @param nazivDretve naziv dretve
   */
  public void setNazivDretve(String nazivDretve) {
    this.nazivDretve = nazivDretve;
  }

  /**
   * Run.
   */
  @Override
  public void run() {
    try {
      this.pretraziDirektorij(this.pocetnaPutanja, razina);
      this.vjezba_03.ukupanBrojRijeci.addAndGet(this.ukupanBrojJavljanja);

      if ((this.vjezba_03.vrstaIspisa == 0 || this.vjezba_03.vrstaIspisa == 2)
          && this.vjezba_03.ukupanBrojRijeci.get() > 0)
        System.out.println("Dretva: " + this.redniBrojDretve + " - Direktorij: "
            + this.pocetnaPutanja + " - Broj javljanja: " + this.ukupanBrojJavljanja);

      Thread.sleep(this.vjezba_03.pauzaDretve);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    synchronized (this.vjezba_03) {
      var brojAktivnihDretvi = this.vjezba_03.brojAktivnihDretvi.decrementAndGet();
      if (brojAktivnihDretvi <= this.vjezba_03.maksDretvi) {
        this.vjezba_03.notify();
      }
    }
  }

  /**
   * Pretrazi direktorij.
   *
   * @param radnaPutanja radna putanja
   * @param radnaRazina radna razina
   * @throws IOException signalizira da se dogodila I/O iznimka.
   */
  private void pretraziDirektorij(Path radnaPutanja, int radnaRazina) throws IOException {

    if (radnaRazina > this.vjezba_03.maksDubina) {
      return;
    }
    var elementi = Files.list(radnaPutanja);

    for (var i = elementi.iterator(); i.hasNext();) {
      var element = i.next();
      if (Files.isDirectory(element) && Files.isReadable(element)) {
        this.pretraziDirektorij(element, radnaRazina++);
      } else if (Files.isRegularFile(element) && Files.isReadable(element)) {
        this.ukupanBrojJavljanja += this.vjezba_03.pretraziDatoteku(element, this.redniBrojDretve);
      }
    }
    elementi.close();

  }

}
