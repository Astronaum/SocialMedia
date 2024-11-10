package org.example.socialnetwork.controllers;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.entities.Person;
import org.example.socialnetwork.services.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
