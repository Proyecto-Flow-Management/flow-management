package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.AlternativeParameter;

public interface IAlternativeParameterService {

	AlternativeParameter add(AlternativeParameter alternativeParameter);
	AlternativeParameter update(AlternativeParameter alternativeParameter);
	List<AlternativeParameter> getAll();
	AlternativeParameter getById(Integer id);
	void delete(Integer id);
}
