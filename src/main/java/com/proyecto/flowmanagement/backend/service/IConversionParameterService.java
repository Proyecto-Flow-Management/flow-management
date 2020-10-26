package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.ConversionParameter;

public interface IConversionParameterService {

	ConversionParameter add(ConversionParameter conversionParameter);
	ConversionParameter update(ConversionParameter conversionParameter);
	List<ConversionParameter> getAll();
	ConversionParameter getById(Integer id);
	void delete(Integer id);
}
