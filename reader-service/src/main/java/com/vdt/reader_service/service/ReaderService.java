package com.vdt.reader_service.service;

import java.util.List;

import com.vdt.reader_service.dto.ReaderDTO;
import com.vdt.reader_service.response.UserResponse;

public interface ReaderService {
    ReaderDTO getReaderById(String userId);

    List<ReaderDTO> getAllReaders();

    ReaderDTO createReader(ReaderDTO readerDto, int expiryPeriod);

    ReaderDTO updateReader(String userId, ReaderDTO readerDto);

    UserResponse activateReader(String userId);

    ReaderDTO deactivateReader(String userId);

    void addDocumentToBookshelf(String userId, String documentCopyCode) throws Exception;
}

