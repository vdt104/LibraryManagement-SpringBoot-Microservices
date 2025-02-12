package com.vdt.user_service.service.impl;

import com.vdt.user_service.response.UserResponse;
import com.vdt.user_service.dto.LibrarianDTO;
import com.vdt.user_service.entity.Librarian;
import com.vdt.user_service.entity.Role;
import com.vdt.user_service.entity.User;
import com.vdt.user_service.exception.EmailAlreadyExistsException;
import com.vdt.user_service.mapper.LibrarianMapper;
import com.vdt.user_service.repository.LibrarianRepository;
import com.vdt.user_service.repository.UserRepository;
import com.vdt.user_service.service.LibrarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibrarianServiceImpl implements LibrarianService {
    private final LibrarianRepository librarianRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse createLibrarian(LibrarianDTO librarianDto) {
        Optional<User> existingUser = userRepository.findByEmail(librarianDto.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Librarian librarian = LibrarianMapper.toEntity(librarianDto);

        String password = generateRandomPassword();

        User user = librarian.getUser();
        user.setActive(true);
        user.setRole(Role.builder()
                .id(3L)
                .build());
        user.setPassword(passwordEncoder.encode(password));

        librarian.setUser(user);

        librarianRepository.save(librarian);

        return UserResponse.builder()
                .password(password)
                .pin(null)
                .build();
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
