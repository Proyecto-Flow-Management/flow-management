package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ValidationServiceImpl
{
    public List<String> validarGuia(Guide guide)
    {
        List<String> errores = new LinkedList<>();

        errores.addAll(guide.validarGuia());

        return  errores;
    }
}
