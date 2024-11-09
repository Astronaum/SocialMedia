package org.example.socialnetwork.repository;

import org.example.socialnetwork.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relationship, Long> {
}

