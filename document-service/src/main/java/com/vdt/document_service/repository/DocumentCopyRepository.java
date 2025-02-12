package com.vdt.document_service.repository;

import java.util.List;
import java.util.Optional;

import com.vdt.document_service.entity.Document;
import com.vdt.document_service.entity.DocumentCopy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentCopyRepository extends JpaRepository<DocumentCopy, String> {
    Optional<DocumentCopy> findByDocumentCopyCode(String documentCopyCode);

    List<DocumentCopy> findByDocument(Document document);
}
