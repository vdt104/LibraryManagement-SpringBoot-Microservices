package com.vdt.document_service.repository;

import java.util.Optional;

import com.vdt.document_service.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, String> {
    Optional<Document> findByDocumentCode(String documentCode);
}
