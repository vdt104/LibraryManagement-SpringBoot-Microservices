package com.vdt.reader_service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

    @OneToOne(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReaderCard readerCard;
}
