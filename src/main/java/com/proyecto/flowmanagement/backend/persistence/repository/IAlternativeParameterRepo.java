package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.AlternativeParameter;

public interface IAlternativeParameterRepo extends JpaRepository<AlternativeParameter,Integer>{

}
