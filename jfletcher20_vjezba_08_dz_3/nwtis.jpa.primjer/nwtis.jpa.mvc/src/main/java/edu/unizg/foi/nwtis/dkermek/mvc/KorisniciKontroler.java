/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.dkermek.mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici;
import edu.unizg.foi.nwtis.jfletcher20.jpa.pomocnici.GrupeFacade;
import edu.unizg.foi.nwtis.jfletcher20.jpa.pomocnici.KorisniciFacade;
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
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author Dragutin Kermek
 */
@Controller
@Path("korisnici")
@CsrfProtected
@RequestScoped
public class KorisniciKontroler implements Serializable {

  private static final long serialVersionUID = -8007192317402372003L;

  @Inject
  KorisniciFacade korisniciFacade;

  @Inject
  GrupeFacade grupeFacade;

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("korisnici/index.jsp")
  public void pocetak() {}

  @GET
  @Path("ispisKorisnika")
  @View("korisnici/korisnici.jsp")
  public void ispisKorisnika() {
    List<Korisnici> korisnici = korisniciFacade.findAll();
    model.put("korisnici", korisnici);
    var broj = korisniciFacade.count();
    model.put("brojKorisnika", broj);
  }

  @POST
  @Path("pretrazivanjeKorisnika")
  @View("korisnici/korisnici.jsp")
  public void pretrazivanjeKorisnika(@FormParam("prezime") String prezime,
      @FormParam("ime") String ime) {
    List<Korisnici> korisnici;
    if ((ime == null || ime.length() == 0) && (prezime == null || prezime.length() == 0)) {
      korisnici = korisniciFacade.findAll();
    } else if (ime == null || ime.length() == 0) {
      korisnici = korisniciFacade.findAll(prezime, "%");
    } else if (prezime == null || prezime.length() == 0) {
      korisnici = korisniciFacade.findAll("%", ime);
    } else {
      korisnici = korisniciFacade.findAll(prezime, ime);
    }
    model.put("korisnici", korisnici);
  }

  @GET
  @Path("noviKorisnik")
  @View("korisnici/noviKorisnik.jsp")
  public void noviKorisnik() {}

  @POST
  @Path("dodajKorisnika")
  public String dodajKorisnika(@MvcBinding @FormParam("korisnik") String korId,
      @FormParam("lozinka") String lozinka, @FormParam("prezime") String prezime,
      @FormParam("ime") String ime, @FormParam("email") String email) {
    if (bindingResult.isFailed() || korId == null || korId.trim().length() == 0 || prezime == null
        || lozinka.trim().length() == 0 || lozinka == null || prezime.trim().length() == 0
        || ime == null || ime.trim().length() == 0 || email == null || email.trim().length() == 0) {
      model.put("poruka", "Nisu upisani potrebni podaci.");
      model.put("pogreska", true);
      if (korId != null) {
        model.put("korisnik", korId);
      } else {
        model.put("korisnik", "");
      }
      model.put("lozinka", "");
      if (prezime != null) {
        model.put("prezime", prezime);
      } else {
        model.put("prezime", "");
      }
      if (prezime != null) {
        model.put("ime", ime);
      } else {
        model.put("ime", "");
      }
      if (email != null) {
        model.put("email", email);
      } else {
        model.put("email", "");
      }
      return "korisnici/noviKorisnik.jsp";
    }
    var korisnik = new Korisnici();
    korisnik.setKorisnik(korId);
    korisnik.setIme(ime);
    korisnik.setPrezime(prezime);
    korisnik.setLozinka(lozinka);
    korisnik.setEmail(email);
    korisnik.setGrupes(new ArrayList<Grupe>());

    try {
      korisniciFacade.create(korisnik);
      model.put("poruka",
          "Uspješno dodan korisnik: " + korisnik.getIme() + " " + korisnik.getPrezime());
    } catch (Exception ex) {
      model.put("poruka",
          "Problem kod upisa korisnika. " + ex.getClass().getName() + " - " + ex.getMessage());
      model.put("pogreska", true);
    }
    return "korisnici/noviKorisnik.jsp";
  }

