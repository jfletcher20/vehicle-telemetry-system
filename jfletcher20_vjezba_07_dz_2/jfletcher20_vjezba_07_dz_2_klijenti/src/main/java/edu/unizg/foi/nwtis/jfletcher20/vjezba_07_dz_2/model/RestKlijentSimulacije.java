package edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.jfletcher20.vjezba_07_dz_2.podaci.Simulacija;
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
import slusac.AppContextListener;

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
   * @param nazivDatoteke podaci vozila
   * @param idVozila id vozila
   * @param trajanjeSek trajanje sekunde unutar simulacije
   * @param trajanjePauze trajanje pauze između simulacija podataka
   * @return true, ako je uspješno
   */
  public boolean postVoznjaJSON(String nazivDatoteke, int idVozila, int trajanjeSek,
      int trajanjePauze) {
    RestVoznje rk = new RestVoznje();
    var odgovor = rk.postJSON(nazivDatoteke, idVozila, trajanjeSek, trajanjePauze);
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

    private long razlikaVremena(Voznja p, long vrijemeZadnjeVoznje) {
      if (vrijemeZadnjeVoznje < 0)
        vrijemeZadnjeVoznje = 0;
      long razlika = p.getVrijeme() - vrijemeZadnjeVoznje;
      if (razlika >= p.getVrijeme())
        razlika = 0;
      return razlika;
    }

    private Voznja voznjaIzCSV(int idVozila, String podaciVozilaDatoteka, int brojRetka) {
      try (BufferedReader reader = new BufferedReader(new FileReader(podaciVozilaDatoteka))) {
        for (int i = 0; i < brojRetka; i++)
          reader.readLine();
        String row = reader.readLine();
        if (row == null)
          return null;
        String[] data = row.split(",");
        return new Voznja(idVozila, brojRetka, Parsiraj.l(data[0]), Parsiraj.d(data[1]),
            Parsiraj.d(data[2]), Parsiraj.d(data[3]), Parsiraj.d(data[4]), Parsiraj.d(data[5]),
            Parsiraj.i(data[6]), Parsiraj.i(data[7]), Parsiraj.d(data[8]), Parsiraj.i(data[9]),
            Parsiraj.i(data[10]), Parsiraj.d(data[11]), Parsiraj.d(data[12]), Parsiraj.d(data[13]),
            Parsiraj.d(data[14]));
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    /**
     * Priceka razliku.
     * 
     * @param razlika razlika
     * @param p podaci
     * @return razlika
     * @throws InterruptedException
     */
    private long pricekaj(long razlika, Voznja p, int trajanjeSek, long zadnjeVrijeme)
        throws InterruptedException {
      if ((long) (razlika * trajanjeSek / 1000.0) > 0)
        Thread.sleep((long) (razlika * trajanjeSek / 1000.0));
      razlika = razlikaVremena(p, zadnjeVrijeme);
      return razlika;
    }

    /**
     * Dodaje simulaciju voznje.
     *
     * @param nazivDatoteke podaci vozila
     * @param idVozila id vozila
     * @param trajanjeSek trajanje sekunde unutar simulacije
     * @param trajanjePauze trajanje pauze između simulacija podataka
     * @return true, ako je uspješno
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean postJSON(String nazivDatoteke, int idVozila, int trajanjeSek, int trajanjePauze)
        throws ClientErrorException {
      if (nazivDatoteke == null || trajanjeSek == 0 || trajanjePauze == 0
          || nazivDatoteke.length() == 0 || idVozila < 0 || trajanjeSek < 0 || trajanjePauze < 0)
        return false;
      Invocation.Builder request = webTarget.request(MediaType.APPLICATION_JSON);
      int brojRetka = 1;
      String row = "";
      long razlika = 0, zadnjeVrijeme = 0;
      try {
        File file = new File(AppContextListener.getAppPath() + nazivDatoteke);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        if (reader.readLine().split(",")[0].matches("-?\\d+")) {
          brojRetka = 0;
          reader.close();
          reader = new BufferedReader(new FileReader(file));
        }
        while ((row = reader.readLine()) != null) {
          var data = row.split(",");
          Voznja p = podaciUVoznju(data, idVozila, brojRetka);
          razlika = pricekaj(razlika, p, trajanjeSek, zadnjeVrijeme);
          zadnjeVrijeme = p.getVrijeme();
          request.post(Entity.entity(p, MediaType.APPLICATION_JSON), String.class);
          Thread.sleep(trajanjePauze);
        }
        reader.close();
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
      return true;
    }

    /**
     * Close.
     */
    public void close() {
      client.close();
    }

    /**
     * Ispis.
     * 
     * @param p podaci
     * @param brojRetka broj retka
     */
    private void ispis(Voznja p, int brojRetka) {
      System.out.println("Komanda: VOZILO " + p.getId() + " " + brojRetka + " " + p.getVrijeme()
          + " " + p.getBrzina() + " " + p.getSnaga() + " " + p.getStruja() + " " + p.getVisina()
          + " " + p.getGpsBrzina() + " " + p.getTempVozila() + " " + p.getPostotakBaterija() + " "
          + p.getNaponBaterija() + " " + p.getKapacitetBaterija() + " " + p.getTempBaterija() + " "
          + p.getPreostaloKm() + " " + p.getUkupnoKm() + " " + p.getGpsSirina() + " "
          + p.getGpsDuzina());
    }

    /**
     * Pricekaj.
     * 
     * @param razlika razlika
     * @param p podaci
     * @throws InterruptedException iznimka
     */
    private void pricekaj(long razlika, Voznja p, int trajanjeSek) throws InterruptedException {
      if (razlika < 0)
        razlika = razlika * -1;
      if ((long) (razlika * trajanjeSek / 1000.0) > 0)
        Thread.sleep((long) (razlika * trajanjeSek / 1000.0));
      else
        Thread.sleep(0);
    }

    /**
     * Pretvara podatke u voznju.
     * 
     * @param podaci podaci
     * @param idVozila id vozila
     * @param brojRetka broj retka
     * @return voznja
     */
    private Voznja podaciUVoznju(String[] podaci, int idVozila, int brojRetka) {
      return new Voznja(idVozila, brojRetka, Parsiraj.l(podaci[0]), Parsiraj.d(podaci[1]),
          Parsiraj.d(podaci[2]), Parsiraj.d(podaci[3]), Parsiraj.d(podaci[4]),
          Parsiraj.d(podaci[5]), Parsiraj.i(podaci[6]), Parsiraj.i(podaci[7]),
          Parsiraj.d(podaci[8]), Parsiraj.i(podaci[9]), Parsiraj.i(podaci[10]),
          Parsiraj.d(podaci[11]), Parsiraj.d(podaci[12]), Parsiraj.d(podaci[13]),
          Parsiraj.d(podaci[14]));
    }
  }
}
