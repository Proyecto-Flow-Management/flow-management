package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IAlternativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.repository.IAlternativeRepo;

@Service
public class AlternativeServiceImpl implements IAlternativeService {

	@Autowired
	private IAlternativeRepo repo;
	
	@Override
	public Alternative add(Alternative alternative) {
		return repo.save(alternative);
	}

	@Override
	public Alternative update(Alternative alternative) {
		return repo.save(alternative);
	}

	@Override
	public List<Alternative> getAll() {
		return repo.findAll();
	}

	@Override
	public Alternative getById(Integer id) {
		Optional<Alternative> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Alternative();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
