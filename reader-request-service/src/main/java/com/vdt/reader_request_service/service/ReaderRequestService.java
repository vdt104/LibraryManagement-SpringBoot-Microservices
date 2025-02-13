package com.vdt.reader_request_service.service;

import com.vdt.reader_request_service.dto.ReaderRequestDTO;

public interface ReaderRequestService {
    ReaderRequestDTO createReaderRequest(ReaderRequestDTO readerRequestDto);

    ReaderRequestDTO getReaderRequestById(String id);

    ReaderRequestDTO acceptReaderRequest(String id, ReaderRequestDTO readerRequestDto);

    ReaderRequestDTO rejectReaderRequest(String id, ReaderRequestDTO readerRequestDto);

    ReaderRequestDTO borrowReaderRequest(String id, ReaderRequestDTO readerRequestDto);

    ReaderRequestDTO returnReaderRequest(String id, ReaderRequestDTO readerRequestDto);

    ReaderRequestDTO cancelReaderRequest(String id);
}
