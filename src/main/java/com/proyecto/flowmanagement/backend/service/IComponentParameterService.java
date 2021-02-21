package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.ComponentParameter;

public interface IComponentParameterService {

	ComponentParameter add(ComponentParameter componentParameter);
	ComponentParameter update(ComponentParameter componentParameter);
	List<ComponentParameter> getAll();
	ComponentParameter getById(Integer id);
	void delete(Integer id);
}
