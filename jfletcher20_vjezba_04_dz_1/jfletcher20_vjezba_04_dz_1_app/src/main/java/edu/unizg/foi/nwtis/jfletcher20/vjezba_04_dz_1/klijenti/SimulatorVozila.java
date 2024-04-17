package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.klijenti;

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
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.podaci.RedPodaciVozila;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/*
 * Opis rada sustava: Sustav je zamišljen na simulira vožnju e-vozilom po nekom gradu. Simulacija
 * vožnje e-vozila temelji se na stvarnim podacima koji su zapisani u csv datotekama. Datoteka
 * podataka za vožnju e-vozila sadrži poda tke u redcima, a nazivi njihovih stupaca nalaze se u 1.
 * retku u datoteci: Milliseconds since Epoch Speed Watt Ampere Altitude GPS Speed Vehicle Body
 * Temperature Battery Percentage Battery Voltage Battery Capacity Battery Temperature Remaining
 * Mileage Total Mileage Latitude Longitude
 * 
 * npr. Milliseconds since Epoch,Speed,Watt,Ampere,Altitude,GPS Speed,Vehicle Body Temperature
 * ,Battery Percentage,Battery Voltage,Battery Capacity,Battery Temperature,Remaining Mileage,Total
 * Mileage,Latitude,Longitude
 * 1708073749078,0.02,0.8086,0.02,214.2,1.337297,19,93,40.43,7314,20,27.9,816.458,46.286644,16.35285
 * 1708073749394,0.358,0.8086,0.02,214.2,1.337297,19,93,40.43,7314,20,27.9,816.458,46.286644,16.
 * 35285
 * 1708073749756,2.149,0.8086,0.02,214.2,1.337297,19,93,40.43,7314,20,27.9,816.458,46.286644,16.
 * 35285
 * 1708073750092,0.947,0.8086,0.02,214.2,1.337297,19,93,40.43,7314,20,27.9,816.458,46.286644,16.
 * 35285
 * 1708073750654,0.575,0.8086,0.02,214.2,1.337297,19,93,40.43,7314,20,27.9,816.458,46.286644,16.
 * 35285
 * 
 * 
 * Podaci u datoteci odgovaraju zapisu PodaciVozila uz dva dodana podatka (id i broj) . Potrebno je
 * obraditi pažnju na podatke tipa double jer se mogu javiti cjelobrojne vrijednosti u tim stupcima.
 * Npr. u 7. retku u B stupcu pod nazivom Speed nalazi se vrijednosti 0 (nema decimalnu točku). To
 * se posebno odnosi na pos lužiteljske klase (i njihove radnike) koje primaju podatke o vozilima te
 * trebaju ispravnost primljenih poda taka. Treba predvidjeti da podatak kod tipa double može biti
 * sa ali i bez decimalne točke i da treba u oba slučaja ispravno preuzeti vrijednost.
 * 
 * Podaci u datoteci bilježe se u vrlo kratkim intervalima (manje od 1 sek) tako da se može pratili
 * vožnja e vozilom pa određenom putanji. Podaci u datoteci tretiraju se kao „živi“ podaci koje
 * e-vozilo (tj. simulator vožnje) šalje podatke svakog pojedinog retka, jedan po jedan, prema
 * sustavu za telemetriju i kontrolu brzine. Za svaki redak utvrđuje se razlika između njegovog
 * vremena i vremena prethodnog retka. Ta razlika vremena množi se sa korekcijom vremena, koja se
 * dobije dijeljenjem postavke trajanjeSek s 1000.0. Pos tavka trajanjeSek sadrži broj milisekundi.
 * Dobivena vrijednost je argument za funkciju Thread.sleep kojom dretva spava određeno vrijeme i
 * nakon toga nastavlja s radom na podacima iz retka i zatim spava koliko iznosi postavka
 * trajanjePauze. Ako je vrijednost za trajanjeSek manje od 1000 to znači da će simulacija raditi
 * brže nego u stvarnosti. Npr. ako trajanjeSek ima vrijednost 10 to znači da će simulacija raditi
 * gotovo 100 puta brže od stvarnosti. Npr. ako trajanjeSek ima vrijednost 10000 to znači da će
 * simulacija raditi gotovo 10 puta sporije od stvarnosti.
 */

