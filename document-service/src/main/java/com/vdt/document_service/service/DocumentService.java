package com.vdt.document_service.service;

import com.vdt.document_service.dto.DocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DocumentService {
    DocumentDTO createDocument(DocumentDTO documentDto);

    DocumentDTO getDocumentById(String id);

    Page<DocumentDTO> getAllDocuments(PageRequest pageRequest);
}

