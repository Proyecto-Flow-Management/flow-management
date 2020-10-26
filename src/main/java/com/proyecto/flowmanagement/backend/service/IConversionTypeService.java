package com.proyecto.flowmanagement.backend.service;
import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.ConversionType;

public interface IConversionTypeService {

	ConversionType add(ConversionType conversionType);
	ConversionType update(ConversionType conversionType);
	List<ConversionType> getAll();
	ConversionType getById(Integer id);
	void delete(Integer id);
}
