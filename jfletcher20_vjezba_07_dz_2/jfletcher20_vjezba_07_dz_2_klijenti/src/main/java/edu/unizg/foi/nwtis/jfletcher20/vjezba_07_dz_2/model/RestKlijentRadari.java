package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Radar;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentKazne.
 */
public class RestKlijentRadari {

  /**
   * Konstruktor klase.
   */
  public RestKlijentRadari() {}

  /**
   * Vraća sve radare
   *
   * @return radare
   */
  public List<Radar> getRadariJSON() {
    RestRadari rk = new RestRadari();
    List<Radar> radare = rk.getJSON();

    return radare;
  }

  /**
   * Vraća radar s ID-em.
   *
   * @param id id radara
   * @return radare
   */
  public List<Radar> getRadariJSON_radar(String id) {
    RestRadari rk = new RestRadari();
    var radar = rk.getJSON_radar(id);
    List<Radar> radare = new ArrayList<Radar>();
    radare.add(radar);
    return radare;
  }

  /**
   * Provjerava radar.
   *
   * @param id id vozila
   * @return radare
   */
  public boolean getRadariJSON_radar_provjeri(String id) {
    RestRadari rk = new RestRadari();
    boolean uspjeh = rk.getJSON_radar_provjeri(id);
    return uspjeh;
  }
  
  /**
   * Briše radar.
   *
   * @param id id radara
   * @return boolean uspjeh
   */
  public boolean deleteRadari_radar(String id) {
    RestRadari rk = new RestRadari();
    boolean uspjeh = rk.delete_radar(id);
    return uspjeh;
  }
  
  /**
   * Briše sve radare.
   *
   * @return boolean uspjeh
   */
  public boolean deleteRadari_radare() {
    RestRadari rk = new RestRadari();
    boolean uspjeh = rk.delete_radare();
    return uspjeh;
  }

  /**
   * Klasa RestRadari.
   */
  static class RestRadari {

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestRadari() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/radari");
    }

    /**
     * Vraća radare.
     *
     * @return radare
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Radar> getJSON() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Radar> radare = new ArrayList<Radar>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradare = jb.fromJson(odgovor, Radar[].class);
        radare.addAll(Arrays.asList(pradare));
      }

      return radare;
    }

    /**
     * Vraća radare.
     *
     * @return radare
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Radar> getJSON_reset() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Radar> radare = new ArrayList<Radar>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradare = jb.fromJson(odgovor, Radar[].class);
        radare.addAll(Arrays.asList(pradare));
      }

      return radare;
    }

    /**
     * Vraća radare s identifikatorom.
     *
     * @param id id radara
     * @return radar
     * @throws ClientErrorException iznimka kod poziva klijentaon
     */
    public Radar getJSON_radar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      Radar radar = new Radar();

      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradare = jb.fromJson(odgovor, Radar[].class);
        radar = pradare[0];
      }

      return radar;
    }

    /**
     * Vraća rezultat provjere radara.
     *
     * @param id id vozila
     * @return boolean uspjeh
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean getJSON_radar_provjeri(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      boolean uspjeh = false;
      resource = resource.path(java.text.MessageFormat.format("{0}/provjeri", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        uspjeh = odgovor.contains("OK");
      }

      return uspjeh;
      
    }

    /**
     * Briše radar s identifikatorom.
     * 
     * @param id identifikator radara
     * @return radare
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean delete_radar(String id) throws ClientErrorException {
      
      System.out.println("Brisem radar s ID-om: " + id);
      
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Response restOdgovor = resource.request().delete();
      
      return restOdgovor.getStatus() == 200;
    }
    
    /**
     * Briše sve radare.
     * 
     * @return uspjeh
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean delete_radare() throws ClientErrorException {
      WebTarget resource = webTarget;
      Response restOdgovor = resource.request().delete();
      return restOdgovor.getStatus() == 200;
    }

    /**
     * Close.
     */
    public void close() {
      client.close();
    }
  }
}
