package com.vdt.document_service.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentListResponse {
    private List<DocumentResponse> documents;
    private int totalPages;
}

