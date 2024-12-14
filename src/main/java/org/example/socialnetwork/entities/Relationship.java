package org.example.socialnetwork.entities;

import jakarta.persistence.*;

@Table(name = "relationship")
@Entity
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personA_id", referencedColumnName = "id")
    private Person personA;

    @ManyToOne
    @JoinColumn(name = "personB_id", referencedColumnName = "id")
    private Person personB;

    @Enumerated(EnumType.STRING)
    private RelationType typeRelation;

    public Relationship() {}

    public Relationship(Person personA, Person personB, RelationType typeRelation) {
        this.personA = personA;
        this.personB = personB;
        this.typeRelation = typeRelation;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Person getPersonA() { return personA; }
    public void setPersonA(Person personA) { this.personA = personA; }

    public Person getPersonB() { return personB; }
    public void setPersonB(Person personB) { this.personB = personB; }

    public RelationType getTypeRelation() { return typeRelation; }
    public void setTypeRelation(RelationType typeRelation) { this.typeRelation = typeRelation; }

    public Relationship createReverse() {
        return new Relationship(this.personB, this.personA, this.typeRelation);
    }

}
