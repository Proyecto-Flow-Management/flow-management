package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.persistence.repository.IOperationRepo;
import com.proyecto.flowmanagement.backend.service.IOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation;

@Service
public class OperationServiceImpl implements IOperationService {

	@Autowired
	private IOperationRepo repo;
	
	@Override
	public Operation add(Operation operation) {
		return repo.save(operation);
	}

	@Override
	public Operation update(Operation operation) {
		return repo.save(operation);
	}

	@Override
	public List<Operation> getAll() {
		return repo.findAll();
	}

	@Override
	public Operation getById(Integer id) {
		Optional<Operation> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Operation();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
