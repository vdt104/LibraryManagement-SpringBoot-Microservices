// package com.vdt.user_service.entity;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.MapsId;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Table(name = "reader")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Reader {

//     @Id
//     @Column(name = "user_id")
//     private String userId;

//     @OneToOne
//     @MapsId
//     @JoinColumn(name = "user_id", nullable = false, unique = true)
//     private User user;

//     @Column(name = "student_id", unique = true)
//     private String studentId;
// }
