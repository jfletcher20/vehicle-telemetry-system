package edu.unizg.foi.nwtis.dkermek.jpa.entiteti;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Grupe;
import edu.unizg.foi.nwtis.jfletcher20.jpa.entiteti.Korisnici;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-05-29T13:27:42", comments="EclipseLink-4.0.2.v20230616-r3bfa6ac6ddf76d7909adc5ea7ecaa47c02c007ed")
@StaticMetamodel(Korisnici.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class Korisnici_ { 

    public static volatile SingularAttribute<Korisnici, String> ime;
    public static volatile SingularAttribute<Korisnici, String> prezime;
    public static volatile SingularAttribute<Korisnici, String> lozinka;
    public static volatile ListAttribute<Korisnici, Grupe> grupes;
    public static volatile SingularAttribute<Korisnici, String> email;
    public static volatile SingularAttribute<Korisnici, String> korisnik;

}