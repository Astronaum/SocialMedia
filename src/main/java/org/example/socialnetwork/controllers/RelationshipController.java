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
        // Retrieve and set the actual Person entities based on their IDs
        Person personA = facade.getPerson(relationship.getPersonA().getId());
        Person personB = facade.getPerson(relationship.getPersonB().getId());
        relationship.setPersonA(personA);
        relationship.setPersonB(personB);

        // Validation: Check if PersonA and PersonB are the same
        if (personA.getId().equals(personB.getId())) {
            model.addAttribute("errorMessage", "Une personne ne peut pas avoir une relation avec elle-même.");
            model.addAttribute("persons", facade.getAllPersons());
            model.addAttribute("relationTypes", RelationType.values());
            return "createRelationship"; // Return the form view with the error message
        }

        // Save the relationship
        facade.createBidirectionalRelationship(relationship);
        return "redirect:/relationship/list";
    }

    @GetMapping("/list")
    public String listRelationships(Model model) {
        model.addAttribute("relationships", facade.getUniqueRelationships());
        return "listRelationships";
    }
}
