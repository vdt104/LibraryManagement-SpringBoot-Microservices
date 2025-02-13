package com.vdt.reader_request_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderRequestDocumentEvent {
    private String message;
    private String userId;
}