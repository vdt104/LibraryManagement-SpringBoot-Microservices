package com.vdt.reader_service.service.impl;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.dto.ReaderDTO;
import com.vdt.reader_service.entity.ReaderCard;
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
    public ReaderCardDTO createReaderCard(ReaderCardDTO readerCardDTO) {

        ReaderDTO readerDTO = restTemplate.getForObject("http://user-service/api/v1/readers/" + readerCardDTO.getUserId(), ReaderDTO.class);

        Reader reader = Reader.builder()
                .userId(readerCardDTO.getUserId())
                .studentId(readerDTO.getStudentId())
                .build();

        reader = readerRepository.save(reader);
        
        ReaderCard readerCard = ReaderCardMapper.toEntity(readerCardDTO);

        readerCard.setReader(reader);

        readerCard = readerCardRepository.save(readerCard);

        return ReaderCardMapper.toDTO(readerCard);
    }
}
