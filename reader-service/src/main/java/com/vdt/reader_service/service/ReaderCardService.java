package com.vdt.reader_service.service;

import com.vdt.reader_service.dto.ReaderCardDTO;

public interface ReaderCardService {
    ReaderCardDTO createReaderCard(ReaderCardDTO readerCardDTO);

    ReaderCardDTO getReaderCard(String cardId);

    ReaderCardDTO getReaderCardByReaderId(String readersId);

    ReaderCardDTO updateReaderCard(String cardId, ReaderCardDTO readerCardDTO);

    ReaderCardDTO updateReaderCardByReaderId(String readersId, ReaderCardDTO readerCardDTO);
}
