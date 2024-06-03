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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author NWTiS_1
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Kazne.findAll", query = "SELECT k FROM Kazne k")})
public class Kazne implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer rb;
    @Basic(optional = false)
    @NotNull
    private long vrijemepocetak;
    @Basic(optional = false)
    @NotNull
    private long vrijemekraj;
    @Basic(optional = false)
    @NotNull
    private double brzina;
    @Basic(optional = false)
    @NotNull
    private double gpssirina;
    @Basic(optional = false)
    @NotNull
    private double gpsduzina;
    @Basic(optional = false)
    @NotNull
    private double gpssirinaradar;
    @Basic(optional = false)
    @NotNull
    private double gpsduzinaradar;
    @JoinColumn(name = "ID", referencedColumnName = "VOZILO")
    @ManyToOne(optional = false)
    private Vozila id;

    public Kazne() {
    }

    public Kazne(Integer rb) {
        this.rb = rb;
    }

    public Kazne(Integer rb, long vrijemepocetak, long vrijemekraj, double brzina, double gpssirina, double gpsduzina, double gpssirinaradar, double gpsduzinaradar) {
        this.rb = rb;
        this.vrijemepocetak = vrijemepocetak;
        this.vrijemekraj = vrijemekraj;
        this.brzina = brzina;
        this.gpssirina = gpssirina;
        this.gpsduzina = gpsduzina;
        this.gpssirinaradar = gpssirinaradar;
        this.gpsduzinaradar = gpsduzinaradar;
    }

    public Integer getRb() {
        return rb;
    }

    public void setRb(Integer rb) {
        this.rb = rb;
    }

    public long getVrijemepocetak() {
        return vrijemepocetak;
    }

    public void setVrijemepocetak(long vrijemepocetak) {
        this.vrijemepocetak = vrijemepocetak;
    }

    public long getVrijemekraj() {
        return vrijemekraj;
    }

    public void setVrijemekraj(long vrijemekraj) {
        this.vrijemekraj = vrijemekraj;
    }

    public double getBrzina() {
        return brzina;
    }

    public void setBrzina(double brzina) {
        this.brzina = brzina;
    }

    public double getGpssirina() {
        return gpssirina;
    }

    public void setGpssirina(double gpssirina) {
        this.gpssirina = gpssirina;
    }

    public double getGpsduzina() {
        return gpsduzina;
    }

    public void setGpsduzina(double gpsduzina) {
        this.gpsduzina = gpsduzina;
    }

    public double getGpssirinaradar() {
        return gpssirinaradar;
    }

    public void setGpssirinaradar(double gpssirinaradar) {
        this.gpssirinaradar = gpssirinaradar;
    }

    public double getGpsduzinaradar() {
        return gpsduzinaradar;
    }

    public void setGpsduzinaradar(double gpsduzinaradar) {
        this.gpsduzinaradar = gpsduzinaradar;
    }

    public Vozila getId() {
        return id;
    }

    public void setId(Vozila id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rb != null ? rb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kazne)) {
            return false;
        }
        Kazne other = (Kazne) object;
        if ((this.rb == null && other.rb != null) || (this.rb != null && !this.rb.equals(other.rb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jpa.entiteti.Kazne[ rb=" + rb + " ]";
    }
    
}
