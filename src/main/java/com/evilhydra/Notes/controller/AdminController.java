package com.evilhydra.Notes.controller;

import com.evilhydra.Notes.model.User;
import com.evilhydra.Notes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-user")
    public ResponseEntity<?> getAllUser(){
        List<User> allUsers = userService.findAllUsers();
        if(!allUsers.isEmpty()){
            return ResponseEntity.ok(allUsers);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("create-admin-user")
    public ResponseEntity<?> createAdminUser(@RequestBody User user){
        User adminUser = userService.createAdminUser(user);
        return new ResponseEntity<>(adminUser, HttpStatus.CREATED);
    }



}
