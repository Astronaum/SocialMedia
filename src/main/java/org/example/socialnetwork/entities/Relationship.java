package org.example.socialnetwork.entities;

import jakarta.persistence.*;

@Entity
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "personA_id")
    private Long personAId;

    @Column(name = "personB_id")
    private Long personBId;

    @Enumerated(EnumType.STRING)
    private RelationType typeRelation;

    public Relationship(){}

    public Relationship(Long id, Long personAId, Long personBId, RelationType typeRelation) {
        this.id = id;
        this.personAId = personAId;
        this.personBId = personBId;
        this.typeRelation = typeRelation;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonAId() {
        return personAId;
    }

    public void setPersonAId(Long personAId) {
        this.personAId = personAId;
    }

    public Long getPersonBId() {
        return personBId;
    }

    public void setPersonBId(Long personBId) {
        this.personBId = personBId;
    }

    public RelationType getTypeRelation() {
        return typeRelation;
    }

    public void setTypeRelation(RelationType typeRelation) {
        this.typeRelation = typeRelation;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "id=" + id +
                ", personAId=" + personAId +
                ", personBId=" + personBId +
                ", typeRelation=" + typeRelation +
                '}';
    }
}
