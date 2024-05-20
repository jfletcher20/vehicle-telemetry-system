package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.posluzitelji;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.posluzitelji.radnici.RadnikZaVozila;

/**
 * Klasa PosluziteljZaVozila
 */
public class PosluziteljZaVozila implements Runnable {

  /**
   * Mrežna vrata poslužitelja
   */
  private int mreznaVrata;

  /**
   * Referenca na centralni sustav
   */
  private CentralniSustav cs;

  /**
   * ExecutorService za izvršavanje zadataka
   */
  private ExecutorService es;

  /**
   * Konstruktor klase PosluziteljZaVozila
   * 
   * @param mreznaVrata Mrežna vrata poslužitelja
   * @param centralniSustav Referenca na centralni sustav
   */
  public PosluziteljZaVozila(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.cs = centralniSustav;
  }

  /**
   * Metoda koja pokreće radnike za vozila za obradu zahtjeva poslužitelja.
   */
  @Override
  public void run() {
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
