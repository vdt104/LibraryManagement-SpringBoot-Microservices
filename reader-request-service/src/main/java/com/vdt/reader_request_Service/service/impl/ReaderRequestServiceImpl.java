package com.vdt.reader_request_Service.service.impl;

import com.vdt.reader_request_Service.dto.DocumentCopyDTO;
import com.vdt.reader_request_Service.dto.ReaderCardDTO;
import com.vdt.reader_request_Service.dto.ReaderRequestDTO;
import com.vdt.reader_request_Service.entity.DocumentCopy;
import com.vdt.reader_request_Service.entity.Reader;
import com.vdt.reader_request_Service.entity.ReaderRequest;
import com.vdt.reader_request_Service.exception.DocumentCopyNotAvailableException;
import com.vdt.reader_request_Service.exception.ReaderCardNotActiveException;
import com.vdt.reader_request_Service.exception.ResourceNotFoundException;
import com.vdt.reader_request_Service.mapper.ReaderRequestMapper;
import com.vdt.reader_request_Service.repository.ReaderRepository;
import com.vdt.reader_request_Service.repository.ReaderRequestRepository;
import com.vdt.reader_request_Service.service.ReaderRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReaderRequestServiceImpl implements ReaderRequestService {

    private final ReaderRepository readerRepository;
    private final ReaderRequestRepository readerRequestRepository;
    private final RestTemplate restTemplate;

    @Override
    public ReaderRequestDTO getReaderRequestById(String id) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        return ReaderRequestMapper.toDTO(readerRequest);
    }

    @Override
    public ReaderRequestDTO acceptReaderRequest(String id, ReaderRequestDTO ReaderRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        Set<DocumentCopy> documentCopies = readerRequest.getDocumentCopies();
        for (DocumentCopy documentCopy : documentCopies) {
            if (documentCopy.getStatus() != DocumentCopy.Status.AVAILABLE) {
                throw new DocumentCopyNotAvailableException("Document copy " + documentCopy.getDocumentCopyCode() + " is not available");
            }
        }


        for (DocumentCopy documentCopy : documentCopies) {
            // documentCopy.setStatus(DocumentCopy.Status.BORROWED);
            // documentCopyRepository.save(documentCopy);
            restTemplate.put("http://document-service/api/v1/document_copies/" + documentCopy.getDocumentCopyCode() + "/status",
                            new DocumentCopyDTO(null, null, null, "BORROWED"));
        }

        readerRequest.setStatus(ReaderRequest.Status.ACCEPTED);

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        if (ReaderRequestDTO.getNotes() != null) {
            readerRequest.setNotes(ReaderRequestDTO.getNotes());
        }

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }

    @Override
    public ReaderRequestDTO rejectReaderRequest(String id, ReaderRequestDTO ReaderRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.REJECTED);

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        if (ReaderRequestDTO.getNotes() != null) {
            readerRequest.setNotes(ReaderRequestDTO.getNotes());
        }

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }

    @Override
    public ReaderRequestDTO borrowReaderRequest(String id, ReaderRequestDTO ReaderRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.BORROWED);
        readerRequest.setDateBorrowed(new Date());

        if (ReaderRequestDTO.getNotes() != null) {
            readerRequest.setNotes(ReaderRequestDTO.getNotes());
        }

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }

    @Override
    public ReaderRequestDTO returnReaderRequest(String id, ReaderRequestDTO ReaderRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.RETURNED);
        readerRequest.setDateReturned(new Date());

        if (ReaderRequestDTO.getPenaltyFee() > 0.0) {
            readerRequest.setPenaltyFee(ReaderRequestDTO.getPenaltyFee());
        }

        if (ReaderRequestDTO.getNotes() != null) {
            readerRequest.setNotes(ReaderRequestDTO.getNotes());
        }

        for (DocumentCopy documentCopy : readerRequest.getDocumentCopies()) {
            // documentCopy.setStatus(DocumentCopy.Status.AVAILABLE);
            // documentCopyRepository.save(documentCopy);
            restTemplate.put("http://document-service/api/v1/document_copies/" + documentCopy.getDocumentCopyCode() + "/status",
            new DocumentCopyDTO(null, null, null, "AVAILABLE"));
        }

        return ReaderRequestMapper.toDTO(readerRequestRepository.save(readerRequest));
    }

    @Override
    public ReaderRequestDTO cancelReaderRequest(String id, ReaderRequestDTO ReaderRequestDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReaderRequest", "id", id));

        readerRequest.setStatus(ReaderRequest.Status.CANCELLED);

        return ReaderRequestMapper.toDTO(readerRequestRepository.save(readerRequest));
    }

    @Override
    public ReaderRequestDTO createReaderRequest(ReaderRequestDTO ReaderRequestDTO) {
        Reader reader = readerRepository.findById(ReaderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reader", "id", ReaderRequestDTO.getUserId()));

        ReaderCardDTO readerCardDTO = restTemplate.getForObject("http://reader-service/api/v1/reader_cards/" + reader.getUserId(), ReaderCardDTO.class);
    
        if (readerCardDTO == null || !readerCardDTO.getStatus().equals("ACTIVE")) {
            throw new ReaderCardNotActiveException("Reader card is not active");
        }

        Set<DocumentCopy> documentCopies = new HashSet<>();
        for (DocumentCopyDTO documentCopyDto : ReaderRequestDTO.getDocumentCopies()) {
            DocumentCopyDTO documentCopy = restTemplate.getForObject(
                    "http://document-service/api/v1/document_copies/" + documentCopyDto.getCode(), 
                    DocumentCopyDTO.class);
            
            if (documentCopy == null) {
                throw new ResourceNotFoundException("DocumentCopy", "code", documentCopyDto.getCode());
            }

            if (!documentCopy.getStatus().equals("AVAILABLE")) {
                throw new DocumentCopyNotAvailableException("Document copy " + documentCopyDto.getCode() + " is not available");
            }

            documentCopies.add(new DocumentCopy(documentCopy.getCode(), documentCopy.getLocation(), DocumentCopy.Status.valueOf(documentCopy.getStatus())));
        }

        ReaderRequest readerRequest = new ReaderRequest();
        readerRequest.setReader(reader);
        readerRequest.setStatus(ReaderRequest.Status.REQUESTED);
        readerRequest.setBorrowingPeriod(ReaderRequestDTO.getBorrowingPeriod());
        readerRequest.setDocumentCopies(documentCopies);
        readerRequest.setPenaltyFee(0.0);
        readerRequest.setDocumentCopies(documentCopies);

        ReaderRequest savedReaderRequest = readerRequestRepository.save(readerRequest);

        savedReaderRequest.getReader().setUserId(null);

        return ReaderRequestMapper.toDTO(savedReaderRequest);
    }
}
