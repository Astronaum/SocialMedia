package org.example.socialnetwork.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    @OneToMany(mappedBy = "personA", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Relationship> relationsAsPersonA = new HashSet<>();

    @OneToMany(mappedBy = "personB", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Relationship> relationsAsPersonB = new HashSet<>();

    public Person() {}

    public Person(String nom, String prenom, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public Set<Relationship> getRelationsAsPersonA() { return relationsAsPersonA; }
    public void setRelationsAsPersonA(Set<Relationship> relationsAsPersonA) { this.relationsAsPersonA = relationsAsPersonA; }

    public Set<Relationship> getRelationsAsPersonB() { return relationsAsPersonB; }
    public void setRelationsAsPersonB(Set<Relationship> relationsAsPersonB) { this.relationsAsPersonB = relationsAsPersonB; }
}