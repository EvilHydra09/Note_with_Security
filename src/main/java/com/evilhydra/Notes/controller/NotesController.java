package com.evilhydra.Notes.controller;

import com.evilhydra.Notes.model.Note;
import com.evilhydra.Notes.model.User;
import com.evilhydra.Notes.service.NoteService;
import com.evilhydra.Notes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserID(userName);
        List<Note> noteList = user.getNoteList();
        if (noteList != null) {
            return ResponseEntity.ok(noteList);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Note>> getNoteById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserID(userName);
        List<Note> collect = user.getNoteList().stream().filter(x -> x.getId().equals(id)).toList();
        if (!collect.isEmpty()) {
            Optional<Note> note = noteService.getNoteById(id);
            if (note.isPresent()) {
                return ResponseEntity.ok(note);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Note user = noteService.createNote(note, userName);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserID(userName);

        List<Note> list = user.getNoteList().stream().filter(x -> x.getId().equals(id)).toList();
        if (!list.isEmpty()) {

            Optional<Note> existingNote = noteService.getNoteById(id);
            if (existingNote.isPresent()) {
                Note updatedNote = existingNote.get();
                updatedNote.setTitle(note.getTitle());
                updatedNote.setContent(note.getContent());
                noteService.updateNote(updatedNote);
                return ResponseEntity.ok(updatedNote);
            }
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findUserByUserID(userName);
        boolean removed = noteService.deleteNoteById(id, userName);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
