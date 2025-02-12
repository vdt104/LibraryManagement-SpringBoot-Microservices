package com.vdt.user_service.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vdt.user_service.dto.UserDTO;
import com.vdt.user_service.entity.Role;
import com.vdt.user_service.entity.User;
import com.vdt.user_service.exception.EmailAlreadyExistsException;
import com.vdt.user_service.exception.ResourceNotFoundException;
import com.vdt.user_service.mapper.UserMapper;
import com.vdt.user_service.repository.UserRepository;
import com.vdt.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            if (user.getRole().getId() == 1) {
                continue;
            }
            userDTOs.add(UserMapper.toDTO(user));
        }

        return userDTOs;
    }

    @Override
    public UserDTO getUserById(String userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return UserMapper.toDTO(existingUser);
    }

    @Override
    public UserDTO getUserByEmail(String userEmail) {
        User existingUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        return UserMapper.toDTO(existingUser);
    }

    @Override
    public String getUserIdByEmail(String userEmail) {
        User existingUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        return existingUser.getId();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = UserMapper.toEntity(userDTO);

        Role role = Role.builder()
                .id(userDTO.getRoleId())
                .build();

        user.setRole(role);

        User createdUser = userRepository.save(user);

        return UserMapper.toDTO(createdUser);
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (!existingUser.getEmail().equals(userDTO.getEmail())) {
            Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

            if (user.isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
        }

        existingUser.setFullName(userDTO.getFullName());
        existingUser.setDob(userDTO.getDob());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setIdentificationNumber(userDTO.getIdentificationNumber());
        existingUser.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public UserDTO activateUser(String userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        existingUser.setActive(true);

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public UserDTO deactivateUser(String userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        existingUser.setActive(false);

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public void changeUserPassword(String userId, String newPassword) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);

        userRepository.save(existingUser);
    }
}
