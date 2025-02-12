package com.vdt.reader_request_service.controller;

import com.vdt.reader_request_service.dto.ReaderRequestDTO;
import com.vdt.reader_request_service.service.ReaderRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reader_requests")
@RequiredArgsConstructor
public class ReaderRequestController {
    private final ReaderRequestService readerRequestService;

    @GetMapping("/{id}")
    public ResponseEntity<ReaderRequestDTO> getReaderRequest(@PathVariable("id") String id) {
        ReaderRequestDTO readerRequest = readerRequestService.getReaderRequestById(id);
        return new ResponseEntity<>(readerRequest, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReaderRequestDTO> createReaderRequest(@Valid @RequestBody ReaderRequestDTO readerRequestDto) {
        ReaderRequestDTO createdReaderRequest = readerRequestService.createReaderRequest(readerRequestDto);
        return new ResponseEntity<>(createdReaderRequest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReaderRequestDTO> updateReaderRequest(@PathVariable("id") String id, @Valid @RequestBody ReaderRequestDTO readerRequestDto) {
        ReaderRequestDTO updatedReaderRequest;

        if (readerRequestDto.getStatus().equals("ACCEPTED")) {
            updatedReaderRequest = readerRequestService.acceptReaderRequest(id, readerRequestDto);
        } else if (readerRequestDto.getStatus().equals("REJECTED")) {
            updatedReaderRequest = readerRequestService.rejectReaderRequest(id, readerRequestDto);
        } else if (readerRequestDto.getStatus().equals("BORROWED")) {
            updatedReaderRequest = readerRequestService.borrowReaderRequest(id, readerRequestDto);
        } else if (readerRequestDto.getStatus().equals("RETURNED")) {
            updatedReaderRequest = readerRequestService.returnReaderRequest(id, readerRequestDto);
        } else {
            throw new IllegalArgumentException("Invalid status");
        }

        return new ResponseEntity<>(updatedReaderRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}/action=cancel")
    public ResponseEntity<ReaderRequestDTO> cancelReaderRequest(@PathVariable("id") String id, @Valid @RequestBody ReaderRequestDTO readerRequestDto) {
        ReaderRequestDTO updatedReaderRequest = readerRequestService.cancelReaderRequest(id, readerRequestDto);
        return new ResponseEntity<>(updatedReaderRequest, HttpStatus.OK);
    }
}
