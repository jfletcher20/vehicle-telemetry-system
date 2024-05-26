/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model.RestKlijentRadari;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Radar;
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

/*
 * ● api/radari- pokriva područje rada poslužitelja PoslužiteljZaRegistraciju s kojim jednosmjerno
 * komunicira. Podatke sprema u memoriji.
 * 
 * o GET–vraćasveradare
 * 
 * o GET/reset– pokreće postupak resetiranja svih poslužitelja slanjem komande poslužitelju
 * PoslužiteljZaRegistraciju
 * 
 * o GET/{id}– vraća podatke za radar sa zadanim id
 * 
 * o GET/{id}/provjeri– pokreće provjeru radara sa zadanim id
 * 
 * o DELETE–brišepodatke za sve radare slanjem komande poslužitelju PoslužiteljZaRegistraciju
 * 
 * o DELETE/{id}– briše podatke za radar radar sa zadanim id slanjem komande poslužitelju
 * PoslužiteljZaRegistraciju
 * 
 */

/**
 * RadariKontroler za MVC model.
 * 
 * @author Joshua Fletcher
 */
@Controller
@Path("radari")
@RequestScoped
public class RadariKontroler {

  @Inject
  private Models model;

  @SuppressWarnings("unused")
  @Inject
  private BindingResult bindingResult;

  /**
   * Za pocetak
   */
  @GET
  @Path("pocetna")
  @View("radari-index.jsp")
  public void pocetna() {
    RestKlijentRadari s = new RestKlijentRadari();
    List<Radar> radari = s.getRadariJSON();
    model.put("radari", radari);
  }

  /**
   * Za pocetak
   */
  @GET
  @Path("index")
  @View("radari-index.jsp")
  public void index() {
    RestKlijentRadari s = new RestKlijentRadari();
    List<Radar> radari = s.getRadariJSON();
    model.put("radari", radari);
  }

  /**
   * Za pocetak
   */
  @GET
  @View("radari-index.jsp")
  public void prazno() {
    RestKlijentRadari s = new RestKlijentRadari();
    List<Radar> radari = s.getRadariJSON();
    model.put("radari", radari);
  }

  /**
   * Za ispis radara
   */
  @GET
  @Path("ispisRadara")
  @View("radari.jsp")
  public void json() {
    RestKlijentRadari s = new RestKlijentRadari();
    List<Radar> radari = s.getRadariJSON();
    model.put("radari", radari);
  }

  /**
   * Provjerava je li varijabla ukljucena.
   * @param var varijabla
   * @return true ako je ukljucena, inace false
   */
  private boolean jeUkljuceno(String var) {
    return var != null && !var.isEmpty() && var.equals("on");
  }

  /**
   * Za pretrazivanje radara
   * @param idRadara ID radara
   * @param provjeri provjeri varijabla 
   * @param delete delete varijabla
   * @param reset reset varijabla
   * @param deleteSve delete sve varijabla
   */
  @POST
  @Path("pretrazivanjeRadara")
  @View("radari.jsp")
  public void json_pi(@FormParam("idRadara") String idRadara,
      @FormParam("provjeri") String provjeri, @FormParam("delete") String delete,
      @FormParam("reset") String reset, @FormParam("delete-sve") String deleteSve) {
    RestKlijentRadari r = new RestKlijentRadari();
    String info = "";
    List<Radar> radari;
    if (idRadara != null && !idRadara.isEmpty() && !(jeUkljuceno(provjeri) || jeUkljuceno(delete)
        || jeUkljuceno(reset) || jeUkljuceno(deleteSve))) {
      info = "Radari s ID " + idRadara;
      radari = r.getRadariJSON_radar(idRadara);
    } else if (idRadara != null && !idRadara.isEmpty() && jeUkljuceno(provjeri)) {
      info = "Provjera radara s ID " + idRadara + " "
          + (r.getRadariJSON_radar_provjeri(idRadara) ? "uspjelo" : "nije uspjelo");
      radari = r.getRadariJSON();
    } else if (idRadara != null && !idRadara.isEmpty() && jeUkljuceno(delete)) {
      info = "Brisanje radara s ID " + idRadara + " "
          + (r.deleteRadari_radar(idRadara) ? "uspjelo" : "nije uspjelo");
      radari = r.getRadariJSON();
    } else if (jeUkljuceno(reset)) {
      info = "Resetiranje svih radara " + (r.deleteRadari_radare() ? "uspjelo" : "nije uspjelo");
      radari = r.getRadariJSON();
    } else if (jeUkljuceno(deleteSve)) {
      info = "Brisanje svih radara " + (r.deleteRadari_radare() ? "uspjelo" : "nije uspjelo");
      radari = r.getRadariJSON();
    } else {
      info = "Prikaz svih radara";
      radari = r.getRadariJSON();
    }
    model.put("radari", radari);
    model.put("vrijednosti", info);
  }

}
