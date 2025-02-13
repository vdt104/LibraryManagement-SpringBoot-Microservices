package com.vdt.document_service.controller;

import lombok.RequiredArgsConstructor;

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

import com.vdt.document_service.dto.DocumentCopyDTO;
import com.vdt.document_service.service.DocumentCopyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/document_copies")
@RequiredArgsConstructor
public class DocumentCopyController {

    private final DocumentCopyService documentCopyService;

    @PutMapping("/{document_code}/status")
    public ResponseEntity<DocumentCopyDTO> updateDocumentCopyStatus(@PathVariable("document_code") String documentCode, @RequestBody DocumentCopyDTO documentCopyDto) {
        DocumentCopyDTO updatedDocumentCopy = documentCopyService.updateDocumentCopyStatus(documentCode, documentCopyDto);
        return new ResponseEntity<>(updatedDocumentCopy, HttpStatus.OK);
    }

    @GetMapping("/{document_code}")
    public ResponseEntity<List<DocumentCopyDTO>> getAllDocumentCopyByDocumentCode(@PathVariable("document_code") String documentCode) {
        List<DocumentCopyDTO> documentCopies = documentCopyService.getAllDocumentCopiesOfDocument(documentCode);
        return new ResponseEntity<>(documentCopies, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<DocumentCopyDTO> getDocumentCopyByDocumentCopyCode(@RequestParam("document_copy_code") String documentCopyCode) {
        DocumentCopyDTO documentCopy = documentCopyService.getDocumentCopyByDocumentCopyCode(documentCopyCode);
        return new ResponseEntity<>(documentCopy, HttpStatus.OK);
    }

    @PostMapping("/{document_id}")
    public ResponseEntity<DocumentCopyDTO> createDocumentCopy(@PathVariable("document_id") String documentId, @Valid @RequestBody DocumentCopyDTO documentCopyDto) {
        DocumentCopyDTO createdDocumentCopy = documentCopyService.createDocumentCopy(documentId, documentCopyDto);
        return new ResponseEntity<>(createdDocumentCopy, HttpStatus.CREATED);
    }

    @PutMapping("/{document_code}")
    public ResponseEntity<DocumentCopyDTO> updateDocumentCopy(@PathVariable("document_code") String documentCode, @Valid @RequestBody DocumentCopyDTO documentCopyDto) {
        DocumentCopyDTO updatedDocumentCopy = documentCopyService.updateDocumentCopy(documentCode, documentCopyDto);
        return new ResponseEntity<>(updatedDocumentCopy, HttpStatus.OK);
    }
}
