package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionType;


public interface IConditionTypeRepo extends JpaRepository<ConditionType,Integer>{

}
