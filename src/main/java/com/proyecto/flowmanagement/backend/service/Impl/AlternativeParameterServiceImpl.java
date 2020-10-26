package com.proyecto.flowmanagement.backend.service.Impl;

import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IAlternativeParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.AlternativeParameter;
import com.proyecto.flowmanagement.backend.persistence.repository.IAlternativeParameterRepo;

@Service
public class AlternativeParameterServiceImpl implements IAlternativeParameterService {

	@Autowired
	private IAlternativeParameterRepo repo;
	
	@Override
	public AlternativeParameter add(AlternativeParameter alternativeParameter) {
		return repo.save(alternativeParameter);
	}

	@Override
	public AlternativeParameter update(AlternativeParameter alternativeParameter) {
		return repo.save(alternativeParameter);
	}

	@Override
	public List<AlternativeParameter> getAll() {
		return repo.findAll();
	}

	@Override
	public AlternativeParameter getById(Integer id) {
		Optional<AlternativeParameter> op = repo.findById(id);
		return op.isPresent() ? op.get() : new AlternativeParameter();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
