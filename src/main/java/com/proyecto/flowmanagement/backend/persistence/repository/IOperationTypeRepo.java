package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationType;

public interface IOperationTypeRepo extends JpaRepository<OperationType,Integer>{

}
