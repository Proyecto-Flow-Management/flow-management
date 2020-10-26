package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;

public interface IOperationParameterService {

	OperationParameter add(OperationParameter operationParameter);
	OperationParameter update(OperationParameter operationParameter);
	List<OperationParameter> getAll();
	OperationParameter getById(Integer id);
	void delete(Integer id);
}
