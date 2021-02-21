package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.Conversion;
import com.proyecto.flowmanagement.backend.persistence.repository.IConversionRepo;

@Service
public class ConversionServiceImpl implements IConversionService {

	@Autowired
	private IConversionRepo repo;
	
	@Override
	public Conversion add(Conversion conversion) {
		return repo.save(conversion);
	}

	@Override
	public Conversion update(Conversion conversion) {
		return repo.save(conversion);
	}

	@Override
	public List<Conversion> getAll() {
		return repo.findAll();
	}

	@Override
	public Conversion getById(Integer id) {
		Optional<Conversion> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Conversion();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
