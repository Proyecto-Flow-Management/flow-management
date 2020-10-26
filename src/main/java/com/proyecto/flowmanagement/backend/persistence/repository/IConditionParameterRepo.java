package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;

public interface IConditionParameterRepo extends JpaRepository<ConditionParameter,Integer>{

}
