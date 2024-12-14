package org.example.socialnetwork.controllers;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.services.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final Facade facade;

    @Autowired
    public PersonController(Facade facade) {
        this.facade = facade;
    }

    @GetMapping
    public String getAllPersons(Model model) {
        model.addAttribute("persons", facade.getAllPersons());
        return "listPersons"; // Template that displays all persons
    }

    @GetMapping("/new")
    public String showCreatePersonForm(Model model) {
        model.addAttribute("person", new Person()); // Create a new Person object for the form
        return "createPerson"; // Show form to create a new person
    }

    @PostMapping("/save")
    @Transactional
    public String createPerson(Person person) {
        facade.createPerson(person); // Save the person
        return "redirect:/persons"; // Redirects to list of persons after saving
    }
    @GetMapping("/related")
    public String getRelatedPersons(@RequestParam Long personId, Model model) {
        model.addAttribute("persons", facade.getPersonsRelatedTo(personId));
        return "listPersons";
    }

    @GetMapping("/search")
    public String searchPersons(@RequestParam("keyword") String keyword, Model model) {
        if (keyword == null || keyword.trim().isEmpty()) {
            model.addAttribute("persons", new ArrayList<>()); // Return an empty list if no keyword is provided
        } else {
            String searchInput = keyword.trim().toLowerCase();

            List<Person> results = new ArrayList<>();

            if (searchInput.contains(" ")) {
                // Treat it as a full name
                results.addAll(facade.searchPersonsByFullName(searchInput));
            } else {
                // Treat it as a partial match (search in both name and description)
                String[] keywords = searchInput.split("\\s+");

                List<Person> personsByName = facade.searchPersonsByNameKeywords(keywords);
                List<Person> personsByDescription = facade.searchPersonsByDescriptionKeywords(keywords);

                // Combine results while avoiding duplicates
                Set<Person> combinedResults = new HashSet<>(personsByName);
                combinedResults.addAll(personsByDescription);

                results = new ArrayList<>(combinedResults);
            }

            model.addAttribute("persons", results);
        }
        return "listPersons";
    }






}
