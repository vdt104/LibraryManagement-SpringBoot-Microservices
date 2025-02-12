package com.vdt.document_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DocumentCodeAlreadyExistsException extends RuntimeException {
    public DocumentCodeAlreadyExistsException(String message) {
        super(message);
    }
}
