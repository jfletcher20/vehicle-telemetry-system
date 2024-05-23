/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Kazna;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja;
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

/**
 * VoznjeKontroler za MVC model.
 * 
 * @author Joshua Fletcher
 */
@Controller
@Path("simulacije")
@RequestScoped
public class SimulacijeKontroler {

  @Inject
  private Models model;

  @SuppressWarnings("unused")
  @Inject
  private BindingResult bindingResult;

//  @GET
//  @Path("pocetak")
//  @View("index.jsp")
//  public void pocetak() {
//    RestKlijentKazne k = new RestKlijentKazne();
//    List<Kazna> kazne = k.getKazneJSON();
//    model.put("kazne", kazne);
//  }

  @GET
  @Path("ispisVoznji")
  @View("simulacije.jsp")
  public void json() {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
  }

  @POST
  @Path("pretrazivanjeVoznji")
  @View("simulacije.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena, @FormParam("rb") String rb, @FormParam("idVozila") String idVozila) {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    String additionalInfo = "";
    List<Voznja> voznje;
    if (idVozila != null && !idVozila.isEmpty() && (odVremena == 0 && doVremena == 0)) {
      voznje = s.getVoznjeJSON_vozilo(idVozila);
      additionalInfo = "Voznje za vozilo s ID " + idVozila;
    } else if (idVozila != null && !idVozila.isEmpty() && (odVremena >= 0 && doVremena >= 0)) {
      voznje = s.getVoznjeJSON_vozilo_od_do(idVozila, odVremena, doVremena);
      additionalInfo = "Voznje za vozilo s ID " + idVozila + " od " + odVremena + " do " + doVremena;
    } else {
      additionalInfo = "Voznje od " + odVremena + " do " + doVremena;
      voznje = s.getVoznjeJSON_od_do(odVremena, doVremena);
    }
    
    model.put("voznje", voznje);
    model.put("values",  additionalInfo);
  }
  
  /*
   * 
public class Voznja {

  private int id, broj;
  private long vrijeme;
  private double brzina, snaga, struja, visina, gpsBrzina, naponBaterija;
  private int tempVozila, postotakBaterija, kapacitetBaterija, tempBaterija;
  private double preostaloKm, ukupnoKm, gpsSirina, gpsDuzina;
   */
  
  @POST
  @Path("dodajVoznju")
  @View("simulacije.jsp")
  public void postJSON(@FormParam("id") int id, @FormParam("broj") int broj,
      @FormParam("vrijeme") long vrijeme, @FormParam("brzina") double brzina,
      @FormParam("snaga") double snaga, @FormParam("struja") double struja,
      @FormParam("visina") double visina, @FormParam("gpsBrzina") double gpsBrzina,
      @FormParam("tempVozila") int tempVozila, @FormParam("postotakBaterija") int postotakBaterija,
      @FormParam("naponBaterija") double naponBaterija,
      @FormParam("kapacitetBaterija") int kapacitetBaterija,
      @FormParam("tempBaterija") int tempBaterija, @FormParam("preostaloKm") double preostaloKm,
      @FormParam("ukupnoKm") double ukupnoKm, @FormParam("gpsSirina") double gpsSirina,
      @FormParam("gpsDuzina") double gpsDuzina) {
    
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    Voznja voznja = new Voznja(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina,
        tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm,
        ukupnoKm, gpsSirina, gpsDuzina);
    
    boolean odgovor = s.postVoznjaJSON(voznja);
    model.put("values", odgovor ? "Uspješno dodana vožnja!" : "Nije uspješno dodana vožnja!");
    
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
    
  }

}