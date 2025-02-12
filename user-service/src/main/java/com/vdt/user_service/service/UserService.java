package com.vdt.user_service.service;

import java.util.List;

import com.vdt.user_service.dto.UserDTO;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String userEmail);

    UserDTO getUserById(String userId);

    UserDTO createUser(UserDTO userDTO);

    String getUserIdByEmail(String userEmail);

    UserDTO updateUser(String userId, UserDTO userDTO);

    UserDTO activateUser(String userId);

    UserDTO deactivateUser(String userId);

    void changeUserPassword(String userId, String newPassword);
}
