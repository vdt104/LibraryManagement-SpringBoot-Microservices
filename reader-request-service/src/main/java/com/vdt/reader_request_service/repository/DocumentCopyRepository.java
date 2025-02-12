package com.vdt.reader_request_service.repository;

import com.vdt.reader_request_service.entity.DocumentCopy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentCopyRepository extends JpaRepository<DocumentCopy, String> {
}
