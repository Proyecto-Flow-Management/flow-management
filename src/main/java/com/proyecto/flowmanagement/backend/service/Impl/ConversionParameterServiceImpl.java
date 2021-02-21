package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IConversionParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.ConversionParameter;
import com.proyecto.flowmanagement.backend.persistence.repository.IConversionParameterRepo;

@Service
public class ConversionParameterServiceImpl implements IConversionParameterService {

	@Autowired
	private IConversionParameterRepo repo;
	
	@Override
	public ConversionParameter add(ConversionParameter conversionParameter) {
		return repo.save(conversionParameter);
	}

	@Override
	public ConversionParameter update(ConversionParameter conversionParameter) {
		return repo.save(conversionParameter);
	}

	@Override
	public List<ConversionParameter> getAll() {
		return repo.findAll();
	}

	@Override
	public ConversionParameter getById(Integer id) {
		Optional<ConversionParameter> op = repo.findById(id);
		return op.isPresent() ? op.get() : new ConversionParameter();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
