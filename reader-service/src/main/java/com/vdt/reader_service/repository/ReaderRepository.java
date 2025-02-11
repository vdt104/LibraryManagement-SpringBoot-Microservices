package com.vdt.reader_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vdt.reader_service.entity.Reader;

public interface ReaderRepository extends JpaRepository<Reader, String> {
}
