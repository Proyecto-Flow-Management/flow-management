package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.ComponentType;

public interface IComponentTypeService {
	ComponentType add(ComponentType componentType);
	ComponentType update(ComponentType componentType);
	List<ComponentType> getAll();
	ComponentType getById(Integer id);
	void delete(Integer id);
}
