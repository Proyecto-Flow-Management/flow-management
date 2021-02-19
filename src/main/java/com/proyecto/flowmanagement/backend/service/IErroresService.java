package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.persistence.entity.Errores;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;

import java.util.List;

public interface IErroresService {
    Errores add(Errores guide);
    List<Errores> getAll();
}
