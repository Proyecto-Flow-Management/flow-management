package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;

public interface IAlternativeService {

	Alternative add(Alternative alternative);
	Alternative update(Alternative alternative);
	List<Alternative> getAll();
	Alternative getById(Integer id);
	void delete(Integer id);
}
