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

  @GET
  @Path("ispisRadara")
  @View("radari.jsp")
  public void json() {
    RestKlijentRadari s = new RestKlijentRadari();
    List<Radar> radari = s.getRadariJSON();
    model.put("radari", radari);
  }

  private boolean jeUkljuceno(String var) {
    return var != null && !var.isEmpty() && var.equals("on");
  }

  /*
   * the form contents:
   * 
   * <fieldset> <div class="row margin-bottom-large"> <button id="resetiraj-radare"
   * class="button-green" style="margin-left: 0;">Resetiraj</button> <p
   * style="margin-left: auto">Upravljaj radarima.</p> <button id="obrisi-radare"
   * class="button-red">Obriši sve</button> </div> <div class="row"> <label for="idRadara">ID Radara
   * <input name="idRadara" id="idRadara" type="number" pattern="[0-9]"> </label> </div> <div
   * class="row" style="width: 43%"> <button id="provjeri-radar" class="button-green"
   * style="margin-left: 0">Provjeri radar</button> <button id="obrisi-radar"
   * class="button-red">Obriši radar</button> </div> </fieldset> <input hidden type="checkbox"
   * id="checkbox-provjeri" name="provjeri"> <input hidden type="checkbox" id="checkbox-delete"
   * name="delete"> <input type="submit" value="Potvrda">
   * 
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
      radari = r.getRadariJSON_radar(idRadara);
      info = "Radari s ID " + idRadara;
    } else if (idRadara != null && !idRadara.isEmpty() && jeUkljuceno(provjeri)) {
      radari = r.getRadariJSON();
      info = "Provjera radara s ID " + idRadara + " "
          + (r.getRadariJSON_radar_provjeri(idRadara) ? "uspjela" : "nije uspjela");
    } else if (idRadara != null && !idRadara.isEmpty() && jeUkljuceno(delete)) {
      radari = r.getRadariJSON();
      info = "Brisanje radara s ID " + idRadara + " "
          + (r.deleteRadari_radar(idRadara) ? "uspjelo" : "nije uspjelo");
    } else if (jeUkljuceno(reset)) {
      radari = r.getRadariJSON();
      info = "Resetiranje svih radara " + (r.deleteRadari_radare() ? "uspjelo" : "nije uspjelo");
    } else if (jeUkljuceno(deleteSve)) {
      radari = r.getRadariJSON();
      info = "Brisanje svih radara " + (r.deleteRadari_radare() ? "uspjelo" : "nije uspjelo");
    } else {
      info = "Prikaz svih radara";
      radari = r.getRadariJSON();
    }

    model.put("radari", radari);
    model.put("vrijednosti", info);
    
  }

}
