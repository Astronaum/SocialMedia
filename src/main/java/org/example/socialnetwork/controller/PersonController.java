package org.example.socialnetwork.controller;

import org.example.socialnetwork.model.Person;
import org.example.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personnes")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public String getAllPersons(Model model) {
        model.addAttribute("persons", personService.getAllPersons());
        return "listPersons";
    }

    @GetMapping("/new")
    public String showCreatePersonForm(Model model) {
        model.addAttribute("person", new Person());
        return "createPerson";
    }

    @PostMapping("/save")
    public String createPerson(Person person) {
        personService.createPerson(person);
        return "redirect:/personnes";
    }
}
