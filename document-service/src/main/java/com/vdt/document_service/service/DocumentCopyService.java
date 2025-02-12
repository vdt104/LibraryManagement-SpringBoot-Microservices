package com.vdt.document_service.service;

import java.util.List;

import com.vdt.document_service.dto.DocumentCopyDTO;

public interface DocumentCopyService {
    DocumentCopyDTO updateDocumentCopyStatus(String code, DocumentCopyDTO documentCopyDto);

    List<DocumentCopyDTO> getAllDocumentCopiesOfDocument(String documentCode);

    DocumentCopyDTO createDocumentCopy(String documentId, DocumentCopyDTO documentCopyDto);

    DocumentCopyDTO updateDocumentCopy(String documentCode, DocumentCopyDTO documentCopyDto);
}
