package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Condition;

public interface IConditionRepo extends JpaRepository<Condition,Integer> {

}
