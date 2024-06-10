/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.rest;

import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jms.PrijenosnikKazni;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.podaci.Kazna;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/nadzori/kazne")
@RequestScoped
public class NadzoriResurs {

  @Inject
  PrijenosnikKazni prijenosnikKazni;

  /**
   * Dodaje novu kaznu.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaKazna podaci nove kazne
   * @return OK ako je kazna uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Transactional(TxType.REQUIRED)
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Kazna novaKazna) {

    var odgovor = prijenosnikKazni.novaKazna("REST primio zahtjev za kaznu od vozila: " + novaKazna.getId());
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis kazne u bazu podataka.").build();
    }
  }


}