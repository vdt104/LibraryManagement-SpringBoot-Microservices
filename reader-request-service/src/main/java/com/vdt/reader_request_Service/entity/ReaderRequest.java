package com.vdt.reader_request_Service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "reader_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Reader reader;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_borrowed")
    private Date dateBorrowed;

    @Column(name = "borrowing_period")
    private int borrowingPeriod;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_returned")
    private Date dateReturned;

    @Column(name = "penalty_fee")
    private Double penaltyFee;

    @Column(name = "notes")
    private String notes;

    @ManyToMany
    @JoinTable(
            name = "reader_request_detail",
            joinColumns = @JoinColumn(name = "reader_request_id"),
            inverseJoinColumns = @JoinColumn(name = "document_copy_code")
    )
    private Set<DocumentCopy> documentCopies;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        REQUESTED, ACCEPTED, REJECTED, BORROWED, RETURNED, OVERDUE, CANCELLED
    }
}
