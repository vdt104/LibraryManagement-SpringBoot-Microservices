package com.vdt.reader_service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ReaderCardDTO {

    @JsonProperty("user_id")
    private String userId;

    private String pin;

    @JsonProperty("issue_date")
    private Date issueDate;

    @JsonProperty("expiry_period")
    private int expiryPeriod;

    private String status;
}
