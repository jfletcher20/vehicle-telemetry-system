/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Radar;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * ● api/radari- pokriva područje rada poslužitelja PoslužiteljZaRegistraciju s kojim jednosmjerno
 * komunicira. Podatke sprema u memoriji. o GET–vraćasveradare o GET/reset– pokreće postupak
 * resetiranja svih poslužitelja slanjem komande poslužitelju PoslužiteljZaRegistraciju o GET/{id}–
 * vraća podatke za radar sa zadanim id o GET/{id}/provjeri– pokreće provjeru radara sa zadanim id o
 * DELETE–brišepodatke za sve radare slanjem komande poslužitelju PoslužiteljZaRegistraciju o
 * DELETE/{id}– briše podatke za radar radar sa zadanim id slanjem komande poslužitelju
 * PoslužiteljZaRegistraciju
 */

/**
 * REST Web Service uz korištenje klase Voznje
 *
 * @author Joshua Lee Fletcher
 */
@Path("nwtis/v1/api/radari")
public class RadariResurs extends SviResursi {

  private String nazivKonfiguracije = "NWTiS_REST_R.txt";

  private String adresaRegistracije;
  private int mreznaVrataRegistracije;

  @PostConstruct
  private void pripremiKorisnikDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    int i = 10;
    while (i-- > 0) {
      try {
        String prefix = "";
        for (int j = 0; j < i; j++)
          prefix += "../";
        var konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(prefix + nazivKonfiguracije);
        System.out.println("Konfiguracija ucitana na adresi: " + prefix + nazivKonfiguracije);
        adresaRegistracije = konfig.dajPostavku("adresaRegistracije");
        mreznaVrataRegistracije = Parsiraj.i(konfig.dajPostavku("mreznaVrataRegistracije"));
      } catch (NeispravnaKonfiguracija e) {
      }
    }
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora) {

    var odgovor = MrezneOperacije
        .posaljiZahtjevPosluzitelju(adresaRegistracije, mreznaVrataRegistracije, "RADAR SVI")
        .trim();

    List<Radar> radari = new ArrayList<Radar>();
    if (odgovor.contains("OK")) {
      odgovor = odgovor.substring(4, odgovor.length() - 1);
      try {
        for (var radar : odgovor.split(", ")) {
          var rad = radar.substring(1, radar.length() - 1).split(" ");
          var r = new Radar(Parsiraj.i(rad[0]), rad[1], Parsiraj.i(rad[2]), Parsiraj.d(rad[3]),
              Parsiraj.d(rad[4]), Parsiraj.i(rad[5]));
          radari.add(r);
        }
      } catch (Exception e) {
      }
    }

    return Response.status(Response.Status.OK).entity(radari).build();
  }

  /**
   * Dohvaća radar s tim ID.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @return lista voznji
   */
  @Path("/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadar(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {

    var odgovor = MrezneOperacije
        .posaljiZahtjevPosluzitelju(adresaRegistracije, mreznaVrataRegistracije, "RADAR SVI")
        .trim();

    List<Radar> radari = new ArrayList<Radar>();
    if (odgovor.contains("OK")) {
      odgovor = odgovor.substring(4, odgovor.length() - 1);
      try {
        for (var radar : odgovor.split(", ")) {
          var rad = radar.substring(1, radar.length() - 1).split(" ");
          if (!rad[0].equals(String.valueOf(id))) continue;
          var r = new Radar(Parsiraj.i(rad[0]), rad[1], Parsiraj.i(rad[2]), Parsiraj.d(rad[3]),
              Parsiraj.d(rad[4]), Parsiraj.i(rad[5]));
          radari.add(r);
        }
      } catch (Exception e) {
      }
    }
    
    if (radari.size() == 0) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("{\"odgovor\": \"ERROR 12 Nema radara s tim ID-om\"}").build();
    }

    return Response.status(Response.Status.OK).entity(radari).build();
  }

  /**
   * Dohvaća radar s tim ID.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @return lista voznji
   */
  @Path("/{id}/provjeri")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getProvjeriRadar(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    var odgovor = MrezneOperacije
        .posaljiZahtjevPosluzitelju(adresaRegistracije, mreznaVrataRegistracije, "RADAR " + id)
        .trim();
    if (odgovor.contains("OK")) {
      return Response.status(Response.Status.OK).entity("{\"odgovor\": \"" + odgovor + "\"}").build();
    } else
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("{\"odgovor\": \"" + odgovor + "\"}").build();
  }


  /**
   * Dodaje novi radar. • RADAR id adresa mreznaVrata gpsSirina gpsDuzina maksUdaljenost
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param noviRadar podaci nove voznje
   * @return OK ako je voznja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  // @POST
  // @Produces({MediaType.APPLICATION_JSON})
  // public Response postJsonDodajVoznju(@HeaderParam("Accept") String tipOdgovora, Radar noviRadar)
  // {
  // String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(noviRadar.getAdresaRadara(),
  // noviRadar.getMreznaVrataRadara(),
  // "RADAR " + noviRadar.getId() + " " + noviRadar.getAdresaRadara() + " "
  // + noviRadar.getGpsSirina() + " " + noviRadar.getGpsDuzina() + " "
  // + noviRadar.getMaksUdaljenost() + "\n");
  // if (odgovor.contains("OK"))
  // return Response.status(Response.Status.OK).entity(odgovor).build();
  // else
  // return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(odgovor).build();
  // }

}
