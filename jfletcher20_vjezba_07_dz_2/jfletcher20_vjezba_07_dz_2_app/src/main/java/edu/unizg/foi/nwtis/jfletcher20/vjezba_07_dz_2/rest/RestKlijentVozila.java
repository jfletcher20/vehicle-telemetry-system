package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.pomocnici.Parsiraj;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentVoznje.
 */
public class RestKlijentVozila {

  /**
   * Konstruktor klase.
   */
  public RestKlijentVozila() {}

  /**
   * Vraća voznje u intervalu od do.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return voznje
   */
  public List<Voznja> getVoznjeJSON_od_do(long odVremena, long doVremena) {
    RestPraceneVoznje rk = new RestPraceneVoznje();
    List<Voznja> voznje = rk.getJSON_od_do(odVremena, doVremena);
    return voznje;
  }

  /**
   * Vraća voznje za vozilo.
   *
   * @param id id vozila
   * @return voznje
   */
  public List<Voznja> getVoznjeJSON_vozilo(String id) {
    RestPraceneVoznje rk = new RestPraceneVoznje();
    List<Voznja> voznje = rk.getJSON_vozilo(id);
    return voznje;
  }

  /**
   * Vraća voznje za vozilo u intervalu od do..
   *
   * @param id id vozila
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return voznje
   */
  public List<Voznja> getVoznjeJSON_vozilo_od_do(String id, long odVremena, long doVremena) {
    RestPraceneVoznje rk = new RestPraceneVoznje();
    List<Voznja> voznje = rk.getJSON_vozilo_od_do(id, odVremena, doVremena);

    return voznje;
  }

  /**
   * Dodaje voznju.
   *
   * @param voznja voznja
   * @return true, ako je uspješno
   */
  public boolean postVoznjaJSON(Voznja voznja) {
    RestPraceneVoznje rk = new RestPraceneVoznje();
    var odgovor = rk.postJSON(voznja);
    return odgovor;
  }

  /**
   * Dodaje voznju.
   *
   * @param voznja voznja
   * @return true, ako je uspješno
   */
  public boolean postVoznjaJSON(PodaciVozila voznja) {
    Voznja voznja2 = new Voznja(voznja.id(), voznja.broj(), voznja.vrijeme(), voznja.brzina(),
        voznja.snaga(), voznja.struja(), voznja.visina(), voznja.gpsBrzina(), voznja.tempVozila(),
        voznja.postotakBaterija(), voznja.naponBaterija(), voznja.kapacitetBaterija(),
        voznja.tempBaterija(), voznja.preostaloKm(), voznja.ukupnoKm(), voznja.gpsSirina(),
        voznja.gpsDuzina());
    RestPraceneVoznje rk = new RestPraceneVoznje();
    var odgovor = rk.postJSON(voznja2);
    return odgovor;
  }
  /**
   * Klasa RestPraceneVoznje.
   */
  static class RestPraceneVoznje {

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://20.24.5.5:8080/";

    /**
     * Konstruktor klase.
     */
    public RestPraceneVoznje() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/vozila");
    }

    /**
     * Vraća voznje.
     *
     * @return voznje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Voznja> getJSON() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }

    /**
     * Vraća voznje u intervalu od do.
     *
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return voznje
     * @throws ClientErrorException iznimka kod poziva klijentan
     */
    public List<Voznja> getJSON_od_do(long odVremena, long doVremena) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }

    /**
     * Vraća voznje za vozilo.
     *
     * @param id id vozila
     * @return voznje
     * @throws ClientErrorException iznimka kod poziva klijentaon
     */
    public List<Voznja> getJSON_vozilo(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }

    /**
     * Vraća voznje za vozilo u intervalu.
     *
     * @param id id vozila
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return voznje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Voznja> getJSON_vozilo_od_do(String id, long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznje = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznje));
      }

      return voznje;
    }

    /**
     * Dodaje voznju.
     *
     * @param voznja voznja
     * @return true, ako je uspješno
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean postJSON(Voznja voznja) throws ClientErrorException {

      WebTarget resource = webTarget;
      if (voznja == null)
        return false;

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      var odgovor =
          request.post(Entity.entity(voznja, MediaType.APPLICATION_JSON), String.class).toString();
      if (odgovor.trim().length() > 0)
        return true;

      return true;

    }

    /**
     * Close.
     */
    public void close() {
      client.close();
    }

    // /**
    // * Ispis.
    // *
    // * @param p podaci
    // * @param brojRetka broj retka
    // */
    // private void ispis(Voznja p) {
    // System.out.println("Komanda: VOZILO " + p.getId() + " " + p.getBroj() + " " + p.getVrijeme()
    // + " " + p.getBrzina() + " " + p.getSnaga() + " " + p.getStruja() + " " + p.getVisina()
    // + " " + p.getGpsBrzina() + " " + p.getTempVozila() + " " + p.getPostotakBaterija() + " "
    // + p.getNaponBaterija() + " " + p.getKapacitetBaterija() + " " + p.getTempBaterija() + " "
    // + p.getPreostaloKm() + " " + p.getUkupnoKm() + " " + p.getGpsSirina() + " "
    // + p.getGpsDuzina());
    // }
  }
}
