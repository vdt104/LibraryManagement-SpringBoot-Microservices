package com.vdt.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vdt.user_service.dto.UserDTO;
import com.vdt.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user_id")
    public ResponseEntity<UserDTO> getUserById(@RequestParam("value") String userId) {
        UserDTO user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user_email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam("value") String userEmail) {
        UserDTO user = userService.getUserByEmail(userEmail);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{user_email}/id")
    public ResponseEntity<String> getUserIdByEmail(@PathVariable("user_email") String userEmail) {
        String userId = userService.getUserIdByEmail(userEmail);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("user_id") String userId, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/{user_id}/is_active")
    public ResponseEntity<?> activateOrDeactivateUser(@PathVariable("user_id") String userId, @RequestParam(name = "boolean") boolean isActive) {
        if (isActive) {
            UserDTO updatedUser = userService.activateUser(userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            UserDTO updatedUser = userService.deactivateUser(userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }

    @PutMapping("/{user_id}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable("user_id") String userId, @RequestParam("action") String action, @RequestBody String newPassword) {
        if ("change".equals(action)) {
            userService.changeUserPassword(userId, newPassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
