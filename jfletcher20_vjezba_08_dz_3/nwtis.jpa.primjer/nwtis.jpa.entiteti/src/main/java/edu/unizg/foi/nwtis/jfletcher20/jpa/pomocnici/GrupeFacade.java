/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package edu.unizg.foi.nwtis.jfletcher20.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.dkermek.jpa.entiteti.Grupe_;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe;
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
public class GrupeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Grupe grupe) {
    em.persist(grupe);
  }

  public void edit(Grupe grupe) {
    em.merge(grupe);
  }

  public void remove(Grupe grupe) {
    em.remove(em.merge(grupe));
  }

  public Grupe find(Object id) {
    return em.find(Grupe.class, id);
  }

  public List<Grupe> findAll() {
    CriteriaQuery<Grupe> cq = cb.createQuery(Grupe.class);
    cq.select(cq.from(Grupe.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Grupe> findRange(int[] range) {
    CriteriaQuery<Grupe> cq = cb.createQuery(Grupe.class);
    cq.select(cq.from(Grupe.class));
    TypedQuery<Grupe> q = em.createQuery(cq);
    q.setMaxResults(range[1] - range[0]);
    q.setFirstResult(range[0]);
    return q.getResultList();
  }

  public List<Grupe> findAll(String grupa, String naziv) {
    CriteriaQuery<Grupe> cq = cb.createQuery(Grupe.class);
    Root<Grupe> grupe = cq.from(Grupe.class);
    Expression<String> zaGrupu = grupe.get(Grupe_.grupa);
    Expression<String> zaNaziv = grupe.get(Grupe_.naziv);
    cq.where(cb.and(cb.like(zaGrupu, grupa), cb.like(zaNaziv, naziv)));
    TypedQuery<Grupe> q = em.createQuery(cq);
    return q.getResultList();
  }

  public int count() {
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(Grupe.class)));
    return ((Long) em.createQuery(cq).getSingleResult()).intValue();
  }

}
