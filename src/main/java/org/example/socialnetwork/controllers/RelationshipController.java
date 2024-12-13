package org.example.socialnetwork.controllers;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.entities.Relationship;
import org.example.socialnetwork.entities.RelationType;
import org.example.socialnetwork.services.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/relationship")
public class RelationshipController {

    @Autowired
    private Facade facade;

    @GetMapping("/create")
    public String showCreateRelationshipForm(Model model) {
        model.addAttribute("relationship", new Relationship());
        model.addAttribute("persons", facade.getAllPersons());
        model.addAttribute("relationTypes", RelationType.values());
        return "createRelationship";
    }

    @PostMapping("/save")
    @Transactional
    public String saveRelationship(@ModelAttribute("relationship") Relationship relationship, Model model) {
        try {
            // Retrieve and set the actual Person entities based on their IDs
            Person personA = facade.getPerson(relationship.getPersonA().getId());
            Person personB = facade.getPerson(relationship.getPersonB().getId());

            if (personA == null || personB == null) {
                throw new IllegalArgumentException("Les personnes sélectionnées n'existent pas.");
            }
            relationship.setPersonA(personA);
            relationship.setPersonB(personB);

            // Validation: Check if PersonA and PersonB are the same
            if (personA.getId().equals(personB.getId())) {
                model.addAttribute("errorMessage", "Une personne ne peut pas avoir une relation avec elle-même.");
                throw new IllegalArgumentException("Une personne ne peut pas avoir une relation avec elle-même.");
            }

            // Check if a relationship of the same type already exists
            if (facade.relationshipWithTypeExists(personA, personB, relationship.getTypeRelation())) {
                model.addAttribute("errorMessage", "La relation de type " + relationship.getTypeRelation() +
                        " existe déjà entre " + personA.getNom() + " et " + personB.getNom() + ".");
                throw new IllegalArgumentException("La relation de ce type existe déjà.");
            }

            // Save the bidirectional relationship
            facade.createBidirectionalRelationship(relationship);
            return "redirect:/relationship/list"; // Redirect to the relationship list on success
        } catch (IllegalArgumentException e) {
            // Handle validation errors by returning to the form with the error message
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite: " + e.getMessage());
        }

        model.addAttribute("persons", facade.getAllPersons());
        model.addAttribute("relationTypes", RelationType.values());
        return "createRelationship";
    }


@GetMapping("/list")
    public String listRelationships(Model model) {
        model.addAttribute("relationships", facade.getUniqueRelationships());
        return "listRelationships";
    }

    @GetMapping("/relation-types")
    public String getRelationTypesSorted(Model model) {
        model.addAttribute("relationTypes", facade.getRelationTypesSortedByOccurrences());
        return "listRelationTypes";
    }

    @GetMapping("/connected-components")
    public String getConnectedComponents(Model model) {
        model.addAttribute("components", facade.findConnectedComponents());
        return "listComponents";
    }

    @GetMapping("/search")
    public String searchRelationshipsByFullName(@RequestParam("personQuery") String personQuery, Model model) {
        if (personQuery == null || personQuery.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Veuillez entrer le nom complet pour effectuer une recherche.");
            model.addAttribute("relationships", new ArrayList<>());
            return "listRelationships";
        }

        // Search for persons matching the full name
        List<Person> matchingPersons = facade.searchPersonsByFullName(personQuery.trim());

        if (matchingPersons.isEmpty()) {
            model.addAttribute("errorMessage", "Aucune personne trouvée correspondant à votre recherche.");
            model.addAttribute("relationships", new ArrayList<>());
        } else {
            // Get relationships for the matching persons
            List<Relationship> relationships = new ArrayList<>();
            for (Person person : matchingPersons) {
                relationships.addAll(facade.getRelationshipsForPerson(person.getId()));
            }

            model.addAttribute("relationships", relationships);
        }

        model.addAttribute("personSearched", personQuery);
        return "listRelationships";
    }


    @GetMapping("/persons")
    @ResponseBody
    public List<Person> getAllPersonsForDropdown() {
        return facade.getAllPersons();
    }

    @GetMapping("/persons/search")
    @ResponseBody
    public List<Person> searchPersonsForDropdown(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return facade.searchPersonsByKeywords(keyword);
    }
}
