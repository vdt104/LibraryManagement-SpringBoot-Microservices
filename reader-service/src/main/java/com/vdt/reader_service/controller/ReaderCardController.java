package com.vdt.reader_service.controller;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.service.ReaderCardService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/reader_cards")
@RequiredArgsConstructor
public class ReaderCardController {

    private final ReaderCardService readerCardService;

    @GetMapping
    public ResponseEntity<ReaderCardDTO> getReaderCardByReaderId(@RequestParam("readers_id") String readersId) {
        ReaderCardDTO readerCard = readerCardService.getReaderCardByReaderId(readersId);
        return new ResponseEntity<>(readerCard, HttpStatus.OK);
    }

    @GetMapping("/{card_id}")
    public ResponseEntity<ReaderCardDTO> getReaderCard(@PathVariable("card_id") String cardId) {
        ReaderCardDTO readerCard = readerCardService.getReaderCard(cardId);
        return new ResponseEntity<>(readerCard, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReaderCardDTO> create(@RequestBody ReaderCardDTO readerCardDTO) {
        ReaderCardDTO createdReaderCard = readerCardService.createReaderCard(readerCardDTO);
        return new ResponseEntity<>(createdReaderCard, HttpStatus.CREATED);
    }

    @PutMapping("/{readers}/{readers_id}")
    public ResponseEntity<ReaderCardDTO> updateReaderCardByReaderId(@PathVariable("readers_id") String readersId, @RequestBody ReaderCardDTO readerCardDTO) {
        ReaderCardDTO updatedReaderCard = readerCardService.updateReaderCardByReaderId(readersId, readerCardDTO);
        return new ResponseEntity<>(updatedReaderCard, HttpStatus.OK);
    }

    @PutMapping("/{card_id}")
    public ResponseEntity<ReaderCardDTO> updateReaderCard(@PathVariable("card_id") String cardId, @RequestBody ReaderCardDTO readerCardDTO) {
        ReaderCardDTO updatedReaderCard = readerCardService.updateReaderCard(cardId, readerCardDTO);
        return new ResponseEntity<>(updatedReaderCard, HttpStatus.OK);
    }
}
