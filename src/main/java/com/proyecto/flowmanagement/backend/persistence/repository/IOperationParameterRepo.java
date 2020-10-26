package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;

public interface IOperationParameterRepo extends JpaRepository<OperationParameter,Integer>{

}
