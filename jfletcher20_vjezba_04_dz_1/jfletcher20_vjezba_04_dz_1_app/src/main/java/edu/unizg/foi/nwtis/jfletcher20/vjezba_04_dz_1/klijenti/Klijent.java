package edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.klijenti;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_04_dz_1.pomocnici.MrezneOperacije;
import unizg.foi.nwtis.konfiguracije.Konfiguracija;
import unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/*
 * Pokretanje programa Klijent sadrži tri ili dva argumenta: datoteka.(txt | xml | bin | json) id
 * vrijemeOd vrijemeDo datoteka.(txt | xml | bin | json) vrijemeOd vrijemeDo Npr. NWTiS_DZ1_K.txt 1
 * 1708073749078 1708074766471 NWTiS_DZ1_K.txt 1708073749078 1708074766471 Putem mrežne utičnice
 * spaja se na PosluziteljKazni i sinkrono šalje komandu poslužitelju na temelju postavki i traži
 * izvršavanja određene akcije.
 */

/*
 * primjer sadrzaja datoteke postavki: #NWTiS 2024. #Thu Mar 07 10:23:41 CET 2024
 * adresaKazne=localhost mreznaVrataKazne=8020
 */

public class Klijent {

  private int id = -1;
  private long vrijemeOd;
  private long vrijemeDo;

  private String adresaKazne;
  private int mreznaVrataKazne;

  public static void main(String[] args) {
    if (args.length < 3 || args.length > 4) {
      System.out.println("Broj argumenata nije 3 ili 4.");
      return;
    }

    Klijent klijent = new Klijent();
    try {
      klijent.preuzmiPostavke(args);
      var r = klijent.posaljiZahtjev();
      System.out.print("Odgovor poslužitelja: "); // TODO: remove this print
      System.out.println(r); // TODO: remove this println
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  private int _i(String s) {
    return Integer.valueOf(s);
  }

  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    if (args.length == 4) {
      id = Integer.parseInt(args[1]);
      vrijemeOd = Long.parseLong(args[2]);
      vrijemeDo = Long.parseLong(args[3]);
    } else {
      vrijemeOd = Long.parseLong(args[1]);
      vrijemeDo = Long.parseLong(args[2]);
    }
    adresaKazne = konfig.dajPostavku("adresaKazne");
    mreznaVrataKazne = _i(konfig.dajPostavku("mreznaVrataKazne"));
  }

  public String konstruirajZahtjev() {
    return id != -1 ? // ako je ID definiran vrati podatke o kaznama vozila
        "VOZILO " + id + " " + vrijemeOd + " " + vrijemeDo : // u protivnom vrati statistiku
        "STATISTIKA " + vrijemeOd + " " + vrijemeDo;
  }

  public String posaljiZahtjev() {
    return MrezneOperacije.posaljiZahtjevPosluzitelju(adresaKazne, mreznaVrataKazne,
        konstruirajZahtjev());
  }

}
