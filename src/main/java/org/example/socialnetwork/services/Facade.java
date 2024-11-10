package org.example.socialnetwork.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.entities.Relationship;
import org.example.socialnetwork.entities.RelationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Facade {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Person createPerson(Person person) {
        entityManager.persist(person);
        return person;
    }

    public Person getPerson(Long id) {
        return entityManager.find(Person.class, id);
    }

    public List<Person> getAllPersons() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    public Person updatePerson(Person person) {
        return entityManager.merge(person);
    }

    public void deletePerson(Long id) {
        Person person = getPerson(id);
        if (person != null) {
            entityManager.remove(person);
        }
    }

    // CRUD for Relationship
    @Transactional
    public Relationship createRelationship(Relationship relationship) {
        entityManager.persist(relationship);
        return relationship;
    }

    public Relationship getRelationship(Long id) {
        return entityManager.find(Relationship.class, id);
    }

    public List<Relationship> getAllRelationships() {
        return entityManager.createQuery("SELECT r FROM Relationship r", Relationship.class).getResultList();
    }

    public Relationship updateRelationship(Relationship relationship) {
        // Retrieve and update the Relationship object
        return entityManager.merge(relationship);
    }

    public void deleteRelationship(Long id) {
        Relationship relationship = getRelationship(id);
        if (relationship != null) {
            entityManager.remove(relationship);
        }
    }
}
