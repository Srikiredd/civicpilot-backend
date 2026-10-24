package com.civicpilot.controller;

import com.civicpilot.entity.User;
import com.civicpilot.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User response = userService.createUser(user);

        return new ResponseEntity<User>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> response = userService.getAllUsers();

        return new ResponseEntity<List<User>>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        User response = userService.getUserById(id);

        return new ResponseEntity<User>(response, HttpStatus.OK);
    }
}