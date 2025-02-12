package com.vdt.reader_service.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reader")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reader {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "student_id", unique = true)
    private String studentId;

    @ManyToMany
    @JoinTable(
        name = "reader_bookshelf",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "document_copy_code")
    )
    private Set<DocumentCopy> documentCopies;
}
