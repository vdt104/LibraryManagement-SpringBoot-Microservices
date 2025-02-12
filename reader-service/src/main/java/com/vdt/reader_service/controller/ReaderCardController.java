package com.vdt.reader_service.controller;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.service.ReaderCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{card_id}")
    public ResponseEntity<ReaderCardDTO> getReaderCard(@PathVariable("card_id") String cardId) {
        ReaderCardDTO readerCard = readerCardService.getReaderCard(cardId);
        return new ResponseEntity<>(readerCard, HttpStatus.OK);
    }
}
