package org.example.socialnetwork.service;

import org.example.socialnetwork.model.Relationship;
import org.example.socialnetwork.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationService {
    @Autowired
    private RelationRepository relationRepository;

    public Relationship createRelation(Relationship relation) {
        return relationRepository.save(relation);
    }
}
