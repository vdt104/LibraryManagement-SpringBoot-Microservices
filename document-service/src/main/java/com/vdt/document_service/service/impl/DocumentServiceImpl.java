package com.vdt.document_service.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.vdt.document_service.dto.DocumentDTO;
import com.vdt.document_service.entity.Author;
import com.vdt.document_service.entity.Category;
import com.vdt.document_service.entity.Document;
import com.vdt.document_service.exception.DocumentCodeAlreadyExistsException;
import com.vdt.document_service.exception.ResourceNotFoundException;
import com.vdt.document_service.mapper.DocumentMapper;
import com.vdt.document_service.repository.AuthorRepository;
import com.vdt.document_service.repository.CategoryRepository;
import com.vdt.document_service.repository.DocumentRepository;
import com.vdt.document_service.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public DocumentDTO getDocumentById(String id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));

        return DocumentMapper.toDTO(document);
    }

    @Override
    public Page<DocumentDTO> getAllDocuments(PageRequest pageRequest) {
        return documentRepository.findAll(pageRequest)
                .map(DocumentMapper::toDTO);
    }

    @Override
    public DocumentDTO createDocument(DocumentDTO documentDto) {
        Optional<Document> existingDocument = documentRepository.findByDocumentCode(documentDto.getDocumentCode());

        if (existingDocument.isPresent()) {
            throw new DocumentCodeAlreadyExistsException("Document code already exists");
        }

        // Find the category by name
        Optional<Category> categoryOptional = categoryRepository.findById(documentDto.getCategory());
        if (!categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category not found");
        }
        Category category = categoryOptional.get();

        // Handle authors
        Set<Author> authors = new HashSet<>();
        if (documentDto.getAuthors() != null) {
            authors = documentDto.getAuthors().stream()
                    .map(authorDto -> {
                        Optional<Author> existingAuthor = authorRepository.findByName(authorDto.getName());
                        if (existingAuthor.isPresent()) {
                            return existingAuthor.get();
                        } else {
                            Author author = new Author();
                            author.setName(authorDto.getName());
                            return authorRepository.save(author);
                        }
                    })
                    .collect(Collectors.toSet());
        }

        Document document = DocumentMapper.toEntity(documentDto);
        document.setCategory(category);
        document.setAuthors(authors);
        document.setQuantity(0);
        Document savedDocument = documentRepository.save(document);

        return DocumentMapper.toDTO(savedDocument);
    }
}
