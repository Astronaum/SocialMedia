package org.example.socialnetwork.model;
import jakarta.persistence.*;

@Entity
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_a_id")
    private Person personA;

    @ManyToOne
    @JoinColumn(name = "person_b_id")
    private Person personB;

    @Enumerated(EnumType.STRING)
    private RelationType typeRelation;

    // Constructeurs, getters et setters
}
