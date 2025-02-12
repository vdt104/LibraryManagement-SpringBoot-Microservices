package com.vdt.user_service.mapper;

import com.vdt.user_service.dto.LibrarianDTO;
import com.vdt.user_service.entity.Librarian;
import com.vdt.user_service.entity.User;

public class LibrarianMapper {

    public static LibrarianDTO toDTO(Librarian librarian) {
        LibrarianDTO librarianDto = new LibrarianDTO();

        librarianDto.setFullName(librarian.getUser().getFullName());
        librarianDto.setDob(librarian.getUser().getDob());
        librarianDto.setGender(librarian.getUser().getGender().name());
        librarianDto.setPhoneNumber(librarian.getUser().getPhoneNumber());
        librarianDto.setAddress(librarian.getUser().getAddress());
        librarianDto.setIdentificationNumber(librarian.getUser().getIdentificationNumber());
        librarianDto.setEmail(librarian.getUser().getEmail());
        librarianDto.setDepartment(librarian.getDepartment());
        librarianDto.setPosition(librarian.getPosition());
        librarianDto.setWorkplace(librarian.getWorkplace());

        return librarianDto;
    }

    public static Librarian toEntity(LibrarianDTO librarianDto) {
        User user = new User();
        user.setFullName(librarianDto.getFullName());
        user.setDob(librarianDto.getDob());
        user.setGender(User.Gender.valueOf(librarianDto.getGender()));
        user.setPhoneNumber(librarianDto.getPhoneNumber());
        user.setAddress(librarianDto.getAddress());
        user.setIdentificationNumber(librarianDto.getIdentificationNumber());
        user.setEmail(librarianDto.getEmail());

        Librarian librarian = new Librarian();
        librarian.setUser(user);
        librarian.setDepartment(librarianDto.getDepartment());
        librarian.setPosition(librarianDto.getPosition());
        librarian.setWorkplace(librarianDto.getWorkplace());

        return librarian;
    }
}