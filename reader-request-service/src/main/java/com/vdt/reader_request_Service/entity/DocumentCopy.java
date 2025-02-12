package com.vdt.reader_request_Service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_copy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCopy {

    @Id
    @Column(name = "document_copy_code", nullable = false, unique = true)
    private String documentCopyCode;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        AVAILABLE, NOT_AVAILABLE, BORROWED
    }
}
