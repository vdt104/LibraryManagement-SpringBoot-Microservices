package com.vdt.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LibrarianDTO extends UserDTO {
    private String department;
    private String position;
    private String workplace;
}
