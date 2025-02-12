package com.vdt.document_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.vdt.document_service.dto.DocumentCopyDTO;
import com.vdt.document_service.entity.Document;
import com.vdt.document_service.entity.DocumentCopy;
import com.vdt.document_service.exception.ResourceNotFoundException;
import com.vdt.document_service.repository.DocumentCopyRepository;
import com.vdt.document_service.repository.DocumentRepository;
import com.vdt.document_service.service.DocumentCopyService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentCopyServiceImpl implements DocumentCopyService {

    private final DocumentRepository documentRepository;
    private final DocumentCopyRepository documentCopyRepository;

    @Override
    public DocumentCopyDTO updateDocumentCopyStatus(String code, DocumentCopyDTO documentCopyDto) {
        // Kiểm tra xem documentCopyCode có tồn tại hay không
        Optional<DocumentCopy> documentCopyOptional = documentCopyRepository.findByDocumentCopyCode(code);
        if (!documentCopyOptional.isPresent()) {
            throw new ResourceNotFoundException("Document Copy", "document_copy", code);
        }
        DocumentCopy documentCopy = documentCopyOptional.get();

        // Cập nhật trạng thái của DocumentCopy
        documentCopy.setStatus(DocumentCopy.Status.valueOf(documentCopyDto.getStatus()));

        // Lưu DocumentCopy vào cơ sở dữ liệu
        documentCopyRepository.save(documentCopy);

        // Trả về DocumentCopyDto
        documentCopyDto.setCode(code);
        documentCopyDto.setDocumentId(documentCopy.getDocument().getDocumentCode());
        return documentCopyDto;
    }

    @Override
    public List<DocumentCopyDTO> getAllDocumentCopiesOfDocument(String documentCode) {
        // Kiểm tra xem documentId có tồn tại hay không
        Optional<Document> documentOptional = documentRepository.findByDocumentCode(documentCode);
        if (!documentOptional.isPresent()) {
            throw new ResourceNotFoundException("Document Code", "document_code", documentCode);
        }

        Document document = documentOptional.get();

        // Lấy ra tất cả DocumentCopy của Document
        List<DocumentCopy> documentCopies = documentCopyRepository.findByDocument(document);

        // Chuyển đổi DocumentCopy sang DocumentCopyDto
        return documentCopies.stream()
            .map(documentCopy -> {
                DocumentCopyDTO documentCopyDto = new DocumentCopyDTO();
                documentCopyDto.setCode(documentCopy.getDocumentCopyCode());
                documentCopyDto.setDocumentId(documentCode);
                documentCopyDto.setLocation(documentCopy.getLocation());
                documentCopyDto.setStatus(documentCopy.getStatus().name());
                return documentCopyDto;
            })
            .toList();
    }

    @Override
    public DocumentCopyDTO createDocumentCopy(String documentId, DocumentCopyDTO documentCopyDto) {
        // Kiểm tra xem documentId có tồn tại hay không
        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (!documentOptional.isPresent()) {
            throw new IllegalArgumentException("Document not found");
        }
        Document document = documentOptional.get();

        // Tạo DocumentCopyCode
        String documentCopyCode = generateDocumentCopyCode(document);

        // Tạo DocumentCopy mới
        DocumentCopy documentCopy = new DocumentCopy();
        documentCopy.setDocumentCopyCode(documentCopyCode);
        documentCopy.setDocument(document);
        documentCopy.setLocation(documentCopyDto.getLocation());
        documentCopy.setStatus(DocumentCopy.Status.AVAILABLE);

        // Lưu DocumentCopy vào cơ sở dữ liệu
        documentCopyRepository.save(documentCopy);

        // Tăng quantity trong Document
        document.setQuantity(document.getQuantity() + 1);
        documentRepository.save(document);

        // Trả về DocumentCopyDto
        documentCopyDto.setCode(documentCopyCode);
        documentCopyDto.setDocumentId(documentId);
        documentCopyDto.setStatus(DocumentCopy.Status.AVAILABLE.name());
        
        return documentCopyDto;
    }

    @Override
    public DocumentCopyDTO updateDocumentCopy(String documentCode, DocumentCopyDTO documentCopyDto) {
        // Kiểm tra xem documentCode có tồn tại hay không
        Optional<DocumentCopy> documentCopyOptional = documentCopyRepository.findByDocumentCopyCode(documentCode);
        if (!documentCopyOptional.isPresent()) {
            throw new ResourceNotFoundException("Document Copy", "document_copy", documentCode);
        }
        DocumentCopy documentCopy = documentCopyOptional.get();

        // Cập nhật thông tin DocumentCopy
        documentCopy.setLocation(documentCopyDto.getLocation());
        documentCopy.setStatus(DocumentCopy.Status.valueOf(documentCopyDto.getStatus()));

        // Lưu DocumentCopy vào cơ sở dữ liệu
        documentCopyRepository.save(documentCopy);

        // Trả về DocumentCopyDto
        documentCopyDto.setCode(documentCode);
        documentCopyDto.setDocumentId(documentCopy.getDocument().getDocumentCode());
        return documentCopyDto;
    }

    private String generateDocumentCopyCode(Document document) {
        String documentCode = document.getDocumentCode();
        int copyCount = document.getQuantity() + 1;
        if (copyCount == 1) {
            return documentCode;
        } else {
            return documentCode + ".C" + copyCount;
        }
    }
}
