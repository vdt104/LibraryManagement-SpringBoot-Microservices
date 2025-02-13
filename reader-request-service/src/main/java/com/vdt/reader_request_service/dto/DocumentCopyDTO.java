package com.vdt.reader_request_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCopyDTO {
    private String code;

    @JsonProperty("document_id")
    private String documentId;

    private String location;

    private String status;
}
