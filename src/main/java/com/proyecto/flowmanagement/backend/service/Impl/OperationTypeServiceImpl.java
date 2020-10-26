package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.persistence.repository.IOperationTypeRepo;
import com.proyecto.flowmanagement.backend.service.IOperationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationType;

@Service
public class OperationTypeServiceImpl implements IOperationTypeService {

	@Autowired
	private IOperationTypeRepo repo;
	
	@Override
	public OperationType add(OperationType operationType) {
		return repo.save(operationType);
	}

	@Override
	public OperationType update(OperationType operationType) {
		return repo.save(operationType);
	}

	@Override
	public List<OperationType> getAll() {
		return repo.findAll();
	}

	@Override
	public OperationType getById(Integer id) {
		Optional<OperationType> op = repo.findById(id);
		return op.isPresent() ? op.get() : new OperationType();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
		
	}

}
