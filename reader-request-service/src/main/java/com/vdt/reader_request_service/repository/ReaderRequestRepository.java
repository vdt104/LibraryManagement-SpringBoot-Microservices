package com.vdt.reader_request_service.repository;

import com.vdt.reader_request_service.entity.ReaderRequest;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRequestRepository extends JpaRepository<ReaderRequest, String> {
    Optional<ReaderRequest> findById(String id);
}
