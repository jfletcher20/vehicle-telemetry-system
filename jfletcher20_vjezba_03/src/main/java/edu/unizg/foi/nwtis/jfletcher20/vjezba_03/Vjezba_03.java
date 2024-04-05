package edu.unizg.foi.nwtis.jfletcher20.vjezba_03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa Vjezba_03.
 */
public class Vjezba_03 {

  /** sdf. */
  static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy");

  /** ukupan broj rijeci. */
  public volatile AtomicInteger ukupanBrojRijeci = new AtomicInteger(0);

  /** maksimalna dubina. */
  public int maksDubina;

  /** pauza dretve. */
  public int pauzaDretve;

  /** maks dretvi. */
  public int maksDretvi;

  /** vrsta ispisa. */
  public int vrstaIspisa;

  /** pocetna putanja. */
  private String pocetnaPutanja;

  /** vrsta trazenja. */
  private int vrstaTrazenja;

  /** vrsta dretvi. */
  private int vrstaDretvi;

  /** predlozak naziva datoteke. */
  private String predlozakNazivaDatoteke;

  /** trazena rijec. */
  private String trazenaRijec;

  /** predlozak. */
  private Pattern predlozak;

  /** broj aktivnih dretvi. */
  public volatile AtomicInteger brojAktivnihDretvi = new AtomicInteger(0);

  /** kreator dretvi. */
  private Thread.Builder kreatorDretvi;

  /** tvornica dretvi. */
  private ThreadFactory tvornicaDretvi;

  /** lista dretvi. */
  private ConcurrentLinkedQueue<Thread> listaDretvi = new ConcurrentLinkedQueue<Thread>();

  /** lista neobradjenih direktorija. */
  private ConcurrentLinkedQueue<Path> listaNeobradjenihDirektorija =
      new ConcurrentLinkedQueue<Path>();

  /**
   * Stvara instancu vjezba 03.
   */
  public Vjezba_03() {}

