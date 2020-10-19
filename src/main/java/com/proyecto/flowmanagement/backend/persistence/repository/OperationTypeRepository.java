package com.proyecto.flowmanagement.backend.persistence.repository;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation_Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTypeRepository extends JpaRepository<Operation_Type, Long> {
}
