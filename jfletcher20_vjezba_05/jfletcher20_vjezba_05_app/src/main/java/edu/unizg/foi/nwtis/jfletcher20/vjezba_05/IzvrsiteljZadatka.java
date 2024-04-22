package edu.unizg.foi.nwtis.matnovak.vjezba_05;

import java.lang.reflect.Constructor;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.ZapisivacRada;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.podaci.DnevnikRada;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.podaci.KorisnikInfo;
import edu.unizg.foi.nwtis.matnovak.vjezba_05.dnevnik.podaci.SustavInfo;

public class IzvrsiteljZadatka {
  private String klasaDnevnika;
  private String nazivDnevnika;
  private boolean obrisatiDnevnik;
  private KorisnikInfo korisnikInfo;
  private SustavInfo sustavInfo;
  private ZapisivacRada dnevnik;
  private String korisnickoImeBazaPodataka;
  private String lozinkaBazaPodataka;
  private String urlBazaPodataka;
  private String upravljacBazaPodataka;

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Broj argumenata nije 3.");
      return;
    }

    var izvrsiteljZadatka = new IzvrsiteljZadatka();
    try {
      izvrsiteljZadatka.preuzmiPostavke(args);

      var korisnickoIme = args[1];
      var lozinka = args[2];
      izvrsiteljZadatka.korisnikInfo =
          new KorisnikInfo(korisnickoIme, lozinka, System.currentTimeMillis(), 0);

      izvrsiteljZadatka.sustavInfo = new SustavInfo();

      izvrsiteljZadatka.ucitajKlasuDnevnika();
      izvrsiteljZadatka.obradiUnos();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private void ucitajKlasuDnevnika() throws Exception {
    var klasa = Class.forName(this.klasaDnevnika);
    var sucelja = klasa.getInterfaces();
    var broj = Arrays.stream(sucelja).filter(s -> s.getName().endsWith("ZapisivacRada")).count();

    if (broj == 0) {
      throw new Exception("Klasa ne implementira traženo sučelje 'ZapisivacRada'");
    }

    this.dnevnik = (ZapisivacRada) klasa.getDeclaredConstructor().newInstance();

    if (this.dnevnik.trebaDatoteku()) {
      Constructor<?> konstruktor = klasa.getConstructor(String.class, boolean.class);
      this.dnevnik =
          (ZapisivacRada) konstruktor.newInstance(this.nazivDnevnika, this.obrisatiDnevnik);
    } else if (this.dnevnik.trebaPodatkeBazaPodataka()) {
      Constructor<?> konstruktor =
          klasa.getConstructor(String.class, String.class, String.class, String.class);
      this.dnevnik = (ZapisivacRada) konstruktor.newInstance(this.korisnickoImeBazaPodataka,
          this.lozinkaBazaPodataka, this.urlBazaPodataka, this.upravljacBazaPodataka);
    }
  }

  private void obradiUnos() throws Exception {
    Scanner in = new Scanner(System.in);
    boolean kraj = false;
    while (!kraj) {
      System.out.print("Upiši komandu: ");
      var ulaz = in.nextLine().trim();
      switch (ulaz) {
        case "q" -> {
          kraj = true;
        }
        default -> {
          var t = ulaz.split(" ");
          if (t.length == 3) {
            var komanda = t[0];
            switch (komanda) {
              case "r":
                var brojIteracija = Integer.valueOf(t[1]);
                var pauzaIteracije = Integer.valueOf(t[2]);
                this.izvrsiZadatak(brojIteracija, pauzaIteracije);
                break;
              case "i":
                var vrijemeOd = Long.valueOf(t[1]);
                var vrijemeDo = Long.valueOf(t[2]);
                this.ispisiZapiseDnevnika(vrijemeOd, vrijemeDo);
                break;
              default:
                System.out.println("Pogrešna komanda: " + komanda);
            }
          }
        }
      }
    }
    in.close();
  }

  private void izvrsiZadatak(int brojIteracija, int pauzaIteracije) throws Exception {
    this.dnevnik.pripremiResurs();
    var pocetak = System.currentTimeMillis();
    for (int i = 0; i < brojIteracija; i++) {
      var dnevnikRada =
          new DnevnikRada(System.currentTimeMillis(), this.korisnikInfo.korisnickoIme(),
              this.sustavInfo.getAdresaRacunala(), this.sustavInfo.getIpAdresaRacunala(),
              this.sustavInfo.getNazivOS(), this.sustavInfo.getVerzijaVM(), "Poruka br: " + i);

      this.dnevnik.upisiDnevnik(dnevnikRada);

      Thread.sleep(pauzaIteracije);
    }
    var kraj = System.currentTimeMillis();
    System.out.println("Početak: %d Kraj: %d".formatted(pocetak, kraj));
    this.dnevnik.otpustiResurs();
  }

  private void ispisiZapiseDnevnika(long vrijemeOd, long vrijemeDo) throws Exception {
    // this.dnevnik.pripremiResurs();
    var zapisiDnevnika =
        this.dnevnik.dohvatiDnevnik(vrijemeOd, vrijemeDo, this.korisnikInfo.korisnickoIme());
    this.dnevnik.otpustiResurs();

    zapisiDnevnika.stream()
        .forEach(dnevnikRada -> System.out.println(
            "%s %s %s %s %s %s %s".formatted(dnevnikRada.vrijeme(), dnevnikRada.korisnickoIme(),
                dnevnikRada.adresaRacunala(), dnevnikRada.ipAdresaRacunala(), dnevnikRada.nazivOS(),
                dnevnikRada.verzijaVM(), dnevnikRada.opisRada())));
  }

  private void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.klasaDnevnika = konfig.dajPostavku("klasaDnevnika");
    this.nazivDnevnika = konfig.dajPostavku("nazivDnevnika");
    this.obrisatiDnevnik = Boolean.valueOf(konfig.dajPostavku("obrisatiDnevnika"));
    this.korisnickoImeBazaPodataka = konfig.dajPostavku("korisnickoImeBazaPodataka");
    this.lozinkaBazaPodataka = konfig.dajPostavku("lozinkaBazaPodataka");
    this.urlBazaPodataka = konfig.dajPostavku("urlBazaPodataka");
    this.upravljacBazaPodataka = konfig.dajPostavku("upravljacBazaPodataka");
  }
}
