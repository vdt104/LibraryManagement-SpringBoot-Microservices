package com.vdt.user_service.controller;

import com.vdt.user_service.dto.ReaderDTO;
import com.vdt.user_service.service.ReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/readers")
@RequiredArgsConstructor
public class ReaderController {
    private final ReaderService readerService;

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDTO> getReaderById(@PathVariable String id) {
        ReaderDTO reader = readerService.getReaderById(id);
        return new ResponseEntity<>(reader, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReaderDTO> createReader(@Valid @RequestBody ReaderDTO readerDto, @RequestParam(name = "expiry_period") int expiryPeriod) {
        ReaderDTO createdReader = readerService.createReader(readerDto, expiryPeriod);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }
}
