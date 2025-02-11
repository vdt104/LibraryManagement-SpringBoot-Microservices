package com.vdt.reader_service.controller;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.entity.ReaderCard;
import com.vdt.reader_service.service.ReaderCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reader_cards")
@RequiredArgsConstructor
public class ReaderCardController {

    private final ReaderCardService readerCardService;

    @PostMapping
    public ResponseEntity<ReaderCardDTO> create(@RequestBody ReaderCardDTO readerCardDTO) {
        ReaderCardDTO createdReaderCard = readerCardService.createReaderCard(readerCardDTO);
        return new ResponseEntity<>(createdReaderCard, HttpStatus.CREATED);
    }
}
