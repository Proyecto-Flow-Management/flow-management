package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IComponentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.ComponentType;
import com.proyecto.flowmanagement.backend.persistence.repository.IComponentTypeRepo;

@Service
public class ComponentTypeServiceImpl implements IComponentTypeService {

	@Autowired
	private IComponentTypeRepo repo;
	
	@Override
	public ComponentType add(ComponentType componentType) {
		return repo.save(componentType);
	}

	@Override
	public ComponentType update(ComponentType componentType) {
		return repo.save(componentType);
	}

	@Override
	public List<ComponentType> getAll() {
		return repo.findAll();
	}

	@Override
	public ComponentType getById(Integer id) {
		Optional<ComponentType> op = repo.findById(id);
		return op.isPresent() ? op.get() : new ComponentType();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
