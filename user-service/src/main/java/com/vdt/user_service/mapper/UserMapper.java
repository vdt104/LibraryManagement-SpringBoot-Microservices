package com.vdt.user_service.mapper;

import com.vdt.user_service.dto.UserDTO;
import com.vdt.user_service.entity.Role;
import com.vdt.user_service.entity.User;

public class UserMapper {

    public static UserDTO toDTO (User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setFullName(user.getFullName());
        userDTO.setDob(user.getDob());
        userDTO.setGender(user.getGender().name());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setIdentificationNumber(user.getIdentificationNumber());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoleId(user.getRole().getId());
        userDTO.setActive(user.isActive());

        return userDTO;
    }

    public static User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setFullName(userDTO.getFullName());
        user.setDob(userDTO.getDob());
        user.setGender(User.Gender.valueOf(userDTO.getGender()));
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setIdentificationNumber(userDTO.getIdentificationNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        Role role = new Role();
        role.setId(userDTO.getRoleId());
        user.setRole(role);
        
        user.setActive(userDTO.isActive());

        return user;
    }    
}
