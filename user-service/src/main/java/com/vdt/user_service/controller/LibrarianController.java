package com.vdt.user_service.controller;

import com.vdt.user_service.dto.LibrarianDTO;
import com.vdt.user_service.response.UserResponse;
import com.vdt.user_service.service.LibrarianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/librarians")
@RequiredArgsConstructor
public class LibrarianController {
    private final LibrarianService librarianService;

    @PostMapping
    public ResponseEntity<UserResponse> createLibrarian(@Valid @RequestBody LibrarianDTO librarianDto) {
        UserResponse createdLibrarian = librarianService.createLibrarian(librarianDto);
        return new ResponseEntity<>(createdLibrarian, HttpStatus.CREATED);
    }
}
