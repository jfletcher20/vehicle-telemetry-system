/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.VoznjeDAO;
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
public class VoznjeResurs extends SviResursi {
  private VoznjeDAO voznjeDAO = null;

  @PostConstruct
  private void pripremiKorisnikDAO() {
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
  public Response getJsonVoznjaVozilo(@HeaderParam("Accept") String tipOdgovora,
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
  public Response postJsonDodajVoznju(@HeaderParam("Accept") String tipOdgovora, Voznja novaVoznja) {
    var odgovor = voznjeDAO.dodajVoznju(novaVoznja);
    if (odgovor)
      return Response.status(Response.Status.OK).build();
    else
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis kazne u bazu podataka.").build();
  }
  
}
