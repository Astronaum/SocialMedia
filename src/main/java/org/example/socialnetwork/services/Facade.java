package org.example.socialnetwork.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.entities.Relationship;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Facade {

    @PersistenceContext
    private EntityManager entityManager;

    public Person getPerson(Long id) {
        return entityManager.find(Person.class, id);
    }

    @Transactional
    public Person createPerson(Person person) {
        entityManager.persist(person);
        return person;
    }

    public List<Person> getAllPersons() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Transactional
    public void createBidirectionalRelationship(Relationship relationship) {
        if (relationship.getPersonA().equals(relationship.getPersonB())) {
            throw new IllegalArgumentException("Une personne ne peut pas avoir une relation avec elle-même.");
        }

        // Ensure personA and personB are managed entities by merging them if necessary
        Person personA = entityManager.find(Person.class, relationship.getPersonA().getId());
        Person personB = entityManager.find(Person.class, relationship.getPersonB().getId());

        if (personA == null || personB == null) {
            throw new IllegalStateException("Person(s) involved in the relationship do not exist in the database.");
        }

        // Set the managed entities into the relationship
        relationship.setPersonA(personA);
        relationship.setPersonB(personB);

        // Persist the initial relationship
        entityManager.persist(relationship);

        // Create and persist the reverse relationship
        Relationship reverseRelationship = new Relationship(
                personB,
                personA,
                relationship.getTypeRelation()
        );

        entityManager.persist(reverseRelationship);
    }

    public boolean relationshipExists(Person personA, Person personB) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Relationship r WHERE " +
                                "(r.personA = :personA AND r.personB = :personB) OR " +
                                "(r.personA = :personB AND r.personB = :personA)", Long.class)
                .setParameter("personA", personA)
                .setParameter("personB", personB)
                .getSingleResult();
        return count > 0;
    }

    public List<Relationship> getAllRelationships() {
        return entityManager.createQuery("SELECT r FROM Relationship r", Relationship.class).getResultList();
    }

    public List<Relationship> getUniqueRelationships() {
        return entityManager.createQuery(
                        "SELECT r FROM Relationship r WHERE r.personA.id < r.personB.id", Relationship.class)
                .getResultList();
    }
}