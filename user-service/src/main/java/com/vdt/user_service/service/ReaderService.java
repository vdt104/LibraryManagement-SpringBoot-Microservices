package com.vdt.user_service.service;

import com.vdt.user_service.dto.ReaderDTO;
import com.vdt.user_service.response.UserResponse;

import java.util.List;

public interface ReaderService {
    ReaderDTO getReaderById(String userId);

    List<ReaderDTO> getAllReaders();

    ReaderDTO createReader(ReaderDTO readerDto, int expiryPeriod);

    ReaderDTO updateReader(String userId, ReaderDTO readerDto);

    UserResponse activateReader(String userId);

    ReaderDTO deactivateReader(String userId);

    void addDocumentToBookshelf(String userId, String documentCopyCode) throws Exception;
}
