package com.vdt.reader_service.mapper;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.entity.ReaderCard;

public class ReaderCardMapper {

    public static ReaderCardDTO toDTO(ReaderCard readerCard) {
        ReaderCardDTO readerCardDTO = new ReaderCardDTO();

        readerCardDTO.setUserId(readerCard.getUserId());
        readerCardDTO.setPin(readerCard.getPin());
        readerCardDTO.setIssueDate(readerCard.getIssueDate());
        readerCardDTO.setExpiryPeriod(readerCard.getExpiryPeriod());
        readerCardDTO.setStatus(readerCard.getStatus().name());

        return readerCardDTO;
    }

    public static ReaderCard toEntity(ReaderCardDTO readerCardDTO) {
        ReaderCard readerCard = new ReaderCard();

        readerCard.setPin(readerCardDTO.getPin());
        readerCard.setIssueDate(readerCardDTO.getIssueDate());
        readerCard.setExpiryPeriod(readerCardDTO.getExpiryPeriod());
        readerCard.setStatus(ReaderCard.Status.valueOf(readerCardDTO.getStatus()));

        return readerCard;
    }
}
