package com.vdt.user_service.service.impl;

import com.vdt.user_service.dto.ReaderCardDTO;
import com.vdt.user_service.dto.ReaderDTO;
import com.vdt.user_service.entity.Reader;
import com.vdt.user_service.entity.Role;
import com.vdt.user_service.entity.User;
import com.vdt.user_service.exception.EmailAlreadyExistsException;
import com.vdt.user_service.exception.ResourceNotFoundException;
import com.vdt.user_service.mapper.ReaderMapper;
import com.vdt.user_service.repository.ReaderRepository;
import com.vdt.user_service.repository.UserRepository;
import com.vdt.user_service.service.ReaderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final UserRepository userRepository;

    private final ReaderRepository readerRepository;

    private final RestTemplate restTemplate;

    @Override
    public ReaderDTO getReaderById(String userId) {
        Reader reader = readerRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        return ReaderMapper.toDTO(reader);
    }

    @Override
    public ReaderDTO createReader(ReaderDTO readerDto, int expiryPeriod) {
        Optional<User> existingUser = userRepository.findByEmail(readerDto.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Reader reader = ReaderMapper.toEntity(readerDto);

        User user = reader.getUser();
        user.setActive(false);
        Role role = Role.builder()
                .id(2L)
                .build();
        user.setRole(role);

        reader.setUser(user);

        Reader savedReader = readerRepository.save(reader);

        ReaderCardDTO readerCardDTO = ReaderCardDTO.builder()
                .userId(savedReader.getUser().getId())
                .expiryPeriod(expiryPeriod)
                .status("REQUESTED")
                .build();
        
        restTemplate.postForObject("http://localhost:8090/api/v1/reader_cards", readerCardDTO, ReaderCardDTO.class);

        return ReaderMapper.toDTO(savedReader);
    }
}
