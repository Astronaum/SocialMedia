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

            // Check if the relationship already exists
            if (facade.relationshipExists(personA, personB)) {
                model.addAttribute("errorMessage", "La relation existe déjà entre " + personA.getNom() + " et " + personB.getNom() + ".");
                throw new IllegalArgumentException("La relation existe déjà.");
            }

            // Save the relationship
            facade.createBidirectionalRelationship(relationship);

            return "redirect:/relationship/list"; // Redirect to the relationship list on success

        } catch (IllegalArgumentException e) {
            // Handle validation errors by returning to the form with the error message
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite: " + e.getMessage());
        }

        // Repopulate form data and return to the form view
        model.addAttribute("persons", facade.getAllPersons());
        model.addAttribute("relationTypes", RelationType.values());
        return "createRelationship";
    }


    @GetMapping("/list")
    public String listRelationships(Model model) {
        model.addAttribute("relationships", facade.getUniqueRelationships());
        return "listRelationships";
    }
}
