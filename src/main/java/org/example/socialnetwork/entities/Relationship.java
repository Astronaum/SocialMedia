package org.example.socialnetwork.entities;

import jakarta.persistence.*;

@Entity
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Person personA;

    @ManyToOne
    private Person personB;

    @Enumerated(EnumType.STRING)
    private RelationType typeRelation;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPersonA() {
        return personA;
    }

    public void setPersonA(Person personA) {
        this.personA = personA;
    }

    public Person getPersonB() {
        return personB;
    }

    public void setPersonB(Person personB) {
        this.personB = personB;
    }

    public RelationType getTypeRelation() {
        return typeRelation;
    }

    public void setTypeRelation(RelationType typeRelation) {
        this.typeRelation = typeRelation;
    }
}
