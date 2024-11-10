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
    public Relationship createRelationship(Relationship relationship) {
        entityManager.persist(relationship);
        return relationship;
    }

    public List<Relationship> getAllRelationships() {
        return entityManager.createQuery("SELECT r FROM Relationship r", Relationship.class).getResultList();
    }
}
