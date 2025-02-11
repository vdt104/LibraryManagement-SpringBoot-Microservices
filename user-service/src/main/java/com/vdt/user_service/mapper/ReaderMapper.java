package com.vdt.user_service.mapper;

import com.vdt.user_service.dto.ReaderDTO;
import com.vdt.user_service.entity.Reader;
import com.vdt.user_service.entity.User;

public class ReaderMapper {

    public static ReaderDTO toDTO(Reader reader) {
        ReaderDTO readerDTO = new ReaderDTO();

        readerDTO.setFullName(reader.getUser().getFullName());
        readerDTO.setDob(reader.getUser().getDob());
        readerDTO.setGender(reader.getUser().getGender().name());
        readerDTO.setPhoneNumber(reader.getUser().getPhoneNumber());
        readerDTO.setAddress(reader.getUser().getAddress());
        readerDTO.setIdentificationNumber(reader.getUser().getIdentificationNumber());
        readerDTO.setEmail(reader.getUser().getEmail());
        readerDTO.setStudentId(reader.getStudentId());

        return readerDTO;
    }

    public static Reader toEntity(ReaderDTO readerDTO) {
        User user = new User();
        user.setFullName(readerDTO.getFullName());
        user.setDob(readerDTO.getDob());
        user.setGender(User.Gender.valueOf(readerDTO.getGender()));
        user.setPhoneNumber(readerDTO.getPhoneNumber());
        user.setAddress(readerDTO.getAddress());
        user.setIdentificationNumber(readerDTO.getIdentificationNumber());
        user.setEmail(readerDTO.getEmail());

        Reader reader = new Reader();
        reader.setUser(user);
        reader.setStudentId(readerDTO.getStudentId());

        return reader;
    }
}
