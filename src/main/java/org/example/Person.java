package org.example;

import jakarta.persistence.*;

import javax.management.relation.Relation;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private Date dateNaissance;



    // Constructeurs, getters et setters
}
