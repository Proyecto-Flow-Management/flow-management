package com.proyecto.flowmanagement.backend.persistence.repository;

import com.proyecto.flowmanagement.backend.persistence.entity.Errores;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IErroresRepo  extends JpaRepository<Errores,Long> {
}
