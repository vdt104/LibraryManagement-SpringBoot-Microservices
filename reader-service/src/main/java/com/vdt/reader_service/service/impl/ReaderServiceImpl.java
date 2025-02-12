package com.vdt.reader_service.service.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vdt.reader_service.dto.ReaderCardDTO;
import com.vdt.reader_service.dto.ReaderDTO;
import com.vdt.reader_service.dto.UserDTO;
import com.vdt.reader_service.entity.Reader;
import com.vdt.reader_service.entity.ReaderCard;
// import com.vdt.reader_service.event.CreateReaderEvent;
// import com.vdt.reader_service.kafka.CreateReaderProducer;
import com.vdt.reader_service.exception.ResourceNotFoundException;
import com.vdt.reader_service.mapper.ReaderCardMapper;
import com.vdt.reader_service.mapper.ReaderMapper;
import com.vdt.reader_service.repository.ReaderCardRepository;
import com.vdt.reader_service.repository.ReaderRepository;
import com.vdt.reader_service.response.UserResponse;
import com.vdt.reader_service.service.ReaderCardService;
import com.vdt.reader_service.service.ReaderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    private final ReaderCardRepository readerCardRepository;

    private final RestTemplate restTemplate;

    // private final CreateReaderProducer createReaderProducer;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ReaderDTO getReaderById(String userId) {
        Reader reader = readerRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        UserDTO userDTO = restTemplate.getForObject("http://user-service/api/v1/users/user_id?value=" + userId, UserDTO.class);

        ReaderDTO readerDTO = toReaderDTO(userDTO);

        readerDTO.setStudentId(reader.getStudentId());

        return readerDTO;
    }

    @Override
    public List<ReaderDTO> getAllReaders() {
        // ResponseEntity<List<UserDTO>> responseEntity = restTemplate.exchange(
        //     "http://user-service/api/v1/users",
        //     HttpMethod.GET,
        //     null,
        //     new ParameterizedTypeReference<List<UserDTO>>() {}
        // );

        // List<UserDTO> userDTOs = responseEntity.getBody();

        // List<ReaderDTO> readerDTOs = new ArrayList<>();

        // for (UserDTO userDTO : userDTOs) {
        //     if (userDTO.getRoleId() == 2) {
        //         Reader reader = readerRepository.findById(userDTO.get
        //                 .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userDTO.getId()));

        //         ReaderDTO readerDTO = toReaderDTO(userDTO);
        //         readerDTO.setStudentId(reader.getStudentId());

        //         readerDTOs.add(readerDTO);
        //     }
        // }

        // return readerDTOs;

        return null;
    }

    @Override
    public ReaderDTO createReader(ReaderDTO readerDto, int expiryPeriod) {

        UserDTO userDTO = toUserDTO(readerDto);

        userDTO.setRoleId(2L);

        userDTO = restTemplate.postForObject("http://user-service/api/v1/users", userDTO, UserDTO.class);

        String createdUserId = restTemplate.getForObject("http://user-service/api/v1/users/" + userDTO.getEmail() + "/id", String.class);

        // Optional<User> existingUser = userRepository.findByEmail(readerDto.getEmail());

        // if (existingUser.isPresent()) {
        //     throw new EmailAlreadyExistsException("Email already exists");
        // }

        Reader reader = ReaderMapper.toEntity(readerDto);

        reader.setUserId(createdUserId);

        Reader savedReader = readerRepository.save(reader);

        // User user = reader.getUser();
        // user.setActive(false);
        // Role role = Role.builder()
        //         .id(2L)
        //         .build();
        // user.setRole(role);

        // reader.setUser(user);

        // Reader savedReader = readerRepository.save(reader);

        // CreateReaderEvent createReaderEvent = new CreateReaderEvent("A guest has requested to become a reader", savedReader);
        // createReaderProducer.sendMessage(createReaderEvent);

        ReaderCardDTO readerCardDTO = ReaderCardDTO.builder()
                .userId(createdUserId)
                .expiryPeriod(expiryPeriod)
                .status("REQUESTED")
                .build();
        
        readerCardRepository.save(ReaderCardMapper.toEntity(readerCardDTO));
            
        // restTemplate.postForObject("http://reader-service/api/v1/reader_cards", readerCardDTO, ReaderCardDTO.class);

        return ReaderMapper.toDTO(savedReader);
    }

    @Override
    public ReaderDTO updateReader(String userId, ReaderDTO readerDto) {
        UserDTO userDTO = toUserDTO(readerDto);
        
        restTemplate.put("http://user-service/api/v1/users/" + userId, userDTO);

        Reader reader = readerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));
        reader.setStudentId(readerDto.getStudentId());

        readerRepository.save(reader);

        return ReaderMapper.toDTO(reader);       

        // User user = existingReader.getUser();

        // // Kiểm tra xem email mới có trùng với email hiện tại hay không
        // if (!user.getEmail().equals(readerDto.getEmail())) {
        //     // Kiểm tra xem email mới có tồn tại trong cơ sở dữ liệu hay không
        //     Optional<User> existingUser = userRepository.findByEmail(readerDto.getEmail());
        //     if (existingUser.isPresent()) {
        //         throw new EmailAlreadyExistsException("Email already exists");
        //     }
        // }

        // user.setFullName(readerDto.getFullName());
        // user.setDob(readerDto.getDob());
        // user.setGender(User.Gender.valueOf(readerDto.getGender()));
        // user.setPhoneNumber(readerDto.getPhoneNumber());
        // user.setAddress(readerDto.getAddress());
        // user.setIdentificationNumber(readerDto.getIdentificationNumber());
        // user.setEmail(readerDto.getEmail());

        // existingReader.setUser(user);

        // Reader updatedReader = readerRepository.save(existingReader);

        // return ReaderMapper.toDTO(updatedReader);
    }

    @Override
    public UserResponse activateReader(String userId) {
        readerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        ReaderCard readerCard = readerCardRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader card", "id", userId));

        String pin = null;
        String password = null;

        if (readerCard.getPin() == null) {
            // Tạo mã PIN 6 số ngẫu nhiên
            pin = generateRandomPin();
            String encodedPin = passwordEncoder.encode(pin);

            password = generateRandomPassword();

            readerCard.setPin(encodedPin);

            if (readerCard.getIssueDate() == null) {
                readerCard.setIssueDate(new Date());
            }
        }

        if (readerCard.getStatus().equals("REQUESTED")) {
            readerCard.setStatus(ReaderCard.Status.ACTIVE);
        }

        readerCardRepository.save(readerCard);

        restTemplate.put("http://user-service/api/v1/users/" + userId + "/is_active?boolean=true", null);
        restTemplate.put("http://user-service/api/v1/users/" + userId + "/password?action=change", password);


        // restTemplate.put("http://user-service/api/v1/users/password/" + userId, null);

        // Reader existingReader = readerRepository.findById(userId)
        //         .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        // User user = existingReader.getUser();
        // user.setActive(true);

        // ReaderCardDTO readerCardDTO = restTemplate.getForObject("http://reader-service/api/v1/reader_cards/readers/" + userId, ReaderCardDTO.class);

        // String pin = null;
        // String password = null;

        // if (readerCardDTO.getPin() == null) {
        //     // Tạo mã PIN 6 số ngẫu nhiên
        //     pin = generateRandomPin();
        //     System.out.println("Generated PIN: " + pin);
        //     String encodedPin = passwordEncoder.encode(pin);

        //     readerCardDTO.setPin(encodedPin);
        // }

        // if (readerCardDTO.getIssueDate() == null) {
        //     readerCardDTO.setIssueDate(new Date());
        // }

        // if (readerCardDTO.getStatus().equals("REQUESTED")) {
        //     readerCardDTO.setStatus("ACTIVE");
        // }

        // if (existingReader.getUser().getPassword() == null) {
        //     password = generateRandomPassword();
        //     System.out.println("Generated password: " + password);
        //     String encodedPassword = passwordEncoder.encode(password);

        //     user.setPassword(encodedPassword);
        

        // restTemplate.put("http://reader-service/api/v1/reader_cards/readers/" + userId, readerCardDTO);    

        return UserResponse.builder()
                .password(password)
                .pin(pin)
                .build();
    }

    @Override
    public ReaderDTO deactivateReader(String userId) {
        Reader existingReader = readerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", userId));

        ReaderCard readerCard = readerCardRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader card", "id", userId));

        readerCard.setStatus(ReaderCard.Status.INACTIVE);

        readerCardRepository.save(readerCard);

        restTemplate.put("http://user-service/api/v1/users/" + userId + "/is_active?boolean=false", null);

        return ReaderMapper.toDTO(existingReader);

        // User user = existingReader.getUser();
        // user.setActive(false);

        // ReaderCardDTO readerCardDTO = restTemplate.getForObject("http://reader-service/api/v1/reader_cards/" + userId, ReaderCardDTO.class);

        // readerCardDTO.setStatus("INACTIVE");

        // restTemplate.put("http://reader-service/api/v1/reader_cards/readers/" + userId, readerCardDTO);

        // existingReader.setUser(user);

        // Reader updatedReader = readerRepository.save(existingReader);

        // return ReaderMapper.toDTO(updatedReader);
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

    private UserDTO toUserDTO(ReaderDTO readerDTO) {
        return UserDTO.builder()
                .fullName(readerDTO.getFullName())
                .dob(readerDTO.getDob())
                .gender(readerDTO.getGender())
                .phoneNumber(readerDTO.getPhoneNumber())
                .address(readerDTO.getAddress())
                .identificationNumber(readerDTO.getIdentificationNumber())
                .email(readerDTO.getEmail())
                .roleId(readerDTO.getRoleId())
                .isActive(readerDTO.isActive())
                .build();
    }

    public ReaderDTO toReaderDTO(UserDTO userDTO) {
        ReaderDTO readerDTO = new ReaderDTO();
        readerDTO.setFullName(userDTO.getFullName());
        readerDTO.setDob(userDTO.getDob());
        readerDTO.setGender(userDTO.getGender());
        readerDTO.setPhoneNumber(userDTO.getPhoneNumber());
        readerDTO.setAddress(userDTO.getAddress());
        readerDTO.setIdentificationNumber(userDTO.getIdentificationNumber());
        readerDTO.setEmail(userDTO.getEmail());
        readerDTO.setRoleId(userDTO.getRoleId());
        readerDTO.setActive(userDTO.isActive());
        return readerDTO;
    }
}