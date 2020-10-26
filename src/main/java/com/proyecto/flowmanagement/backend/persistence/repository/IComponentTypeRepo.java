package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.ComponentType;

public interface IComponentTypeRepo extends JpaRepository<ComponentType,Integer>{

}
