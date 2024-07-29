package com.evilhydra.Notes.service;

import com.evilhydra.Notes.model.User;
import com.evilhydra.Notes.repository.NoteRepository;
import com.evilhydra.Notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User createAdminUser(User user){
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setRoles(List.of("USER","ADMIN"));
        return userRepository.save(user);
    }

    public void saveDataInUser(User user){
        userRepository.save(user);
    }

    public User findUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public void deleteUserByUserName(String username) {
        // Delete all notes associated with the user
        noteRepository.deleteByUserName(username);
        // Delete the user
        userRepository.deleteByUserName(username);
    }

}
