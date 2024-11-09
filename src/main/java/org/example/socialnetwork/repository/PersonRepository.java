package org.example.socialnetwork.repository;

import org.example.socialnetwork.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