  @POST
  @Path("brisiKorisnika")
  public String brisiKorisnika(@MvcBinding @FormParam("korisnik") String korId) {
    if (bindingResult.isFailed() || korId == null || korId.trim().length() == 0) {
      model.put("poruka", "Nisu upisani potrebni podaci.");
      model.put("pogreska", true);
      return "korisnici/pridruziGrupe.jsp";
    }
    var korisnik = korisniciFacade.find(korId);
    if (korisnik == null) {
      model.put("poruka", "Korisnik: " + korId + " ne postoji.");
      model.put("pogreska", true);
      return "korisnici/pridruziGrupe.jsp";
    }

    try {
      korisniciFacade.remove(korisnik);
      return "redirect:korisnici/ispisKorisnika";
    } catch (Exception ex) {
      model.put("poruka",
          "Problem kod brisanja korisnika. " + ex.getClass().getName() + " - " + ex.getMessage());
      model.put("pogreska", true);
    }
    return "korisnici/pridruziGrupe.jsp";
  }

  @GET
  @Path("pridruziGrupe")
  public String pridruziGrupe(@QueryParam("korisnik") String korId) {
    var korisnik = korisniciFacade.find(korId);
    if (korisnik == null) {
      model.put("poruka", "Korisnik: " + korId + " ne postoji.");
      model.put("pogreska", true);
    }
    var grupeP = srediOstaleGrupe(korisnik);
    model.put("korisnik", korisnik);
    model.put("grupe", grupeP);

    return "korisnici/pridruziGrupe.jsp";

  }

  @POST
  @Path("pridruziGrupe")
  public String pridruziGrupe(@MvcBinding @FormParam("korisnik") String korId,
      @FormParam("odabraneGrupe") String odabraneGrupe) {
    if (bindingResult.isFailed() || korId == null || korId.trim().length() == 0
        || odabraneGrupe == null) {
      model.put("poruka", "Nisu upisani potrebni podaci.");
      model.put("pogreska", true);

      return "korisnici/pridruziGrupe.jsp";
    }

    var korisnik = korisniciFacade.find(korId);
    if (korisnik == null) {
      model.put("poruka",
          "Problem kod pridruživanja grupa korisniku. Korisnik: " + korId + " ne postoji.");
      model.put("pogreska", true);
      return "korisnici/pridruziGrupe.jsp";
    }

    var grupeZaUpis = Arrays.asList(odabraneGrupe.split(",")).stream().map(g -> g.trim()).toList();

    korisnik.setGrupes(new ArrayList<Grupe>());

    for (String g1 : grupeZaUpis) {
      var grupa = grupeFacade.find(g1);
      grupa.getKorisnicis().add(korisnik);
      korisnik.getGrupes().add(grupa);
    }

    var grupeVazece = srediOstaleGrupe(korisnik);
    model.put("korisnik", korisnik);
    model.put("grupe", grupeVazece);

    try {
      korisniciFacade.edit(korisnik);
      model.put("poruka", "Uspješno pridruživanje grupa korisniku: " + korisnik.getIme() + " "
          + korisnik.getPrezime());
    } catch (Exception ex) {
      model.put("poruka", "Problem kod pridruživanja grupa korisniku. " + ex.getClass().getName()
          + " - " + ex.getMessage());
      model.put("pogreska", true);
    }
    return "korisnici/pridruziGrupe.jsp";
  }

  private Properties srediOstaleGrupe(Korisnici korisnik) {
    var grupe = grupeFacade.findAll();
    var grupeS = korisnik.getGrupes().stream().map(g -> g.getGrupa()).toList();
    var grupeP = new Properties();
    var ima = false;
    for (Grupe g1 : grupe) {
      ima = false;
      for (String g2 : grupeS) {
        if (g2.compareTo(g1.getGrupa()) == 0) {
          ima = true;
        }
      }
      if (!ima) {
        grupeP.put(g1.getGrupa(), g1.getNaziv());
      }
    }
    return grupeP;
  }
}
