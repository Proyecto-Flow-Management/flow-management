package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.ConversionType;


public interface IConversionTypeRepo extends JpaRepository<ConversionType,Integer> {

}
