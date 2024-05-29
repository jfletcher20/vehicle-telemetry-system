/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.jfletcher20.jpa.pomocnici;


import java.util.List;
import edu.unizg.foi.nwtis.dkermek.jpa.entiteti.Korisnici_;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author Dragutin Kermek
 */
@Named
@Stateless
public class KorisniciFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Korisnici korisnici) {
    em.persist(korisnici);
  }

  public void edit(Korisnici korisnici) {
    em.merge(korisnici);
  }

  public void remove(Korisnici korisnici) {
    em.remove(em.merge(korisnici));
  }

  public Korisnici find(Object id) {
    return em.find(Korisnici.class, id);
  }

  public List<Korisnici> findAll() {
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    cq.select(cq.from(Korisnici.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Korisnici> findRange(int[] range) {
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    cq.select(cq.from(Korisnici.class));
    TypedQuery<Korisnici> q = em.createQuery(cq);
    q.setMaxResults(range[1] - range[0]);
    q.setFirstResult(range[0]);
    return q.getResultList();
  }

  public List<Korisnici> findAll(String prezime, String ime) {
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    Root<Korisnici> korisnici = cq.from(Korisnici.class);
    Expression<String> zaPrezime = korisnici.get(Korisnici_.prezime);
    Expression<String> zaIme = korisnici.get(Korisnici_.ime);
    cq.where(cb.and(cb.like(zaPrezime, prezime), cb.like(zaIme, ime)));
    TypedQuery<Korisnici> q = em.createQuery(cq);
    return q.getResultList();
  }

  public int count() {
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(Korisnici.class)));
    return ((Long) em.createQuery(cq).getSingleResult()).intValue();
  }
}
