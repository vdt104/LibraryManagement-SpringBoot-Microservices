package com.vdt.reader_request_service.repository;

import com.vdt.reader_request_service.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<Reader, String> {
}
