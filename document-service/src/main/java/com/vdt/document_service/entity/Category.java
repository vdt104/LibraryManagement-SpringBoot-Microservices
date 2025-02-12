package com.vdt.document_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    private String code;

    @Column(name = "name", nullable = false)
    private String name;
}
