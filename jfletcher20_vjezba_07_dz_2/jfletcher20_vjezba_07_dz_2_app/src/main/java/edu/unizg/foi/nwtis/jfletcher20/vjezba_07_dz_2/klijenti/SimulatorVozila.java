package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.klijenti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa SimulatorVozila.
 */
public class SimulatorVozila {

  /**
   * Podaci o vozilima.
   */
  private RedPodaciVozila redPodaciVozila;
  /**
   * Datoteka s podacima vozila.
   */
  private String podaciVozilaDatoteka;
  /**
   * Executor servis.
   */
  private ExecutorService es;

  /**
   * Adresa vozila.
   */
  private String adresaVozila;
  /**
   * Mrežna vrata vozila.
   */
  private int mreznaVrataVozila;
  /**
   * Trajanje sekunde.
   */
  private int trajanjeSek;
  /**
   * Trajanje pauze.
   */
  private int trajanjePauze;

  /**
   * Id vozila.
   */
  private int idVozila;

  /**
   * Konstruktor za SimulatorVozila.
   */
  public SimulatorVozila() {}

  /**
   * Konstruktor za SimulatorVozila.
   * 
   * @param adresaVozila adresa vozila
   * @param mreznaVrataVozila mrežna vrata vozila
   * @param trajanjeSek trajanje sekunde
   * @param trajanjePauze trajanje pauze
   */
  public SimulatorVozila(String adresaVozila, int mreznaVrataVozila, int trajanjeSek,
      int trajanjePauze) {
    this.adresaVozila = adresaVozila;
    this.mreznaVrataVozila = mreznaVrataVozila;
    this.trajanjeSek = trajanjeSek;
    this.trajanjePauze = trajanjePauze;
  }

  /**
   * Inicijalizira simulator.
   * 
   * @param args argumenti naredbenog retka
   * @throws NeispravnaKonfiguracija baca se ako konfiguracija nije ispravna
   * @throws NumberFormatException baca se ako je broj neispravan
   * @throws UnknownHostException baca se ako je adresa nepoznata
   */
  private void inicijalizirajSimulator(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    preuzmiPostavke(args);
    redPodaciVozila = new RedPodaciVozila(mreznaVrataVozila);
    preuzmiPostavkeVozila(args);
    es = Executors.newVirtualThreadPerTaskExecutor();
  }

