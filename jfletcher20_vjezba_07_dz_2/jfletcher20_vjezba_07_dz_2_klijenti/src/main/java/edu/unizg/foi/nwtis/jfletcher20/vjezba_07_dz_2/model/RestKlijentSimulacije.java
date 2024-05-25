package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Voznja;
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
public class RestKlijentSimulacije {

  /**
   * Konstruktor klase.
   */
  public RestKlijentSimulacije() {}

  /**
   * Vraća voznje u intervalu od do.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return voznje
   */
  public List<Voznja> getVoznjeJSON_od_do(long odVremena, long doVremena) {
    RestVoznje rk = new RestVoznje();
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
    RestVoznje rk = new RestVoznje();
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
    RestVoznje rk = new RestVoznje();
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
    RestVoznje rk = new RestVoznje();
    var odgovor = rk.postJSON(voznja);
    return odgovor;
  }
  
  /**
   * Dodaje simulaciju voznje.
   * 
   * @param podaciVozila podaci vozila
   * @param idVozila id vozila
   * @param trajanjeSek trajanje sekunde unutar simulacije
   * @param trajanjePauze trajanje pauze između simulacija podataka
   * @return true, ako je uspješno
   */
  public boolean postVoznjaJSON(String podaciVozila, int idVozila, int trajanjeSek, int trajanjePauze) {
    RestVoznje rk = new RestVoznje();
    var odgovor = rk.postJSON(podaciVozila, idVozila, trajanjeSek, trajanjePauze);
    return odgovor;
  }

  /**
   * Klasa RestVoznje.
   */
  static class RestVoznje {

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestVoznje() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/simulacije");
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
     * Dodaje simulaciju voznje.
     *
     * @param podaciVozila podaci vozila
     * @param idVozila id vozila
     * @param trajanjeSek trajanje sekunde unutar simulacije
     * @param trajanjePauze trajanje pauze između simulacija podataka
     * @return true, ako je uspješno
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean postJSON(String podaciVozila, int idVozila, int trajanjeSek, int trajanjePauze)
        throws ClientErrorException {

      WebTarget resource = webTarget;
      if (podaciVozila == null || trajanjeSek == 0 || trajanjePauze == 0
          || podaciVozila.length() == 0 || idVozila < 0 || trajanjeSek < 0 || trajanjePauze < 0)
        return false;

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      /*Thread.sleep((long) (simulator.citajCSV() * simulator.trajanjeSek / 1000.0));
          result.get();
          simulator.simulirajVoznju(kanalKlijenta);
          Thread.sleep(simulator.trajanjePauze);*/
      
      /*
       * String vozilo = "";
      while (true) {
        // stop execution for trajanjeSek / 1000
         String linija = citac.readLine();
         int indexLinije = citac.getLineNumber();
         vozilo = "VOZILO " + idVozila + " " + indexLinije + // add data from the file podaciVozila's current line to the vozilo string
        var odgovor =
            request.post(Entity.entity(podaciZaSimulaciju, MediaType.APPLICATION_JSON), String.class).toString();
      }
      */
      
      var odgovor = "ERROR 29 Nije implementirano\n";
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
  }
}
