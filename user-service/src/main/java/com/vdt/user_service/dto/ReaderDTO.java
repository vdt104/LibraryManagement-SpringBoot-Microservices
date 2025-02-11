package com.vdt.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReaderDTO extends UserDTO {

    @JsonProperty("student_id")
    private String studentId;
}
