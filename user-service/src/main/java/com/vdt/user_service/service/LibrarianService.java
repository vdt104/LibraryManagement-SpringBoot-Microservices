package com.vdt.user_service.service;

import com.vdt.user_service.dto.LibrarianDTO;
import com.vdt.user_service.response.UserResponse;

public interface LibrarianService {
    UserResponse createLibrarian(LibrarianDTO librarianDto);
}
