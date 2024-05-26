/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * KazneKontroler za MVC model.
 * 
 * @author Joshua Fletcher
 */
@Controller
@Path("kazne")
@RequestScoped
public class KazneKontroler {

  @Inject
  private Models model;

  @SuppressWarnings("unused")
  @Inject
  private BindingResult bindingResult;

  /**
   * Za pocetak
   */
  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON();
    model.put("kazne", kazne);
  }

  /**
   * Za pocetak
   */
  @GET
  @Path("pocetna")
  @View("kazne-index.jsp")
  public void pocetna() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON();
    model.put("kazne", kazne);
  }
  
//  /**
//   * Za pocetak
//   */
//  @GET
//  @Path("index")
//  @View("kazne-index.jsp")
//  public void index() {
//    RestKlijentKazne k = new RestKlijentKazne();
//    List<Kazna> kazne = k.getKazneJSON();
//    model.put("kazne", kazne);
//  }
  
//  /**
//   * Za pocetak
//   */
//  @GET
//  @View("kazne-index.jsp")
//  public void prazno() {
//    RestKlijentKazne k = new RestKlijentKazne();
//    List<Kazna> kazne = k.getKazneJSON();
//    model.put("kazne", kazne);
//  }

  /**
   * Za ispis kazni
   */
  @GET
  @Path("ispisKazni")
  @View("kazne.jsp")
  public void json() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON();
    model.put("kazne", kazne);
  }
  
  /**
   * Za ispis kazni vozila prema identifikatoru vozila
   * @param idVozila
   */
  @GET
  @Path("{id}/ispisKazni")
  @View("kazne.jsp")
  public void json_id(@PathParam("id") String idVozila) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(idVozila);
    model.put("kazne", kazne);
  }

  /**
   * Za pretrazivanje kazni
   * @param odVremena
   * @param doVremena
   * @param rb
   * @param idVozila
   */
  @POST
  @Path("pretrazivanjeKazni")
  @View("kazne.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena, @FormParam("rb") String rb, @FormParam("idVozila") String idVozila) {
    RestKlijentKazne k = new RestKlijentKazne();
    String info = "";
    List<Kazna> kazne;
    if (rb != null && !rb.isEmpty()) {
      kazne = List.of(k.getKaznaJSON_rb(rb));
      info = "Kazna s rednim brojem " + rb;
    } else if (idVozila != null && !idVozila.isEmpty() && (odVremena == 0 && doVremena == 0)) {
      kazne = k.getKazneJSON_vozilo(idVozila);
      info = "Kazne za vozilo s ID " + idVozila;
    } else if (idVozila != null && !idVozila.isEmpty() && (odVremena >= 0 && doVremena >= 0)) {
      kazne = k.getKazneJSON_vozilo_od_do(idVozila, odVremena, doVremena);
      info = "Kazne za vozilo s ID " + idVozila + " od " + odVremena + " do " + doVremena;
    } else {
      info = "Kazne od " + odVremena + " do " + doVremena;
      kazne = k.getKazneJSON_od_do(odVremena, doVremena);
    }
    model.put("kazne", kazne);
    model.put("vrijednosti",  info);
  }

}