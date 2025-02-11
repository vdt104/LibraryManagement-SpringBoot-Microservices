package com.vdt.user_service.repository;

import com.vdt.user_service.entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianRepository extends JpaRepository<Librarian, String> {
}
