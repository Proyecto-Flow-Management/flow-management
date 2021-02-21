package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Component;

public interface IComponentService {

	Component add(Component component);
	Component update(Component component);
	List<Component> getAll();
	Component getById(Integer id);
	void delete(Integer id);
}
