package com.proyecto.flowmanagement.backend.repository;

import com.proyecto.flowmanagement.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
