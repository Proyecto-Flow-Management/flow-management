package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation;

public interface IOperationRepo extends JpaRepository<Operation,Integer>{

}
