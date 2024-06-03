/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jpa.entiteti;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author NWTiS_1
 */
@Entity
@Table(name = "DNEVNIK_RADA")
@NamedQueries({
    @NamedQuery(name = "DnevnikRada.findAll", query = "SELECT d FROM DnevnikRada d")})
public class DnevnikRada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    @Size(max = 20)
    private String korisnickoime;
    @Size(max = 99)
    private String adresaracunala;
    @Size(max = 50)
    private String ipadresaracunala;
    @Size(max = 30)
    private String nazivos;
    @Size(max = 20)
    private String verzijavm;
    @Size(max = 512)
    private String opisrada;

    public DnevnikRada() {
    }

    public DnevnikRada(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getKorisnickoime() {
        return korisnickoime;
    }

    public void setKorisnickoime(String korisnickoime) {
        this.korisnickoime = korisnickoime;
    }

    public String getAdresaracunala() {
        return adresaracunala;
    }

    public void setAdresaracunala(String adresaracunala) {
        this.adresaracunala = adresaracunala;
    }

    public String getIpadresaracunala() {
        return ipadresaracunala;
    }

    public void setIpadresaracunala(String ipadresaracunala) {
        this.ipadresaracunala = ipadresaracunala;
    }

    public String getNazivos() {
        return nazivos;
    }

    public void setNazivos(String nazivos) {
        this.nazivos = nazivos;
    }

    public String getVerzijavm() {
        return verzijavm;
    }

    public void setVerzijavm(String verzijavm) {
        this.verzijavm = verzijavm;
    }

    public String getOpisrada() {
        return opisrada;
    }

    public void setOpisrada(String opisrada) {
        this.opisrada = opisrada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DnevnikRada)) {
            return false;
        }
        DnevnikRada other = (DnevnikRada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jpa.entiteti.DnevnikRada[ id=" + id + " ]";
    }
    
}
