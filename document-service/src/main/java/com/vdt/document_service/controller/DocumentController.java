package com.vdt.document_service.controller;

import java.util.List;

import com.vdt.document_service.dto.DocumentDTO;
import com.vdt.document_service.response.DocumentListResponse;
import com.vdt.document_service.response.DocumentResponse;
import com.vdt.document_service.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable String id) {
        DocumentDTO document = documentService.getDocumentById(id);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<DocumentListResponse> getAllDocuments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("documentCode"));

        Page<DocumentDTO> documents = documentService.getAllDocuments(pageRequest);

        int totalPages = documents.getTotalPages();
        List<DocumentResponse> documentList = documents.getContent().stream()
                .map(DocumentResponse::fromDocumentDto)
                .toList();

        return new ResponseEntity<>(DocumentListResponse.builder()
                .documents(documentList)
                .totalPages(totalPages)
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> createDocument(@Valid @RequestBody DocumentDTO documentDto) {
        DocumentDTO createdDocument = documentService.createDocument(documentDto);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }
}