/*
 * PosluziteljZaVozila otvara mrežnu utičnicu na zadanim mrežnim vratima i radi u višedretvenom
 * načinu rada s asinkronim slanjem/primanjem poruka putem kanala. PosluziteljZaVozila za svakog
 * klijenta (Simulator Voznje) kreira novu virtualnu dretvu koja izvršava objekt klase
 * RadnikZaVozila. U tom objektu se prima komanda i ispituje se njena ispravnost. Ispravna komanda
 * odlazi na obradu u kojoj se ispituje da li se e vozilo nalazi u području pojedinog radara. Ako je
 * u području radara, šalje se komanda poslužitelju Poslu ziteljRadara. U ovom obliku asinkronog
 * rada objekt RadnikZaVozila ne šalje odgovor svom klijentu.
 */

/*
 * Za poslužitelja PosluziteljZaVozila ulogu klijenta ima program SimulatorVozila. On se spaja na
 * mrežnu uti čnicu od PosluziteljZaVozila, otvara datoteku s podacima za vožnju e-vozila, čita
 * redak po redak, prim prema podatke za komandu (dodaje id vozila i redni broj retka) i asinkrono
 * šalje komandu bez da čeka na odgovor.
 */

public class SimulatorVozila {

  /*
   * Pokretanje programa SimulacijaVozila sadrži tri argumenta: datoteka1.(txt | xml | bin | json)
   * datoteka2.csv id Npr. NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 Putem mrežne utičnice spaja se na
   * PosluziteljZaVozila i asinkrono šalje komanda poslužitelju putem ka nala na temelju postavki i
   * traži izvršavanja određene akcije. Ne čeka odgovor od poslužitelja kod slanja podataka.
   */

  private RedPodaciVozila redPodaciVozila;
  private String podaciVozilaDatoteka;
  private ExecutorService es;

  private String adresaVozila;
  private int mreznaVrataVozila;
  private int trajanjeSek;
  private int trajanjePauze;

  private int idVozila;

  public SimulatorVozila() {}

  public SimulatorVozila(String adresaVozila, int mreznaVrataVozila, int trajanjeSek,
      int trajanjePauze) {
    this.adresaVozila = adresaVozila;
    this.mreznaVrataVozila = mreznaVrataVozila;
    this.trajanjeSek = trajanjeSek;
    this.trajanjePauze = trajanjePauze;
  }

  private void inicijalizirajSimulator(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    preuzmiPostavke(args);
    redPodaciVozila = new RedPodaciVozila(mreznaVrataVozila);
    preuzmiPostavkeVozila(args);
    es = Executors.newVirtualThreadPerTaskExecutor();
  }

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
          Thread.sleep((long) (simulator.citajCSV() * simulator.korekcijaVremena()));
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

  private void simulirajVoznju(AsynchronousSocketChannel kanalKlijenta) {
    es.submit(() -> {
      try {
        var zahtjev = konstruirajZahtjev();
        byte[] sadrzaj = new String(zahtjev).getBytes();
        ByteBuffer bb = ByteBuffer.wrap(sadrzaj);
        Future<Integer> pisac = kanalKlijenta.write(bb);
        pisac.get();
        System.out.println(zahtjev);
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

  private double korekcijaVremena() {
    return trajanjeSek / 1000.0;
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

  private double _dp(String value) {
    return Double.parseDouble(value);
  }

  private long _lp(String value) {
    return Long.parseLong(value);
  }

  private int _ip(String value) {
    return Integer.parseInt(value);
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
    mreznaVrataVozila = _ip(konfig.dajPostavku("mreznaVrataVozila"));
    trajanjeSek = _ip(konfig.dajPostavku("trajanjeSek"));
    trajanjePauze = _ip(konfig.dajPostavku("trajanjePauze"));
  }

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
    idVozila = _ip(args[2]);
  }

  public long citajCSV() {
    try (BufferedReader reader = new BufferedReader(new FileReader(podaciVozilaDatoteka))) {
      for (int i = 0; i < brojRetka; i++)
        reader.readLine();
      String row = reader.readLine();
      if (row == null)
        System.exit(0); // prekida program ako nema više redaka
      String[] data = row.split(",");
      var p = new PodaciVozila(idVozila, brojRetka++, _lp(data[0]), _dp(data[1]), _dp(data[2]),
          _dp(data[3]), _dp(data[4]), _dp(data[5]), _ip(data[6]), _ip(data[7]), _dp(data[8]),
          _ip(data[9]), _ip(data[10]), _dp(data[11]), _dp(data[12]), _dp(data[13]), _dp(data[14]));
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
