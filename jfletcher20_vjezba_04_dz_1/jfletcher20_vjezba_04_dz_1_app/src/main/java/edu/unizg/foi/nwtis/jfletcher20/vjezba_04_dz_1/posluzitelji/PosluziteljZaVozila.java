package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.posluzitelji.radnici.RadnikZaVozila;

/*
 * PosluziteljZaVozila otvara mrežnu utičnicu na zadanim mrežnim vratima i radi u višedretvenom
 * načinu rada s asinkronim slanjem/primanjem poruka putem kanala. PosluziteljZaVozila za svakog
 * klijenta (Simulator Voznje) kreira novu virtualnu dretvu koja izvršava objekt klase
 * RadnikZaVozila. U tom objektu se prima komanda i ispituje se njena ispravnost. Ispravna komanda
 * odlazi na obradu u kojoj se ispituje da li se e vozilo nalazi u području pojedinog radara. Ako je
 * u području radara, šalje se komanda poslužitelju Poslu ziteljRadara. U ovom obliku asinkronog
 * rada objekt RadnikZaVozila ne šalje odgovor svom klijentu. 3 Predmet: Napredne Web tehnologije i
 * servisi Ak. god.: 2023./2024. Za poslužitelja PosluziteljZaVozila ulogu klijenta ima program
 * SimulatorVozila. On se spaja na mrežnu uti čnicu od PosluziteljZaVozila, otvara datoteku s
 * podacima za vožnju e-vozila, čita redak po redak, prim prema podatke za komandu (dodaje id vozila
 * i redni broj retka) i asinkrono šalje komandu bez da čeka na odgovor.
 */

public class PosluziteljZaVozila implements Runnable {
  private int mreznaVrata;
  private CentralniSustav cs;
  private ExecutorService es;
  /*
   * Klijent se spaja se na PosluziteljZaVozila putem mrežne utičnice i šalje komandu (završava s
   * \n) poslužitelju na temelju postavki i traži izvršavanja određene akcije i vraća odgovor
   * (završava s \n): • VOZILO id broj vrijeme brzina snaga struja visina gpsBrzina tempVozila
   * postotakBaterija naponBaterija kapacitetBaterija tempBaterija preostaloKm ukupnoKm gpsSirina
   * gpsDuzina o npr. VOZILO 1 101 1708073749078 0.02 0.8086 0.02 214.2 1.337297 19 93 40.43 7314 20
   * 27.9 816.458 46.286644 16.35285 o Provjera da li ispravni podaci. Ako su ispravni provjerava da
   * li se nalazi u dosegu radara. Ako se nalazi tada se poslužitelju PosluziteljRadara šalju podaci
   * o vožnji i vraća OK. o Npr. OK Kodovi pogrešaka su: o Kada format komande nije ispravan, vraća
   * odgovor ERROR 20 tekst (tekst objašnjava razlog pogreške) o Kada nešto drugo nije u redu vraća
   * odgovor ERROR 29 tekst (tekst objašnjava razlog po greške).
   * 
   * primjer poziva> VOZILO 1 101 1708073749078 0.02 0.8086 0.02 214.2 1.337297 19 93 40.43 7314 20
   */


  public PosluziteljZaVozila(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.cs = centralniSustav;
  }

  @Override
  public void run() {
    pokreniPosluzitelja();
  }

  private void pokreniPosluzitelja() {
    es = Executors.newVirtualThreadPerTaskExecutor();
    AsynchronousServerSocketChannel ass = null;
    try {
      ass = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(this.mreznaVrata));
      while (true)
        es.execute(new RadnikZaVozila(ass.accept().get(), cs));
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      ass.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
