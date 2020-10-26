package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Conversion;

public interface IConversionService {

	Conversion add(Conversion conversion);
	Conversion update(Conversion conversion);
	List<Conversion> getAll();
	Conversion getById(Integer id);
	void delete(Integer id);
}
