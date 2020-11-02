package com.proyecto.flowmanagement.backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;

public interface IGuideRepo extends JpaRepository<Guide,Long>{

}
