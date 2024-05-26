/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.kontroler;

import java.io.File;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model.RestKlijentSimulacije;
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
import slusac.AppContextListener;

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
  
  /**
   * Za pocetak
   */
  @GET
  @Path("index")
  @View("simulacije-index.jsp")
  public void index() {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
  }
  
  /**
   * Za pocetak
   */
  @GET
  @Path("pocetna")
  @View("simulacije-index.jsp")
  public void pocetna() {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
  }
  
  /**
   * Za pocetak
   */
  @GET
  @View("simulacije-index.jsp")
  public void prazno() {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
  }

  /**
   * Za ispis voznji
   */
  @GET
  @Path("ispisVoznji")
  @View("simulacije.jsp")
  public void json() {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
  }

  /**
   * Za pretrazivanje voznji
   * 
   * @param odVremena
   * @param doVremena
   * @param rb
   * @param idVozila
   */
  @POST
  @Path("pretrazivanjeVoznji")
  @View("simulacije.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena, @FormParam("rb") String rb,
      @FormParam("idVozila") String idVozila) {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    String info = "";
    List<Voznja> voznje;
    if (idVozila != null && !idVozila.isEmpty() && (odVremena == 0 && doVremena == 0)) {
      voznje = s.getVoznjeJSON_vozilo(idVozila);
      info = "Voznje za vozilo s ID " + idVozila;
    } else if (idVozila != null && !idVozila.isEmpty() && (odVremena >= 0 && doVremena >= 0)) {
      voznje = s.getVoznjeJSON_vozilo_od_do(idVozila, odVremena, doVremena);
      info = "Voznje za vozilo s ID " + idVozila + " od " + odVremena + " do " + doVremena;
    } else {
      info = "Voznje od " + odVremena + " do " + doVremena;
      voznje = s.getVoznjeJSON_od_do(odVremena, doVremena);
    }
    model.put("voznje", voznje);
    model.put("vrijednosti", info);
  }

  /**
   * Za dodavanje voznje
   * 
   * @param id vozila iz forme
   * @param broj retka "u datoteci" iz forme
   * @param vrijeme vozila iz forme
   * @param brzina vozila iz forme
   * @param snaga vozila iz forme
   * @param struja vozila iz forme
   * @param visina vozila iz forme
   * @param gpsBrzina vozila iz forme
   * @param tempVozila vozila iz forme
   * @param postotakBaterija vozila iz forme
   * @param naponBaterija vozila iz forme
   * @param kapacitetBaterija vozila iz forme
   * @param tempBaterija vozila iz forme
   * @param preostaloKm vozila iz forme
   * @param ukupnoKm vozila iz forme
   * @param gpsSirina vozila iz forme
   * @param gpsDuzina vozila iz forme
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
    model.put("vrijednosti", odgovor ? "Uspješno dodana vožnja!" : "Nije uspješno dodana vožnja!");
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);
  }

  /**
   * Za dodavanje simulacije voznje
   * 
   * @param nazivDatoteke
   * @param idVozila
   * @param trajanjeSek
   * @param trajanjePauza
   */
  @POST
  @Path("dodajSimulacijuVoznje")
  @View("simulacije.jsp")
  public void postJSON(@FormParam("nazivDatoteke") String nazivDatoteke,
      @FormParam("idVozila") int idVozila, @FormParam("trajanjeSek") int trajanjeSek,
      @FormParam("trajanjePauza") int trajanjePauza) {
    RestKlijentSimulacije s = new RestKlijentSimulacije();
    boolean odgovor = true;
    if (nazivDatoteke == null || nazivDatoteke.isEmpty())
      odgovor = false;
    File file = new File(AppContextListener.getAppPath() + nazivDatoteke);
    if (!file.exists()) {
      odgovor = false;
      model.put("vrijednosti", "Datoteka ne postoji! ");
    } else {
      model.put("vrijednosti", "");
      s.postVoznjaJSON(nazivDatoteke, idVozila, trajanjeSek, trajanjePauza);
    }
    model.put("vrijednosti",
        odgovor ? model.get("vrijednosti") + "Uspješno dodana simulacija vožnje " : model.get("vrijednosti") + "Nije uspješno dodana simulacija vožnje ");
    model.put("vrijednosti", model.get("vrijednosti") + " " + nazivDatoteke + " " + idVozila + " "
        + trajanjeSek + " " + trajanjePauza);
    List<Voznja> voznje = s.getVoznjeJSON_od_do(0, 0);
    model.put("voznje", voznje);

  }

}
