/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

  private String nazivKonfiguracije = "NWTiS_REST_S.txt";

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
        adresaPosluzitelja = konfig.dajPostavku("adresaVozila");
        mreznaVrataPosluzitelja = Parsiraj.i(konfig.dajPostavku("mreznaVrataVozila"));
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
    try {
      posaljiZahtjevVoznje(novaVoznja);
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
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
      MrezneOperacije.posaljiZahtjevPosluzitelju(adresaPosluzitelja,
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
    } catch (Exception e) {
      return "ERROR 29 " + e.getMessage() + "\n";
    }
    return "OK\n";
  }

}
