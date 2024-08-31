package com.evilhydra.Notes.service;

import com.evilhydra.Notes.model.User;
import com.evilhydra.Notes.repository.NoteRepository;
import com.evilhydra.Notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;




    public List<User> findAllUsers() {
        return userRepository.findAll();
    }



    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createAdminUser(User user){
        return userRepository.save(user);
    }

    public void saveDataInUser(User user){
        userRepository.save(user);
    }

    public User findUserByUserID(String username) {
        return userRepository.findByUserId(username);
    }

    public void deleteUserByUserID(String userID) {

        noteRepository.deleteByUserId(userID);
        // Delete the user
        userRepository.deleteByUserId(userID);
    }

}
