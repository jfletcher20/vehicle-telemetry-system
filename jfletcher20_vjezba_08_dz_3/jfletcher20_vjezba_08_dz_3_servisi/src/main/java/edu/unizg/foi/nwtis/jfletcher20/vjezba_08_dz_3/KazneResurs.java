/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jpa.entiteti.Kazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jpa.pomocnici.KazneFacade;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Web Service uz korištenje klase Kazna
 *
 * @author Dragutin Kermek
 */
@Path("nwtis/v1/api/kazne")
@RequestScoped
public class KazneResurs {

  @Inject
  KazneFacade kazneFacade;

  @Inject
  VozilaFacade vozilaFacade;

  /**
   * Dohvaća sve kazne ili kazne u intervalu, ako je definiran
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param od od vremena
   * @param do do vremena
   * @return lista kazni
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK)
          .entity(pretvoriKolekcijuKazna(kazneFacade.dohvatiSveKazne())).build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(pretvoriKolekcijuKazna(kazneFacade.dohvatiKazne(odVremena, doVremena))).build();
    }
  }

  /**
   * Dohvaća kaznu za definirani redni broj
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param rb redni broj zapisa
   * @return lista kazni
   */
  @Path("{rb}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonKaznaRb(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("rb") int rb) {

    var kazne = kazneFacade.find(rb);
    if (kazne == null) {
      return Response.status(Response.Status.NOT_FOUND).entity("Ne postoji kazna s rb: " + rb)
          .build();
    } else {
      return Response.status(Response.Status.OK).entity(pretvoriKazna(kazne)).build();
    }
  }

  /**
   * Dohvaća kazne za definirano vozilo
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @return lista kazni
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonKaznaVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {

    return Response.status(Response.Status.OK)
        .entity(pretvoriKolekcijuKazna(kazneFacade.dohvatiKazneVozila(id))).build();
  }

  /**
   * Provjerava stanje
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @return OK
   */
  @HEAD
  @Produces({MediaType.APPLICATION_JSON})
  public Response head(@HeaderParam("Accept") String tipOdgovora) {

    if (provjeriPosluzitelja()) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješna provjera poslužitelja kazni.").build();
    }
  }

  /**
   * Dodaje novu kaznu.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaKazna podaci nove kazne
   * @return OK ako je kazna uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Kazna novaKazna) {

    var kazne = pretvoriKazna(novaKazna);

    var odgovor = kazneFacade.dodajKaznu(kazne);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis kazne u bazu podataka.").build();
    }
  }

  private boolean provjeriPosluzitelja() {
    var poruka = new StringBuilder();
    poruka.append("TEST").append("\n");

    // TODO trenutno je ukodirana adresa i broj mrežnih vratiju. Treba preuzeti iz konfiguracijskih
    // podataka.
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju("localhost", 8020, poruka.toString());

    if (odgovor != null) {
      return true;
    } else {
      return false;
    }
  }


  private Kazne pretvoriKazna(Kazna kazna) {
    var kazne = new Kazne();
    kazne.setBrzina(kazna.getBrzina());
    kazne.setGpsduzina(kazna.getGpsDuzina());
    kazne.setGpssirina(kazna.getGpsSirina());
    kazne.setGpsduzinaradar(kazna.getGpsDuzinaRadar());
    kazne.setGpssirinaradar(kazna.getGpsSirinaRadar());
    kazne.setVrijemepocetak(kazna.getVrijemePocetak());
    kazne.setVrijemekraj(kazna.getVrijemeKraj());

    var vozilo = vozilaFacade.find(kazna.getId());

    kazne.setId(vozilo);
    return kazne;
  }

  private Kazna pretvoriKazna(Kazne kazne) {
    if (kazne == null) {
      return null;
    }
    var kazna = new Kazna(kazne.getId().getVozilo(), kazne.getVrijemepocetak(),
        kazne.getVrijemekraj(), kazne.getBrzina(), kazne.getGpssirina(), kazne.getGpsduzina(),
        kazne.getGpssirinaradar(), kazne.getGpsduzinaradar());
    return kazna;
  }

  private List<Kazna> pretvoriKolekcijuKazna(List<Kazne> kazne) {
    var kaznaKolekcija = new ArrayList<Kazna>();
    for (Kazne k : kazne) {
      var kazna = pretvoriKazna(k);
      kaznaKolekcija.add(kazna);
    }
    return kaznaKolekcija;
  }
}