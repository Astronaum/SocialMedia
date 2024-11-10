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
    public String saveRelationship(@ModelAttribute("relationship") Relationship relationship) {
        // Retrieve and set the actual Person entities based on their IDs
        relationship.setPersonA(facade.getPerson(relationship.getPersonA().getId()));
        relationship.setPersonB(facade.getPerson(relationship.getPersonB().getId()));

        // Save the relationship
        facade.createRelationship(relationship);
        return "redirect:/relationship/list";
    }

    @GetMapping("/list")
    public String listRelationships(Model model) {
        model.addAttribute("relationships", facade.getAllRelationships());
        return "listRelationships";
    }
}
