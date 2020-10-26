package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation;

public interface IOperationService {
	Operation add(Operation operation);
	Operation update(Operation operation);
	List<Operation> getAll();
	Operation getById(Integer id);
	void delete(Integer id);
}
