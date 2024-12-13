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
            Person personA = facade.getPerson(relationship.getPersonA().getId());
            Person personB = facade.getPerson(relationship.getPersonB().getId());

            if (personA == null || personB == null) {
                model.addAttribute("errorMessage", "Les personnes sélectionnées n'existent pas.");
                return "createRelationship";
            }

            if (personA.getId().equals(personB.getId())) {
                model.addAttribute("errorMessage", "Une personne ne peut pas avoir une relation avec elle-même.");
                return "createRelationship";
            }

            if (facade.relationshipWithTypeExists(personA, personB, relationship.getTypeRelation())) {
                model.addAttribute("errorMessage", "La relation de type " + relationship.getTypeRelation() +
                        " existe déjà entre " + personA.getNom() + " et " + personB.getNom() + ".");
                return "createRelationship";
            }

            facade.createBidirectionalRelationship(relationship);
            return "redirect:/relationship/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Une erreur inattendue s'est produite : " + e.getMessage());
            return "createRelationship";
        }
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
    public String searchRelationshipsByPerson(@RequestParam("personId") Long personId, Model model) {
        if (personId == null) {
            model.addAttribute("errorMessage", "Veuillez sélectionner une personne.");
            model.addAttribute("relationships", new ArrayList<>());
            return "listRelationships";
        }

        Person person = facade.getPerson(personId);
        if (person == null) {
            model.addAttribute("errorMessage", "Personne introuvable.");
            model.addAttribute("relationships", new ArrayList<>());
            return "listRelationships";
        }

        List<Relationship> relationships = facade.getRelationshipsForPerson(personId);
        model.addAttribute("personSearched", person);
        model.addAttribute("relationships", relationships);
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
