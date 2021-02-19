package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.persistence.entity.Errores;
import com.proyecto.flowmanagement.backend.persistence.repository.IConversionTypeRepo;
import com.proyecto.flowmanagement.backend.persistence.repository.IErroresRepo;
import com.proyecto.flowmanagement.backend.service.IErroresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErroresServiceImpl implements IErroresService {

    @Autowired
    private IErroresRepo repo;

    @Override
    public Errores add(Errores error) {
        return repo.save(error);
    }

    @Override
    public List<Errores> getAll() {
        return this.repo.findAll();
    }
}
