package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;

public interface IConditionParameterService {

	ConditionParameter add(ConditionParameter conditionParameter);
	ConditionParameter update(ConditionParameter conditionParameter);
	List<ConditionParameter> getAll();
	ConditionParameter getById(Integer id);
	void delete(Integer id);
}
