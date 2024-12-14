package org.example.socialnetwork.services;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.entities.RelationType;
import org.example.socialnetwork.entities.Relationship;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Person personA = entityManager.find(Person.class, relationship.getPersonA().getId());
        Person personB = entityManager.find(Person.class, relationship.getPersonB().getId());

        if (personA == null || personB == null) {
            throw new IllegalStateException("Une ou plusieurs personnes n'existent pas dans la base de données.");
        }

        Relationship managedRelationship = new Relationship(personA, personB, relationship.getTypeRelation());
        entityManager.persist(managedRelationship);

        Relationship reverseRelationship = new Relationship(personB, personA, relationship.getTypeRelation());
        entityManager.persist(reverseRelationship);

        System.out.println("Relation créée avec succès entre " + personA.getNom() + " et " + personB.getNom());
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

    public List<Relationship> getRelationshipsForPerson(Long personId) {
        return entityManager.createQuery(
                        "SELECT r FROM Relationship r WHERE r.personA.id = :personId OR r.personB.id = :personId", Relationship.class)
                .setParameter("personId", personId)
                .getResultList();
    }


    public List<Person> getPersonsRelatedTo(Long personId) {
        return entityManager.createQuery(
                        "SELECT DISTINCT p FROM Relationship r JOIN r.personB p WHERE r.personA.id = :personId", Person.class)
                .setParameter("personId", personId)
                .getResultList();
    }

    public List<Person> searchPersonsByNameKeywords(String[] keywords) {
        if (keywords == null || keywords.length == 0) {
            return new ArrayList<>();
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT p FROM Person p WHERE ");
        for (int i = 0; i < keywords.length; i++) {
            queryBuilder.append("(LOWER(p.nom) LIKE LOWER(:keyword").append(i).append(") OR LOWER(p.prenom) LIKE LOWER(:keyword").append(i).append("))");
            if (i < keywords.length - 1) {
                queryBuilder.append(" OR ");
            }
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Person.class);
        for (int i = 0; i < keywords.length; i++) {
            query.setParameter("keyword" + i, "%" + keywords[i] + "%");
        }

        return query.getResultList();
    }

    public List<Person> searchPersonsByFullName(String fullName) {
        return entityManager.createQuery(
                        "SELECT p FROM Person p WHERE " +
                                "LOWER(TRIM(CONCAT(p.nom, ' ', p.prenom))) = LOWER(TRIM(:fullName))", Person.class)
                .setParameter("fullName", fullName.trim())
                .getResultList();
    }



    public List<Person> searchPersonsByDescriptionKeywords(String[] keywords) {
        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT p FROM Person p WHERE ");
        for (int i = 0; i < keywords.length; i++) {
            queryBuilder.append("LOWER(p.description) LIKE LOWER(:keyword").append(i).append(")");
            if (i < keywords.length - 1) {
                queryBuilder.append(" OR ");
            }
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Person.class);
        for (int i = 0; i < keywords.length; i++) {
            query.setParameter("keyword" + i, "%" + keywords[i] + "%");
        }

        return query.getResultList();
    }

    public List<Person> searchPersonsByKeywords(String searchInput) {
        String[] keywords = searchInput.trim().split("\\s+");
        if (keywords.length == 0) {
            return new ArrayList<>();
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT p FROM Person p WHERE ");
        for (int i = 0; i < keywords.length; i++) {
            queryBuilder.append("LOWER(p.nom) LIKE LOWER(:keyword").append(i)
                    .append(") OR LOWER(p.prenom) LIKE LOWER(:keyword").append(i).append(")");
            if (i < keywords.length - 1) {
                queryBuilder.append(" OR ");
            }
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Person.class);
        for (int i = 0; i < keywords.length; i++) {
            query.setParameter("keyword" + i, "%" + keywords[i] + "%");
        }

        return query.getResultList();
    }


    public boolean relationshipWithTypeExists(Person personA, Person personB, RelationType typeRelation) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Relationship r WHERE " +
                                "r.personA = :personA AND r.personB = :personB AND r.typeRelation = :typeRelation", Long.class)
                .setParameter("personA", personA)
                .setParameter("personB", personB)
                .setParameter("typeRelation", typeRelation)
                .getSingleResult();
        return count > 0;
    }

    public List<Person> getPersonsWithoutRelations() {
        return entityManager.createQuery(
                        "SELECT p FROM Person p WHERE p.relationsAsPersonA IS EMPTY AND p.relationsAsPersonB IS EMPTY", Person.class)
                .getResultList();
    }

    public List<Person> getPersonsWithMoreThanNRelations(int n) {
        return entityManager.createQuery(
                        "SELECT p FROM Person p WHERE SIZE(p.relationsAsPersonA) + SIZE(p.relationsAsPersonB) >= :n", Person.class)
                .setParameter("n", n)
                .getResultList();
    }



    public List<Object[]> getRelationTypesSortedByOccurrences() {
        return entityManager.createQuery(
                        "SELECT r.typeRelation, COUNT(r) FROM Relationship r GROUP BY r.typeRelation ORDER BY COUNT(r) DESC", Object[].class)
                .getResultList();
    }

    public List<Set<Person>> findConnectedComponents() {
        List<Person> allPersons = getAllPersons();
        Set<Person> visited = new HashSet<>();
        List<Set<Person>> components = new ArrayList<>();

        for (Person person : allPersons) {
            if (!visited.contains(person)) {
                Set<Person> component = new HashSet<>();
                exploreComponent(person, component, visited);
                components.add(component);
            }
        }
        return components;
    }

    private void exploreComponent(Person person, Set<Person> component, Set<Person> visited) {
        visited.add(person);
        component.add(person);

        List<Person> neighbors = getNeighbors(person);
        for (Person neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                exploreComponent(neighbor, component, visited);
            }
        }
    }
    private List<Person> getNeighbors(Person person) {
        return entityManager.createQuery(
                        "SELECT DISTINCT r.personB FROM Relationship r WHERE r.personA = :person UNION " +
                                "SELECT DISTINCT r.personA FROM Relationship r WHERE r.personB = :person", Person.class)
                .setParameter("person", person)
                .getResultList();
    }
    public List<Person> getPersonsWithoutRelationships() {
        return entityManager.createQuery(
                        "SELECT p FROM Person p WHERE p.relationsAsPersonA IS EMPTY AND p.relationsAsPersonB IS EMPTY", Person.class)
                .getResultList();
    }
    public List<Person> getPersonsWithMoreThanNRelationships(int n) {
        return entityManager.createQuery(
                        "SELECT DISTINCT p FROM Person p " +
                                "WHERE (SELECT COUNT(r) FROM Relationship r WHERE r.personA = p OR r.personB = p) >= :n", Person.class)
                .setParameter("n", n)
                .getResultList();
    }

    public List<Person> getPersonsWithMultipleRelationTypes() {
        return entityManager.createQuery(
                        "SELECT DISTINCT r.personA FROM Relationship r " +
                                "WHERE (SELECT COUNT(DISTINCT r2.typeRelation) " +
                                "       FROM Relationship r2 " +
                                "       WHERE r2.personA = r.personA) > 1", Person.class)
                .getResultList();
    }




}
