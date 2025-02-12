package com.vdt.user_service.service.impl;

import com.vdt.user_service.dto.ReaderCardDTO;
import com.vdt.user_service.dto.ReaderDTO;
import com.vdt.user_service.entity.Reader;
import com.vdt.user_service.entity.Role;
import com.vdt.user_service.entity.User;
import com.vdt.user_service.event.CreateReaderEvent;
import com.vdt.user_service.exception.EmailAlreadyExistsException;
import com.vdt.user_service.exception.ResourceNotFoundException;
import com.vdt.user_service.kafka.CreateReaderProducer;
import com.vdt.user_service.mapper.ReaderMapper;
import com.vdt.user_service.repository.ReaderRepository;
import com.vdt.user_service.repository.UserRepository;
import com.vdt.user_service.response.UserResponse;
import com.vdt.user_service.service.ReaderService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final UserRepository userRepository;

    private final ReaderRepository readerRepository;

    private final RestTemplate restTemplate;

    private final CreateReaderProducer createReaderProducer;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ReaderDTO getReaderById(String userId) {
        Reader reader = readerRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        return ReaderMapper.toDTO(reader);
    }

    @Override
    public List<ReaderDTO> getAllReaders() {
        List<Reader> readers = readerRepository.findAll();

        List<ReaderDTO> readerDtos = new ArrayList<>();

        for (Reader reader : readers) {
            readerDtos.add(ReaderMapper.toDTO(reader));
        }

        return readerDtos;
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

        CreateReaderEvent createReaderEvent = new CreateReaderEvent("A guest has requested to become a reader", savedReader);
        createReaderProducer.sendMessage(createReaderEvent);

        ReaderCardDTO readerCardDTO = ReaderCardDTO.builder()
                .userId(savedReader.getUser().getId())
                .expiryPeriod(expiryPeriod)
                .status("REQUESTED")
                .build();
        
        restTemplate.postForObject("http://reader-service/api/v1/reader_cards", readerCardDTO, ReaderCardDTO.class);

        return ReaderMapper.toDTO(savedReader);
    }

    @Override
    public ReaderDTO updateReader(String userId, ReaderDTO readerDto) {
        Reader existingReader = readerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        User user = existingReader.getUser();

        // Kiểm tra xem email mới có trùng với email hiện tại hay không
        if (!user.getEmail().equals(readerDto.getEmail())) {
            // Kiểm tra xem email mới có tồn tại trong cơ sở dữ liệu hay không
            Optional<User> existingUser = userRepository.findByEmail(readerDto.getEmail());
            if (existingUser.isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
        }

        user.setFullName(readerDto.getFullName());
        user.setDob(readerDto.getDob());
        user.setGender(User.Gender.valueOf(readerDto.getGender()));
        user.setPhoneNumber(readerDto.getPhoneNumber());
        user.setAddress(readerDto.getAddress());
        user.setIdentificationNumber(readerDto.getIdentificationNumber());
        user.setEmail(readerDto.getEmail());

        existingReader.setUser(user);

        Reader updatedReader = readerRepository.save(existingReader);

        return ReaderMapper.toDTO(updatedReader);
    }

    @Override
    public UserResponse activateReader(String userId) {
        Reader existingReader = readerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        User user = existingReader.getUser();
        user.setActive(true);

        ReaderCardDTO readerCardDTO = restTemplate.getForObject("http://reader-service/api/v1/reader_cards/readers/" + userId, ReaderCardDTO.class);

        String pin = null;
        String password = null;

        if (readerCardDTO.getPin() == null) {
            // Tạo mã PIN 6 số ngẫu nhiên
            pin = generateRandomPin();
            System.out.println("Generated PIN: " + pin);
            String encodedPin = passwordEncoder.encode(pin);

            readerCardDTO.setPin(encodedPin);
        }

        if (readerCardDTO.getIssueDate() == null) {
            readerCardDTO.setIssueDate(new Date());
        }

        if (readerCardDTO.getStatus().equals("REQUESTED")) {
            readerCardDTO.setStatus("ACTIVE");
        }

        if (existingReader.getUser().getPassword() == null) {
            password = generateRandomPassword();
            System.out.println("Generated password: " + password);
            String encodedPassword = passwordEncoder.encode(password);

            user.setPassword(encodedPassword);
        }

        restTemplate.put("http://reader-service/api/v1/reader_cards/readers/" + userId, readerCardDTO);    

        // // Cập nhật ReaderCard
        // ReaderCard readerCard = existingReader.getReaderCard();

        // String pin = null;
        // String password = null;

        // if (readerCard.getCreatedAt().equals(readerCard.getUpdatedAt())) {
        //     // Tạo mã PIN 6 số ngẫu nhiên
        //     pin = generateRandomPin();
        //     System.out.println("Generated PIN: " + pin);
        //     String encodedPin = passwordEncoder.encode(pin);

        //     password = generateRandomPassword();
        //     System.out.println("Generated password: " + password);
        //     String encodedPassword = passwordEncoder.encode(password);

        //     user.setPassword(encodedPassword);

        //     readerCard.setPin(encodedPin);
        //     readerCard.setIssueDate(new Date());
        // }
        // readerCard.setIssueDate(new Date());
        // readerCard.setStatus(ReaderCard.Status.ACTIVE);

        // existingReader.setReaderCard(readerCard);

        existingReader.setUser(user);

        readerRepository.save(existingReader);

        return UserResponse.builder()
                .password(password)
                .pin(pin)
                .build();
    }

    @Override
    public ReaderDTO deactivateReader(String userId) {
        Reader existingReader = readerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        User user = existingReader.getUser();
        user.setActive(false);

        ReaderCardDTO readerCardDTO = restTemplate.getForObject("http://reader-service/api/v1/reader_cards/" + userId, ReaderCardDTO.class);

        readerCardDTO.setStatus("INACTIVE");

        restTemplate.put("http://reader-service/api/v1/reader_cards/readers/" + userId, readerCardDTO);

        // ReaderCard readerCard = existingReader.getReaderCard();

        // User user = existingReader.getUser();
        // user.setActive(false);

        // readerCard.setStatus(ReaderCard.Status.INACTIVE);
        // existingReader.setReaderCard(readerCard);

        existingReader.setUser(user);

        Reader updatedReader = readerRepository.save(existingReader);

        return ReaderMapper.toDTO(updatedReader);
    }

    @Override
    public void addDocumentToBookshelf(String userId, String documentCopyCode) throws Exception {
//        Reader existingReader = readerRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));
//
//        DocumentCopy documentCopy = documentCopyRepository.findByDocumentCopyCode(documentCopyCode)
//                .orElseThrow(() -> new ResourceNotFoundException("Document copy", "code", documentCopyCode));
//
//        if (documentCopy.getStatus() != DocumentCopy.Status.AVAILABLE) {
//            throw new Exception("Document copy is not available");
//        }
//
//        existingReader.getDocumentCopies().add(documentCopy);
//
//        readerRepository.save(existingReader);
    }

    private String generateRandomPin() {
        SecureRandom random = new SecureRandom();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_+=<>?";

        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        StringBuilder password = new StringBuilder();

        // Đảm bảo có ít nhất 1 ký tự viết hoa
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        // Đảm bảo có ít nhất 1 ký tự viết thường
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        // Đảm bảo có ít nhất 1 số
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        // Đảm bảo có ít nhất 1 ký tự đặc biệt
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        // Thêm các ký tự ngẫu nhiên còn lại để đạt độ dài tối thiểu là 6
        for (int i = 4; i < 6; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Trộn các ký tự để tạo mật khẩu ngẫu nhiên
        return password.toString();
    }
}
