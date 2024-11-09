package org.example.socialnetwork.model;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "personA", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Relationship> relations = new HashSet<>();

    // Constructeurs, getters et setters
}