  /**
   * Glavna metoda.
   *
   * @param args ulazni argumenti od 3 do 4, 1. je naziv datoteke konfiguracije, 2. je predlozak
   *        naziva datoteke, 3. je rijec koju trazimo, 4. je opcionalan i pocetni direktorij
   *        pretrage
   * @throws IOException -signalizira da se I/O iznimka dogodila.
   * @throws InterruptedException iznimka prekidanja
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    if (args.length < 3 || args.length > 4) {
      System.out.println("Broj argumenata nije u rasponu 3 - 4.");
      return;
    }

    var vjezba_03 = new Vjezba_03();

    if (args.length == 4) {
      vjezba_03.pocetnaPutanja = args[3];
    } else {
      vjezba_03.pocetnaPutanja = ".";
    }

    vjezba_03.predlozakNazivaDatoteke = args[1];
    vjezba_03.trazenaRijec = args[2];
    var radniPredlozak =
        vjezba_03.predlozakNazivaDatoteke.replace(".", "\\.").replaceAll("\\*", ".+");
    vjezba_03.predlozak = Pattern.compile(radniPredlozak);
    // ulaz: NWTiS_0*.t* potreban regex: NWTiS_0.+\\.t.+
    vjezba_03.preuzmiPostavke(args);
    vjezba_03.pretrazivanjeStruktureDirektorija(args);
    vjezba_03.pretrazivanjeNeobradjenihDirektorija();

    if (!vjezba_03.listaDretvi.isEmpty()) {
      for (Thread dretva : vjezba_03.listaDretvi) {
        if (dretva.isAlive()) {
          dretva.join();
        }
      }
    }

    System.out.println("Ukupan broj pojave riječi: " + vjezba_03.ukupanBrojRijeci.get());

  }

  /**
   * Pretrazi datoteku.
   *
   * @param datoteka datoteka
   * @param brojDretve broj dretve
   * @return int
   */
  public int pretraziDatoteku(Path datoteka, int brojDretve) {
    var poklapanje = this.predlozak.matcher(datoteka.getFileName().toString());
    var status = poklapanje.matches();
    if (!status) {
      return 0;
    }
    int brojJavljanja = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(datoteka.toFile()))) {
      String redak;
      while ((redak = reader.readLine()) != null) {
        int pozicija = 0;
        if (this.vrstaTrazenja == 0) {
          String redakMalaSlova = redak.toLowerCase();
          String trazenaRijecMalaSlova = this.trazenaRijec.toLowerCase();
          while ((pozicija = redakMalaSlova.indexOf(trazenaRijecMalaSlova, pozicija)) != -1) {
            brojJavljanja++;
            pozicija += trazenaRijecMalaSlova.length();
          }
        } else {
          while ((pozicija = redak.indexOf(this.trazenaRijec, pozicija)) != -1) {
            brojJavljanja++;
            pozicija += this.trazenaRijec.length();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    switch (this.vrstaIspisa) {
      case 1:
      case 2:
        this.ispisiDatotekaBrojJavljanja(brojDretve, datoteka, brojJavljanja);
        break;
    }

    return brojJavljanja;
  }



  /**
   * Ispisi datoteka broj javljanja.
   *
   * @param brojDretve broj dretve
   * @param datoteka datoteka
   * @param brojJavljanja broj javljanja
   */
  private void ispisiDatotekaBrojJavljanja(int brojDretve, Path datoteka, int brojJavljanja) {
    System.out.println("Dretva: " + brojDretve + " - Datoteka: " + datoteka.getFileName()
        + " - Broj javljanja: " + brojJavljanja);
  }

  /**
   * Preuzmi postavke.
   *
   * @param args args
   */
  private void preuzmiPostavke(String[] args) {
    try {
      Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(args[0]);
      // Properties postavke = konfig.dajSvePostavke();
      // System.out.println("Sve postavke");
      // for (Object o : postavke.keySet()) {
      // String k = (String) o;
      // String v = postavke.getProperty(k);
      // System.out.println("k: " + k + " v: " + v);
      // }
      // System.out.println();
      this.maksDubina = Integer.valueOf(konfig.dajPostavku("maksDubina"));
      this.vrstaTrazenja = Integer.valueOf(konfig.dajPostavku("vrstaTrazenja"));
      this.vrstaDretvi = Integer.valueOf(konfig.dajPostavku("vrstaDretvi"));
      this.pauzaDretve = Integer.valueOf(konfig.dajPostavku("pauzaDretve"));
      this.maksDretvi = Integer.valueOf(konfig.dajPostavku("maksDretvi"));
      this.vrstaIspisa = Integer.valueOf(konfig.dajPostavku("vrstaIspisa"));

      if (this.vrstaDretvi == 0) {
        this.kreatorDretvi = Thread.ofPlatform();
      } else {
        this.kreatorDretvi = Thread.ofVirtual();
      }

      this.tvornicaDretvi = this.kreatorDretvi.factory();

    } catch (NeispravnaKonfiguracija ex) {
      Logger.getLogger(Vjezba_03.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Pretrazivanje strukture direktorija.
   *
   * @param args args
   * @throws IOException signal da se dogodila I/O exception iznimka.
   */
  private void pretrazivanjeStruktureDirektorija(String[] args) throws IOException {
    Path radnaPutanja = Path.of(this.pocetnaPutanja).toAbsolutePath();
    var elementi = Files.list(radnaPutanja);
    // elementi.forEach(d -> {
    // try {
    // System.out.println("%s %6d %20s %s".formatted(
    // sdf.format(new Date(Files.getLastModifiedTime(d).toMillis())),
    // Files.getAttribute(d, "basic:size"), d.toFile().getName(),
    // (Files.isDirectory(d) ? "<DIR>" : "")));
    // } catch (IOException e) {
    // System.out.println(e.getMessage());
    // }
    // });
    for (var i = elementi.iterator(); i.hasNext();) {
      var element = i.next();

      // System.out.println(element + " " + Files.isDirectory(element) + " " +
      // Files.isReadable(element));

      if (Files.isDirectory(element) && Files.isReadable(element)) {
        this.obradiDirektorij(element);
      } else if (Files.isRegularFile(element) && Files.isReadable(element)) {
        int brojJavljanja = this.pretraziDatoteku(element, 0);
        this.ukupanBrojRijeci.addAndGet(brojJavljanja);
      }
    }
    elementi.close();
  }

  /**
   * Obradi direktorij.
   *
   * @param putanja putanja
   */
  private void obradiDirektorij(Path putanja) {

    if (this.brojAktivnihDretvi.get() < this.maksDretvi) {
      stvoriDretvuZaDirektorij(putanja);
    } else {
      this.listaNeobradjenihDirektorija.add(putanja);
    }
  }

  /**
   * Stvori dretvu za direktorij.
   *
   * @param putanja putanja
   */
  private void stvoriDretvuZaDirektorij(Path putanja) {
    var obrada = new ObradaDirektorija(this, 1, putanja);
    var dretva = this.tvornicaDretvi.newThread(obrada);
    dretva.setName(obrada.getNazivDretve());
    this.listaDretvi.add(dretva);
    dretva.start();
  }

  /**
   * Pretrazivanje neobradjenih direktorija.
   *
   * @throws IOException dogodila se I/O iznimka.
   * @throws InterruptedException iznimka prekida
   */
  private synchronized void pretrazivanjeNeobradjenihDirektorija()
      throws IOException, InterruptedException {
    Iterator<Path> iterator = listaNeobradjenihDirektorija.iterator();
    while (iterator.hasNext()) {
      Path putanja = iterator.next();

      if (this.brojAktivnihDretvi.get() < this.maksDretvi) {
        iterator.remove();
        stvoriDretvuZaDirektorij(putanja);
      } else {
        this.wait(this.pauzaDretve);

        iterator.remove();
        stvoriDretvuZaDirektorij(putanja);
      }
    }
  }

}
