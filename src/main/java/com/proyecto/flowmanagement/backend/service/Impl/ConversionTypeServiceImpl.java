package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IConversionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.ConversionType;
import com.proyecto.flowmanagement.backend.persistence.repository.IConversionTypeRepo;

@Service
public class ConversionTypeServiceImpl implements IConversionTypeService {
	@Autowired
	private IConversionTypeRepo repo;
	
	@Override
	public ConversionType add(ConversionType conversionType) {
		return repo.save(conversionType);
	}

	@Override
	public ConversionType update(ConversionType conversionType) {
		return repo.save(conversionType);
	}

	@Override
	public List<ConversionType> getAll() {
		return repo.findAll();
	}

	@Override
	public ConversionType getById(Integer id) {
		Optional<ConversionType> op = repo.findById(id);
		return op.isPresent() ? op.get() : new ConversionType();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
