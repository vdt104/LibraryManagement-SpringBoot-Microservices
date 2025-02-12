package com.vdt.document_service.repository;

import com.vdt.document_service.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {
    Optional<Author> findByName(String name);
}
