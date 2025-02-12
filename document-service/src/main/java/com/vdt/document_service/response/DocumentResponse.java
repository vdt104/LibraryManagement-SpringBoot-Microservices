package com.vdt.document_service.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


import com.vdt.document_service.dto.AuthorDTO;
import com.vdt.document_service.dto.DocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentResponse {
    @JsonProperty("document_code")
    private String documentCode;
    private String title;
    private List<AuthorDTO> author;
    private String publisher;

    public static DocumentResponse fromDocumentDto(DocumentDTO documentDto) {
        return DocumentResponse.builder()
                .documentCode(documentDto.getDocumentCode())
                .title(documentDto.getTitle())
                .author(documentDto.getAuthors())
                .publisher(documentDto.getPublisher())
                .build();
    }
}
