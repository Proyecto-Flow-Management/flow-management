package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ValidationServiceImpl
{
    public ValidationDTO validarGuia(Guide guide)
    {
        return guide.validarGuia();
    }
}
