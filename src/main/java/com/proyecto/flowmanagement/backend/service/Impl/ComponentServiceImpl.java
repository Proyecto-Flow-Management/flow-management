package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.Component;
import com.proyecto.flowmanagement.backend.persistence.repository.IComponentRepo;

@Service
public class ComponentServiceImpl implements IComponentService {

	@Autowired
	private IComponentRepo repo;
	
	@Override
	public Component add(Component component) {
		return repo.save(component);
	}

	@Override
	public Component update(Component component) {
		return repo.save(component);
	}

	@Override
	public List<Component> getAll() {
		return repo.findAll();
	}

	@Override
	public Component getById(Integer id) {
		Optional<Component> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Component();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
