package com.proyecto.flowmanagement.backend.service;
import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationType;

public interface IOperationTypeService {

	OperationType add(OperationType operationType);
	OperationType update(OperationType operationType);
	List<OperationType> getAll();
	OperationType getById(Integer id);
	void delete(Integer id);
}
