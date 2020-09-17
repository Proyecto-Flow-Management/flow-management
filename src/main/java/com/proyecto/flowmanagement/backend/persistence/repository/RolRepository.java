package com.proyecto.flowmanagement.backend.persistence.repository;

import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolRepository  extends JpaRepository<Rol, Long> {
}
