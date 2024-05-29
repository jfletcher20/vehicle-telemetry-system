/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.dkermek.mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici;
import edu.unizg.foi.nwtis.jfletcher20.jpa.pomocnici.GrupeFacade;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.mvc.binding.MvcBinding;
import jakarta.mvc.security.CsrfProtected;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 *
 * @author Dragutin Kermek
 */
@Controller
@Path("grupe")
@CsrfProtected
@RequestScoped
public class GrupeKontroler implements Serializable {

  private static final long serialVersionUID = 5368593540038000603L;

  @Inject
  GrupeFacade grupeFacade;

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("grupe/index.jsp")
  public void pocetak() {}

  @GET
  @Path("ispisGrupa")
  @View("grupe/grupe.jsp")
  public void ispisGrupa() {
    List<Grupe> grupe = grupeFacade.findAll();
    model.put("grupe", grupe);
    var broj = grupeFacade.count();
    model.put("brojGrupa", broj);
  }

  @POST
  @Path("pretrazivanjeGrupa")
  @View("grupe/grupe.jsp")
  public void pretrazivanjeGrupa(@FormParam("grupa") String grupa,
      @FormParam("naziv") String naziv) {
    List<Grupe> grupe;
    if ((grupa == null || grupa.length() == 0) && (naziv == null || naziv.length() == 0)) {
      grupe = grupeFacade.findAll();
    } else if (grupa == null || grupa.length() == 0) {
      grupe = grupeFacade.findAll("%", naziv);
    } else if (naziv == null || naziv.length() == 0) {
      grupe = grupeFacade.findAll(grupa, "%");;
    } else {
      grupe = grupeFacade.findAll(grupa, naziv);
    }
    model.put("grupe", grupe);
  }

  @GET
  @Path("novaGrupa")
  @View("grupe/novaGrupa.jsp")
  public void novaGrupa() {}

  @POST
  @Path("dodajGrupu")
  public String dodajGrupu(@MvcBinding @FormParam("grupa") String grupa,
      @FormParam("naziv") String naziv) {
    if (bindingResult.isFailed() || grupa == null || grupa.trim().length() == 0 || naziv == null
        || naziv.trim().length() == 0) {
      model.put("poruka", "Nisu upisani potrebni podaci.");
      model.put("pogreska", true);
      if (grupa != null) {
        model.put("grupa", grupa);
      } else {
        model.put("grupa", "");
      }
      model.put("naziv", "");
      if (naziv != null) {
        model.put("naziv", naziv);
      } else {
        model.put("naziv", "");
      }
      return "grupe/novaGrupa.jsp";
    }
    var grupaE = new Grupe();
    grupaE.setGrupa(grupa);
    grupaE.setNaziv(naziv);
    grupaE.setKorisnicis(new ArrayList<Korisnici>());

    try {
      grupeFacade.create(grupaE);
      model.put("poruka", "Uspješno dodana grupa: " + grupaE.getGrupa() + " " + grupaE.getNaziv());
    } catch (Exception ex) {
      model.put("poruka",
          "Problem kod upisa grupe. " + ex.getClass().getName() + " - " + ex.getMessage());
      model.put("pogreska", true);
    }
    return "grupe/novaGrupa.jsp";
  }
}
