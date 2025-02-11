package com.vdt.user_service.service;

import com.vdt.user_service.dto.ReaderDTO;

public interface ReaderService {
    ReaderDTO createReader(ReaderDTO readerDto, int expiryPeriod);

    ReaderDTO getReaderById(String userId);
}
