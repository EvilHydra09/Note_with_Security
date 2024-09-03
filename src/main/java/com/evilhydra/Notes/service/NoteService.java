package com.evilhydra.Notes.service;

import com.evilhydra.Notes.model.Note;
import com.evilhydra.Notes.model.User;
import com.evilhydra.Notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserService userService;

    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }
    @Transactional
    public Note createNote(Note note, String userName) {
        User user = userService.findUserByUserID(userName);
        note.setRemoteAddedDateTime(LocalDateTime.now());
        note.setUserName(user.getUserName());
        note.setUserId(userName);
        Note saved = noteRepository.save(note);
        user.getNoteList().add(saved);
        userService.saveDataInUser(user);
        return saved;
    }
    @Transactional
    public boolean deleteNoteById(String id,String userName) {
        boolean removed = false;
        try {
            User user = userService.findUserByUserID(userName);
            removed = user.getNoteList().removeIf(x -> x.getId().equals(id));
            if (removed){
                userService.saveDataInUser(user);
                noteRepository.deleteById(id);
            }
        }catch (Exception e){
            throw new RuntimeException("An error occurred while deleting the entry");
        }
        return removed;
    }

    public void updateNote(Note updatedNote) {
        updatedNote.setRemoteAddedDateTime(LocalDateTime.now());
        noteRepository.save(updatedNote);
    }
    @Transactional
    public void deleteNotesByUserName(String userName) {
        noteRepository.deleteByUserId(userName);
    }
}
