package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionType;

public interface IConditionTypeService {

	ConditionType add(ConditionType conditionType);
	ConditionType update(ConditionType conditionType);
	List<ConditionType> getAll();
	ConditionType getById(Integer id);
	void delete(Integer id);
}
