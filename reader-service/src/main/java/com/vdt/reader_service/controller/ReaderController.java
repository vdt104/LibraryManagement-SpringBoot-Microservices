package com.vdt.reader_service.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vdt.reader_service.dto.ReaderDTO;
import com.vdt.reader_service.response.UserResponse;
import com.vdt.reader_service.service.ReaderService;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity<List<ReaderDTO>> getAllReaders() {
        List<ReaderDTO> readers = readerService.getAllReaders();
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReaderDTO> createReader(@Valid @RequestBody ReaderDTO readerDto, @RequestParam(name = "expiry_period") int expiryPeriod) {
        ReaderDTO createdReader = readerService.createReader(readerDto, expiryPeriod);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/bookshelf")
    public ResponseEntity<String> addDocumentToBookshelf(@PathVariable String id, @RequestParam(name = "document_copy_code") String documentCopyCode) {
        try {
            readerService.addDocumentToBookshelf(id, documentCopyCode);
            return new ResponseEntity<>("Document added to bookshelf", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderDTO> updateReader(@PathVariable String id, @RequestBody ReaderDTO readerDto) {

        ReaderDTO updatedReader = readerService.updateReader(id, readerDto);
        return new ResponseEntity<>(updatedReader, HttpStatus.OK);
    }

    @PutMapping("/{id}/is_active")
    public ResponseEntity<?> activateOrDeactivateReader(@PathVariable String id, @RequestParam(name = "boolean") boolean isActive) {
        if (isActive) {
            UserResponse updatedReader = readerService.activateReader(id);
            return new ResponseEntity<>(updatedReader, HttpStatus.OK);
        } else {
            ReaderDTO updatedReader = readerService.deactivateReader(id);
            return new ResponseEntity<>(updatedReader, HttpStatus.OK);
        }
    }
}
