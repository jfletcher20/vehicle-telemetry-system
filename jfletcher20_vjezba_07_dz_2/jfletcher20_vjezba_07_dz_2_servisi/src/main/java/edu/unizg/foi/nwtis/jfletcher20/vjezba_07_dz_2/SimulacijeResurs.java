/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Simulacija;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.VoznjeDAO;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Web Service uz korištenje klase Voznje
 *
 * @author Joshua Lee Fletcher
 */
@Path("nwtis/v1/api/simulacije")
public class SimulacijeResurs extends SviResursi {

  private String nazivKonfiguracije = "NWTiS_REST_R.txt";

  private String adresaPosluzitelja;
  private int mreznaVrataPosluzitelja;

  private VoznjeDAO voznjeDAO = null;

  @PostConstruct
  private void pripremiKorisnikDAO() {
    int i = 10;
    while (i-- > 0) {
      try {
        String prefix = "";
        for (int j = 0; j < i; j++)
          prefix += "../";
        var konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(prefix + nazivKonfiguracije);
        System.out.println("Konfiguracija ucitana na adresi: " + prefix + nazivKonfiguracije);
        adresaPosluzitelja = konfig.dajPostavku("adresaRegistracije");
        mreznaVrataPosluzitelja = Parsiraj.i(konfig.dajPostavku("mreznaVrataRegistracije"));
      } catch (NeispravnaKonfiguracija e) {
      }
    }
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.voznjeDAO = new VoznjeDAO(vezaBP);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Dohvaća sve voznje ili voznje u intervalu, ako je definiran
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param od od vremena
   * @param do do vremena
   * @return lista voznji
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK).entity(voznjeDAO.dohvatiSveVoznje().toArray())
          .build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(voznjeDAO.dohvatiVoznje(odVremena, doVremena).toArray()).build();
    }
  }

  /**
   * Dohvaća voznje za definirano vozilo
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @return lista voznji
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonSimulacijaVoznja(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0)
      return Response.status(Response.Status.OK).entity(voznjeDAO.dohvatiVoznjeVozila(id)).build();
    else
      return Response.status(Response.Status.OK)
          .entity(voznjeDAO.dohvatiVoznjeVozila(id, odVremena, doVremena)).build();
  }

  /**
   * Dodaje novu voznju.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaVoznja podaci nove voznje
   * @return OK ako je voznja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response postDodajSimulaciju(@HeaderParam("Accept") String tipOdgovora,
      Voznja novaVoznja) {
    String r = "";
    try {
      r = posaljiZahtjevVoznje(novaVoznja);
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(r).build();
    }
    var odgovor = voznjeDAO.dodajVoznju(novaVoznja);
    if (odgovor)
      return Response.status(Response.Status.OK).build();
    else
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis voznje u bazu podataka.").build();
  }


  private String posaljiZahtjevVoznje(Voznja novaVoznja) {
    try {
      // voznje (id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,
      // postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,
      // gpsSirina, gpsDuzina) "
      String r = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaPosluzitelja,
          mreznaVrataPosluzitelja,
          "VOZILO " + novaVoznja.getId() + " " + novaVoznja.getBroj() + " "
              + novaVoznja.getVrijeme() + " " + novaVoznja.getBrzina() + " " + novaVoznja.getSnaga()
              + " " + novaVoznja.getStruja() + " " + novaVoznja.getVisina() + " "
              + novaVoznja.getGpsBrzina() + " " + novaVoznja.getTempVozila() + " "
              + novaVoznja.getPostotakBaterija() + " " + novaVoznja.getNaponBaterija() + " "
              + novaVoznja.getKapacitetBaterija() + " " + novaVoznja.getTempBaterija() + " "
              + novaVoznja.getPreostaloKm() + " " + novaVoznja.getUkupnoKm() + " "
              + novaVoznja.getGpsSirina() + " " + novaVoznja.getGpsDuzina() + "\n")
          .trim();
      if (r.contains("ERROR"))
        throw new Exception("Neuspješno slanje voznje na server zbog: " + r);
    } catch (Exception e) {
      return "ERROR 29 " + e.getMessage() + "\n";
    }
    return "OK\n";
  }

  /*
   * this is the server-side -- this function should do something similar public long citajCSV() {
   * try (BufferedReader reader = new BufferedReader(new FileReader(podaciVozilaDatoteka))) { for
   * (int i = 0; i < brojRetka; i++) reader.readLine(); String row = reader.readLine(); if (row ==
   * null) System.exit(0); // prekida program ako nema više redaka String[] data = row.split(",");
   * var p = new PodaciVozila(idVozila, brojRetka++, Parsiraj.l(data[0]), Parsiraj.d(data[1]),
   * Parsiraj.d(data[2]), Parsiraj.d(data[3]), Parsiraj.d(data[4]), Parsiraj.d(data[5]),
   * Parsiraj.i(data[6]), Parsiraj.i(data[7]), Parsiraj.d(data[8]), Parsiraj.i(data[9]),
   * Parsiraj.i(data[10]), Parsiraj.d(data[11]), Parsiraj.d(data[12]), Parsiraj.d(data[13]),
   * Parsiraj.d(data[14])); long razlika = 0; if (redPodaciVozila.dajBrojPodatakaVozila() > 0)
   * razlika = razlikaVremena(p); redPodaciVozila.dodajPodatakVozila(p); return razlika; } catch
   * (IOException e) { e.printStackTrace(); return 0; } }
   */

  /**
   * Dodaje novu simulaciju voznji.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param datoteka naziv .csv datoteke s podacima nove voznje
   * @param idVozila identifikator vozila
   * @param trajanjeSek trajanje sekunde u simulaciji voznje
   * @param trajanjePauze trajanje pauze u simulaciji voznje
   * @return OK ako je voznja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonDodajVoznju(@HeaderParam("Accept") String tipOdgovora,
      Simulacija simulacija) {

    System.out.println("Simulacija: " + simulacija);

//    String datoteka = simulacija.getPodaciVozila();
    int idVozila = simulacija.getId();
    int trajanjeSek = simulacija.getTrajanjeSek();
    int trajanjePauze = simulacija.getTrajanjePauze();

    int brojRetka = 1;
    long vrijemeZadnjeVoznje = 0;
    boolean odgovor = false;
    int maxRetciCSVDatoteke = 0;
    try {
//      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//          .entity("Datoteka ima podatke " + maxRetciCSVDatoteke + " za varijable " + datoteka + ", "
//              + idVozila + ", " + trajanjeSek + ", " + trajanjePauze + "\n")
//          .build();
    } catch (Exception e) {
      e.printStackTrace();

    }

    try {
//      Voznja novaVoznja = voznjaIzCSV(idVozila, datoteka, brojRetka);
      // Thread
      // .sleep((long) (razlikaVremena(novaVoznja, vrijemeZadnjeVoznje) * trajanjeSek / 1000.0));
//      odgovor = voznjeDAO.dodajVoznju(novaVoznja);
//      vrijemeZadnjeVoznje = novaVoznja.getVrijeme();
      brojRetka++;
      System.out.println("iteracija: " + brojRetka + " od " + maxRetciCSVDatoteke);
      // Thread.sleep(trajanjePauze);
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("ERROR 29 kod simulacije: " + e.getMessage() + "\n").build();
    }
    if (odgovor)
      return Response.status(Response.Status.OK).build();
    else
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis kazne u bazu podataka.").build();
  }

}
