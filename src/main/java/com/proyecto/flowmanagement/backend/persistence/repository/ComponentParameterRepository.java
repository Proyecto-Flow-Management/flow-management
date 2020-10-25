package com.proyecto.flowmanagement.backend.persistence.repository;


import com.proyecto.flowmanagement.backend.persistence.entity.Component_Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentParameterRepository extends JpaRepository<Component_Parameter, Long> {
}
