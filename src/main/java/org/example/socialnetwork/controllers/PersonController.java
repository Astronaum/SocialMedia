package org.example.socialnetwork.controllers;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.services.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
            model.addAttribute("persons", new ArrayList<>()); // Return empty list if no keyword provided
        } else {
            model.addAttribute("persons", facade.searchPersonsByKeywords(keyword));
        }
        return "listPersons";
    }
    @GetMapping("/persons")
    @ResponseBody
    public List<Person> getAllPersonsForDropdown() {
        return facade.getAllPersons(); // Méthode existante dans le service
    }
    @GetMapping("/relationship/persons/search")
    @ResponseBody
    public List<Person> searchPersonsForDropdown(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return facade.searchPersonsByKeywords(keyword);
    }



}
