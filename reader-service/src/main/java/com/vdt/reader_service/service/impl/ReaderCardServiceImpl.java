package com.vdt.reader_service.service.impl;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.dto.ReaderDTO;
import com.vdt.reader_service.entity.ReaderCard;
import com.vdt.reader_service.exception.ResourceNotFoundException;
import com.vdt.reader_service.mapper.ReaderCardMapper;
import com.vdt.reader_service.repository.ReaderCardRepository;
import com.vdt.reader_service.repository.ReaderRepository;
import com.vdt.reader_service.service.ReaderCardService;
import lombok.RequiredArgsConstructor;

import com.vdt.reader_service.entity.Reader;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReaderCardServiceImpl implements ReaderCardService {

    private final ReaderCardRepository readerCardRepository;

    private final ReaderRepository readerRepository;

    private final RestTemplate restTemplate;

    @Override
    public ReaderCardDTO getReaderCard(String cardId) {
        ReaderCard readerCard = readerCardRepository.findById(cardId)
            .orElseThrow(() -> new ResourceNotFoundException("ReaderCard", "cardId", cardId));

        return ReaderCardMapper.toDTO(readerCard);
    }

    @Override
    public ReaderCardDTO getReaderCardByReaderId(String readersId) {
        Reader existingReader = readerRepository.findById(readersId)
            .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", readersId));

        ReaderCard readerCard = readerCardRepository.findByUserId(existingReader.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("ReaderCard", "userId", existingReader.getUserId()));

        return ReaderCardMapper.toDTO(readerCard);
    }

    @Override
    public ReaderCardDTO createReaderCard(ReaderCardDTO readerCardDTO) {

        ReaderDTO readerDTO = restTemplate.getForObject("http://user-service/api/v1/readers/" + readerCardDTO.getUserId(), ReaderDTO.class);

        Reader reader = Reader.builder()
                .userId(readerCardDTO.getUserId())
                .studentId(readerDTO.getStudentId())
                .build();

        reader = readerRepository.save(reader);
        
        ReaderCard readerCard = ReaderCardMapper.toEntity(readerCardDTO);

        // readerCard.setUserId(reader.getUserId());

        readerCard = readerCardRepository.save(readerCard);

        return ReaderCardMapper.toDTO(readerCard);
    }

    @Override
    public ReaderCardDTO updateReaderCard(String cardId, ReaderCardDTO readerCardDTO) {
        ReaderCard readerCard = readerCardRepository.findById(cardId)
            .orElseThrow(() -> new ResourceNotFoundException("ReaderCard", "cardId", cardId));

        readerCard.setPin(readerCardDTO.getPin() != null ? readerCardDTO.getPin() : readerCard.getPin());
        readerCard.setIssueDate(readerCardDTO.getIssueDate() != null ? readerCardDTO.getIssueDate() : readerCard.getIssueDate());
        readerCard.setExpiryPeriod(readerCardDTO.getExpiryPeriod() != 0 ? readerCardDTO.getExpiryPeriod() : readerCard.getExpiryPeriod());
        readerCard.setStatus(readerCardDTO.getStatus() != null ? ReaderCard.Status.valueOf(readerCardDTO.getStatus()) : readerCard.getStatus());

        readerCard = readerCardRepository.save(readerCard);

        return ReaderCardMapper.toDTO(readerCard);
    }

    @Override
    public ReaderCardDTO updateReaderCardByReaderId(String readersId, ReaderCardDTO readerCardDTO) {
        Reader existingReader = readerRepository.findById(readersId)
            .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", readersId));

        ReaderCard readerCard = readerCardRepository.findByUserId(existingReader.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("ReaderCard", "userId", existingReader.getUserId()));

        // // if (readerCard.getCreatedAt() = readerCard.getUpdatedAt()) => generate new pin
        // if (readerCard.getCreatedAt().equals(readerCard.getUpdatedAt())) {
        //     readerCard.setPin(generateRandomPin());
        // } else {
        //     readerCard.setPin(readerCardDTO.getPin() != null ? readerCardDTO.getPin() : readerCard.getPin());
        // }

        readerCard.setPin(readerCardDTO.getPin() != null ? readerCardDTO.getPin() : readerCard.getPin());
        readerCard.setIssueDate(readerCardDTO.getIssueDate() != null ? readerCardDTO.getIssueDate() : readerCard.getIssueDate());
        readerCard.setExpiryPeriod(readerCardDTO.getExpiryPeriod() != 0 ? readerCardDTO.getExpiryPeriod() : readerCard.getExpiryPeriod());
        readerCard.setStatus(readerCardDTO.getStatus() != null ? ReaderCard.Status.valueOf(readerCardDTO.getStatus()) : readerCard.getStatus());

        readerCard = readerCardRepository.save(readerCard);

        return ReaderCardMapper.toDTO(readerCard);
    }
}
