package com.vdt.reader_request_service.service.impl;

import com.vdt.reader_request_service.dto.DocumentCopyDTO;
import com.vdt.reader_request_service.dto.ReaderCardDTO;
import com.vdt.reader_request_service.dto.ReaderRequestDTO;
import com.vdt.reader_request_service.entity.ReaderRequest;
import com.vdt.reader_request_service.event.ReaderRequestDocumentEvent;
import com.vdt.reader_request_service.exception.DocumentCopyNotAvailableException;
import com.vdt.reader_request_service.exception.ReaderCardNotActiveException;
import com.vdt.reader_request_service.exception.ResourceNotFoundException;
import com.vdt.reader_request_service.kafka.ReaderRequestDocumentProducer;
import com.vdt.reader_request_service.mapper.ReaderRequestMapper;
import com.vdt.reader_request_service.repository.ReaderRequestRepository;
import com.vdt.reader_request_service.service.ReaderRequestService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReaderRequestServiceImpl implements ReaderRequestService {

    private final ReaderRequestRepository readerRequestRepository;
    private final RestTemplate restTemplate;
    private final ReaderRequestDocumentProducer readerRequestDocumentProducer;

    @Override
    public ReaderRequestDTO getReaderRequestById(String id) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        return ReaderRequestMapper.toDTO(readerRequest);
    }

    // @Override
    // public ReaderRequestDTO acceptReaderRequest(String id, ReaderRequestDTO readerRequestDTO) {
    //     ReaderRequest readerRequest = readerRequestRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

    //     Set<DocumentCopy> documentCopies = readerRequest.getDocumentCopies();
    //     for (DocumentCopy documentCopy : documentCopies) {
    //         if (documentCopy.getStatus() != DocumentCopy.Status.AVAILABLE) {
    //             throw new DocumentCopyNotAvailableException("Document copy " + documentCopy.getDocumentCopyCode() + " is not available");
    //         }
    //     }


    //     for (DocumentCopy documentCopy : documentCopies) {
    //         // documentCopy.setStatus(DocumentCopy.Status.BORROWED);
    //         // documentCopyRepository.save(documentCopy);
    //         restTemplate.put("http://document-service/api/v1/document_copies/" + documentCopy.getDocumentCopyCode() + "/status",
    //                         new DocumentCopyDTO(null, null, null, "BORROWED"));
    //     }

    //     readerRequest.setStatus(ReaderRequest.Status.ACCEPTED);

    //     ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

    //     if (readerRequestDTO.getNotes() != null) {
    //         readerRequest.setNotes(readerRequestDTO.getNotes());
    //     }

    //     return ReaderRequestMapper.toDTO(savedReaderRequest);
    // }

    @Override
    public ReaderRequestDTO acceptReaderRequest(String id, ReaderRequestDTO readerRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        Set<String> documentCopyCodes = readerRequest.getDocumentCopyCodes();
        for (String documentCopyCode : documentCopyCodes) {
            DocumentCopyDTO documentCopy = restTemplate.getForObject(
                    "http://document-service/api/v1/document_copies?document_copy_code=" + documentCopyCode,
                    DocumentCopyDTO.class);

            if (documentCopy == null) {
                throw new ResourceNotFoundException("DocumentCopy", "code", documentCopyCode);
            }

            if (!documentCopy.getStatus().equals("AVAILABLE")) {
                throw new DocumentCopyNotAvailableException("Document copy " + documentCopyCode + " is not available");
            }
        }

        for (String documentCopyCode : documentCopyCodes) {
            restTemplate.put("http://document-service/api/v1/document_copies/" + documentCopyCode + "/status",
                            new DocumentCopyDTO(null, null, null, "BORROWED"));
        }

        readerRequest.setStatus(ReaderRequest.Status.ACCEPTED);

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        if (readerRequestDTO.getNotes() != null) {
            readerRequest.setNotes(readerRequestDTO.getNotes());
        }

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }

    @Override
    public ReaderRequestDTO rejectReaderRequest(String id, ReaderRequestDTO readerRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.REJECTED);

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        if (readerRequestDTO.getNotes() != null) {
            readerRequest.setNotes(readerRequestDTO.getNotes());
        }

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }

    @Override
    public ReaderRequestDTO borrowReaderRequest(String id, ReaderRequestDTO readerRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.BORROWED);
        readerRequest.setDateBorrowed(new Date());

        if (readerRequestDTO.getNotes() != null) {
            readerRequest.setNotes(readerRequestDTO.getNotes());
        }

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }

    @Override
    public ReaderRequestDTO returnReaderRequest(String id, ReaderRequestDTO readerRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.RETURNED);
        readerRequest.setDateReturned(new Date());

        if (readerRequestDTO.getPenaltyFee() > 0.0) {
            readerRequest.setPenaltyFee(readerRequestDTO.getPenaltyFee());
        }

        if (readerRequestDTO.getNotes() != null) {
            readerRequest.setNotes(readerRequestDTO.getNotes());
        }

        // for (DocumentCopy documentCopy : readerRequest.getDocumentCopies()) {
        //     // documentCopy.setStatus(DocumentCopy.Status.AVAILABLE);
        //     // documentCopyRepository.save(documentCopy);
        //     restTemplate.put("http://document-service/api/v1/document_copies/" + documentCopy.getDocumentCopyCode() + "/status",
        //     new DocumentCopyDTO(null, null, null, "AVAILABLE"));
        // }

        for (String documentCopyCode : readerRequest.getDocumentCopyCodes()) {
            restTemplate.put("http://document-service/api/v1/document_copies/" + documentCopyCode + "/status",
                            new DocumentCopyDTO(null, null, null, "AVAILABLE"));
        }

        return ReaderRequestMapper.toDTO(readerRequestRepository.save(readerRequest));
    }

    @Override
    public ReaderRequestDTO cancelReaderRequest(String id) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.CANCELLED);

        ReaderRequestDocumentEvent readerRequestDocumentEvent = new ReaderRequestDocumentEvent("A reader has cancelled the request to borrow documents", readerRequest.getUserId());
        readerRequestDocumentProducer.sendMessage(readerRequestDocumentEvent);

        return ReaderRequestMapper.toDTO(readerRequestRepository.save(readerRequest));
    }

    @Override
    public ReaderRequestDTO createReaderRequest(ReaderRequestDTO readerRequestDTO) {
        try {
            restTemplate.getForObject("http://reader-service/api/v1/readers/" + readerRequestDTO.getUserId(), Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Reader", "id", readerRequestDTO.getUserId());
            } else {
                throw e;
            }
        }

        ReaderCardDTO readerCardDTO = restTemplate.getForObject("http://reader-service/api/v1/reader_cards?readers_id=" + readerRequestDTO.getUserId(), ReaderCardDTO.class);
    
        if (readerCardDTO == null || !readerCardDTO.getStatus().equals("ACTIVE")) {
            throw new ReaderCardNotActiveException("Reader card is not active");
        }

        // Set<DocumentCopy> documentCopies = new HashSet<>();
        Set<String> documentCopyCodes = new HashSet<>();

        for (DocumentCopyDTO documentCopyDto : readerRequestDTO.getDocumentCopies()) {
            DocumentCopyDTO documentCopy = restTemplate.getForObject(
                    "http://document-service/api/v1/document_copies?document_copy_code=" + documentCopyDto.getCode(),
                    DocumentCopyDTO.class);
            
            if (documentCopy == null) {
                throw new ResourceNotFoundException("DocumentCopy", "code", documentCopyDto.getCode());
            }

            if (!documentCopy.getStatus().equals("AVAILABLE")) {
                throw new DocumentCopyNotAvailableException("Document copy " + documentCopyDto.getCode() + " is not available");
            }

            // documentCopies.add(new DocumentCopy(documentCopy.getCode(), documentCopy.getLocation(), DocumentCopy.Status.valueOf(documentCopy.getStatus())));
            documentCopyCodes.add(documentCopyDto.getCode());
        }

        ReaderRequest readerRequest = new ReaderRequest();
        readerRequest.setUserId(readerRequestDTO.getUserId());
        readerRequest.setStatus(ReaderRequest.Status.REQUESTED);
        readerRequest.setBorrowingPeriod(readerRequestDTO.getBorrowingPeriod());
        readerRequest.setDocumentCopyCodes(documentCopyCodes);
        readerRequest.setPenaltyFee(0.0);

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        ReaderRequestDocumentEvent readerRequestDocumentEvent = new ReaderRequestDocumentEvent("A reader has requested to borrow documents", readerRequestDTO.getUserId());
        readerRequestDocumentProducer.sendMessage(readerRequestDocumentEvent);

        savedReaderRequest.setUserId(null);

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }
}
