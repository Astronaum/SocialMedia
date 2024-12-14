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
                throw new IllegalArgumentException("Les personnes sélectionnées n'existent pas.");
            }

            relationship.setPersonA(personA);
            relationship.setPersonB(personB);

            if (personA.getId().equals(personB.getId())) {
                model.addAttribute("errorMessage", "Une personne ne peut pas avoir une relation avec elle-même.");
                throw new IllegalArgumentException("Une personne ne peut pas avoir une relation avec elle-même.");
            }

            if (facade.relationshipWithTypeExists(personA, personB, relationship.getTypeRelation())) {
                model.addAttribute("errorMessage", "La relation de type " + relationship.getTypeRelation() + " existe déjà entre " + personA.getNom() + " et " + personB.getNom() + ".");
                return "createRelationship";
            }

            facade.createBidirectionalRelationship(relationship);
            return "redirect:/relationship/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
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

    @GetMapping("/filter")
    public String filterRelationships(@RequestParam(name = "filter") String filter,
                                      @RequestParam(name = "keyword", required = false) String keyword,
                                      @RequestParam(name = "n", required = false) Integer n,
                                      Model model) {
        switch (filter) {
            case "related-to":
                model.addAttribute("persons", facade.getPersonsRelatedTo(Long.parseLong(keyword)));
                break;

            case "without-relations":
                model.addAttribute("persons", facade.getPersonsWithoutRelationships());
                break;
            case "more-than-n":
                model.addAttribute("persons", facade.getPersonsWithMoreThanNRelationships(n));
                break;
            case "multiple-types":
                model.addAttribute("persons", facade.getPersonsWithMultipleRelationTypes());
                break;
            default:
                model.addAttribute("persons", new ArrayList<>());
        }
        return "listPersons";
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
}
