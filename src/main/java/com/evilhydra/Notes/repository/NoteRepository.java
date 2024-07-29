package com.evilhydra.Notes.repository;

import com.evilhydra.Notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
    void deleteByUserName(String userName);
}