  /**
   * Glavna metoda.
   * 
   * @param args argumenti naredbenog retka
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Broj argumenata nije 3.");
      return;
    }
    SimulatorVozila simulator = new SimulatorVozila();
    try {
      simulator.inicijalizirajSimulator(args);
      AsynchronousSocketChannel kanalKlijenta = AsynchronousSocketChannel.open();
      var adresa = new InetSocketAddress(simulator.adresaVozila, simulator.mreznaVrataVozila);
      Future<Void> result = kanalKlijenta.connect(adresa);
      while (true) {
        try {
          Thread.sleep((long) (simulator.citajCSV() * simulator.trajanjeSek / 1000.0));
          result.get();
          simulator.simulirajVoznju(kanalKlijenta);
          Thread.sleep(simulator.trajanjePauze);
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
    } catch (NumberFormatException | NeispravnaKonfiguracija | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Simuliraj vožnju vozila.
   * 
   * @param kanalKlijenta kanal klijenta
   */
  private void simulirajVoznju(AsynchronousSocketChannel kanalKlijenta) {
    es.submit(() -> {
      try {
        var zahtjev = konstruirajZahtjev();
        byte[] sadrzaj = new String(zahtjev).getBytes();
        ByteBuffer bb = ByteBuffer.wrap(sadrzaj);
        Future<Integer> pisac = kanalKlijenta.write(bb);
        pisac.get();
        bb.clear();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }


  /**
   * Razlika izmedu vremena zadanog vozila i posljednjeg vozila dodanog u red.
   * 
   * @param podaci podaci nekog konkretnog vozila
   * @return razlika vremena posljednje dodanog vozila i onog proslijeđenog funkciji
   */
  private long razlikaVremena(PodaciVozila podaci) {
    PodaciVozila v = (PodaciVozila) redPodaciVozila.dajSvePodatkeVozila()
        .toArray()[redPodaciVozila.dajBrojPodatakaVozila() - 1];
    return podaci.vrijeme() - v.vrijeme();
  }

  /**
   * Konstruiraj zahtjev prema podacima vozila.
   * 
   * @return zahtjev u obliku VOZILO podatak podatak podatak ...
   */
  private String konstruirajZahtjev() {
    try {
      PodaciVozila p = (PodaciVozila) redPodaciVozila.dajSvePodatkeVozila()
          .toArray()[redPodaciVozila.dajBrojPodatakaVozila() - 1];
      return "VOZILO " //
          + p.id() + " " //
          + p.broj() + " " //
          + p.vrijeme() + " " //
          + p.brzina() + " " //
          + p.snaga() + " " //
          + p.struja() + " " //
          + p.visina() + " " //
          + p.gpsBrzina() + " " //
          + p.tempVozila() + " " //
          + p.postotakBaterija() + " " //
          + p.naponBaterija() + " " //
          + p.kapacitetBaterija() + " " //
          + p.tempBaterija() + " " //
          + p.preostaloKm() + " " //
          + p.ukupnoKm() + " " //
          + p.gpsSirina() + " " //
          + p.gpsDuzina() + "";
    } catch (Exception e) {
      return "NO DATA";
    }
  }

  /**
   * Preuzmi postavke simulatora.
   * 
   * @param args argumenti prema kojima se određuju postavke simulatora - args[0] datoteka s
   *        podacima simulatora i servera PosluziteljZaVozila
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   */
  public void preuzmiPostavke(String[] args) throws NeispravnaKonfiguracija, NumberFormatException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    adresaVozila = konfig.dajPostavku("adresaVozila");
    mreznaVrataVozila = Parsiraj.i(konfig.dajPostavku("mreznaVrataVozila"));
    trajanjeSek = Parsiraj.i(konfig.dajPostavku("trajanjeSek"));
    trajanjePauze = Parsiraj.i(konfig.dajPostavku("trajanjePauze"));
  }

  /**
   * Indeks retka u datoteci s podacima vozila koji se trenutno čita.
   */
  int brojRetka = 1;

  /**
   * Preuzmi postavke vozila.
   * 
   * @param args argumenti prema kojima se određuju postavke vozila - args[1] datoteka s podacima,
   *        args[2] id vozila
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   * @throws UnknownHostException
   */
  public void preuzmiPostavkeVozila(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    podaciVozilaDatoteka = args[1];
    idVozila = Parsiraj.i(args[2]);
  }

  /**
   * Čitanje podataka iz datoteke s podacima vozila. Svakim pozivom se čita novi redak iz datoteke.
   * 
   * @return razlika vremena između trenutnog i prethodnog vozila
   */
  public long citajCSV() {
    try (BufferedReader reader = new BufferedReader(new FileReader(podaciVozilaDatoteka))) {
      for (int i = 0; i < brojRetka; i++)
        reader.readLine();
      String row = reader.readLine();
      if (row == null)
        System.exit(0); // prekida program ako nema više redaka
      String[] data = row.split(",");
      var p = new PodaciVozila(idVozila, brojRetka++, Parsiraj.l(data[0]), Parsiraj.d(data[1]),
          Parsiraj.d(data[2]), Parsiraj.d(data[3]), Parsiraj.d(data[4]), Parsiraj.d(data[5]),
          Parsiraj.i(data[6]), Parsiraj.i(data[7]), Parsiraj.d(data[8]), Parsiraj.i(data[9]),
          Parsiraj.i(data[10]), Parsiraj.d(data[11]), Parsiraj.d(data[12]),
          Parsiraj.d(data[13]), Parsiraj.d(data[14]));
      long razlika = 0;
      if (redPodaciVozila.dajBrojPodatakaVozila() > 0)
        razlika = razlikaVremena(p);
      redPodaciVozila.dodajPodatakVozila(p);
      return razlika;
    } catch (IOException e) {
      e.printStackTrace();
      return 0;
    }
  }

}
