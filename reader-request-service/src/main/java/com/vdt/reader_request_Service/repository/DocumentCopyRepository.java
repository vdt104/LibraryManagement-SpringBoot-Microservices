package com.vdt.reader_request_Service.repository;

import com.vdt.reader_request_Service.entity.DocumentCopy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentCopyRepository extends JpaRepository<DocumentCopy, String> {
}
