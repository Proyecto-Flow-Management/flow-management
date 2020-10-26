package com.proyecto.flowmanagement.backend.persistence.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;

public interface IStepRepo extends JpaRepository<Step,Integer>{

}
