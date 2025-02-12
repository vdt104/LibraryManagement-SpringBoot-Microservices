package com.vdt.reader_request_Service.mapper;

import com.vdt.reader_request_Service.dto.ReaderRequestDTO;
import com.vdt.reader_request_Service.entity.ReaderRequest;

public class ReaderRequestMapper {

    public static ReaderRequestDTO toDTO(ReaderRequest readerRequest) {
        ReaderRequestDTO readerRequestDto = new ReaderRequestDTO();

        readerRequestDto.setUserId(readerRequest.getReader().getUserId());
        readerRequestDto.setStatus(readerRequest.getStatus().name());
        readerRequestDto.setDateBorrowed(readerRequest.getDateBorrowed());
        readerRequestDto.setBorrowingPeriod(readerRequest.getBorrowingPeriod());
        readerRequestDto.setDateReturned(readerRequest.getDateReturned());
        readerRequestDto.setPenaltyFee(readerRequest.getPenaltyFee());
        readerRequestDto.setNotes(readerRequest.getNotes());

        return readerRequestDto;
    }

    public static ReaderRequest toEntity(ReaderRequestDTO readerRequestDto) {
        ReaderRequest readerRequest = new ReaderRequest();

        readerRequest.setStatus(ReaderRequest.Status.valueOf(readerRequestDto.getStatus()));
        readerRequest.setDateBorrowed(readerRequestDto.getDateBorrowed());
        readerRequest.setBorrowingPeriod(readerRequestDto.getBorrowingPeriod());
        readerRequest.setDateReturned(readerRequestDto.getDateReturned());
        readerRequest.setPenaltyFee(readerRequestDto.getPenaltyFee());
        readerRequest.setNotes(readerRequestDto.getNotes());

        return readerRequest;
    }
}