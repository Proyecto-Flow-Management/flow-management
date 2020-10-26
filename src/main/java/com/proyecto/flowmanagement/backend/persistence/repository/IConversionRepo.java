package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Conversion;

public interface IConversionRepo extends JpaRepository<Conversion,Integer> {

}
