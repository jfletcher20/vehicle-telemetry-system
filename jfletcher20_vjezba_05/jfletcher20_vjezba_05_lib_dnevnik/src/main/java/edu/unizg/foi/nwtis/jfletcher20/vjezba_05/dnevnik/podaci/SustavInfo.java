package edu.unizg.foi.nwtis.jfletcher20.vjezba_05.dnevnik.podaci;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class SustavInfo {
  private final String adresaRacunala;
  private final String ipAdresaRacunala;
  private final String nazivOS;
  private final String proizvodacVM;
  private final String verzijaVM;
  private final String direktorijVM;
  private final String direktorijRadni;
  private final String direktorijKorisnik;

  public SustavInfo() throws UnknownHostException {
    this.adresaRacunala = InetAddress.getLocalHost().getHostName();
    this.ipAdresaRacunala = InetAddress.getLocalHost().getHostAddress();
    this.nazivOS = System.getProperty("os.name");
    this.proizvodacVM = System.getProperty("java.vm.vendor");
    this.verzijaVM = System.getProperty("java.vm.version");
    this.direktorijVM = System.getProperty("java.home");
    this.direktorijRadni = System.getProperty("java.io.tmpdir");
    this.direktorijKorisnik = System.getProperty("user.dir");
  }

  public String getAdresaRacunala() {
    return adresaRacunala;
  }

  public String getIpAdresaRacunala() {
    return ipAdresaRacunala;
  }

  public String getNazivOS() {
    return nazivOS;
  }

  public String getProizvodacVM() {
    return proizvodacVM;
  }

  public String getVerzijaVM() {
    return verzijaVM;
  }

  public String getDirektorijVM() {
    return direktorijVM;
  }

  public String getDirektorijRadni() {
    return direktorijRadni;
  }

  public String getDirektorijKorisnik() {
    return direktorijKorisnik;
  }

}
