package com.vdt.reader_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vdt.reader_service.entity.ReaderCard;

public interface ReaderCardRepository extends JpaRepository<ReaderCard, String> {
}
