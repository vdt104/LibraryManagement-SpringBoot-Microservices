package com.vdt.document_service.mapper;

import com.vdt.document_service.dto.AuthorDTO;
import com.vdt.document_service.dto.DocumentDTO;
import com.vdt.document_service.entity.Document;

import java.util.stream.Collectors;

public class DocumentMapper {
    public static DocumentDTO toDTO(Document document) {
        DocumentDTO documentDto = new DocumentDTO();

        documentDto.setDocumentCode(document.getDocumentCode());
        documentDto.setTitle(document.getTitle());
        documentDto.setTopic(document.getTopic());
        documentDto.setDescription(document.getDescription());
        documentDto.setNote(document.getNote());
        documentDto.setCategory(document.getCategory().getCode());
        documentDto.setYearPublished(document.getYearPublished());
        documentDto.setPublisher(document.getPublisher());
        documentDto.setQuantity(document.getQuantity());
        documentDto.setAuthors(document.getAuthors().stream()
                .map(author -> new AuthorDTO(author.getId(), author.getName()))
                .collect(Collectors.toList()));

        return documentDto;
    }

    public static Document toEntity(DocumentDTO documentDto) {
        Document document = new Document();

        document.setDocumentCode(documentDto.getDocumentCode());
        document.setTitle(documentDto.getTitle());
        document.setTopic(documentDto.getTopic());
        document.setDescription(documentDto.getDescription());
        document.setNote(documentDto.getNote());
        document.setYearPublished(documentDto.getYearPublished());
        document.setPublisher(documentDto.getPublisher());
        document.setQuantity(documentDto.getQuantity());

        return document;
    }
}
