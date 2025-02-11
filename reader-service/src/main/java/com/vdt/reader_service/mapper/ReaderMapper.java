package com.vdt.reader_service.mapper;

import com.vdt.reader_service.dto.ReaderDTO;
import com.vdt.reader_service.entity.Reader;

public class ReaderMapper {

    public static ReaderDTO toDTO(Reader reader) {
        ReaderDTO readerDTO = new ReaderDTO();

        readerDTO.setStudentId(reader.getStudentId());

        return readerDTO;
    }

    public static Reader toEntity(ReaderDTO readerDTO) {
        Reader reader = new Reader();

        reader.setStudentId(readerDTO.getStudentId());

        return reader;
    }
    
}
