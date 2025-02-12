package com.vdt.reader_service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @JsonProperty("full_name")
    private String fullName;

    private Date dob;

    private String gender;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    @JsonProperty("identification_number")
    private String identificationNumber;

    @NotBlank(message = "Email is required")
    private String email;

    @JsonIgnore
    private String password;

    @JsonProperty("retype_password")
    @JsonIgnore
    private String retypePassword;

    @JsonProperty("role_id")
    private Long roleId;

    @JsonProperty("is_active")
    private boolean isActive;
}
