package com.vdt.reader_request_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReaderDTO {

    @JsonProperty("student_id")
    private String studentId;
}