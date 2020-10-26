package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Condition;

public interface IConditionService {

	Condition add(Condition condition);
	Condition update(Condition condition);
	List<Condition> getAll();
	Condition getById(Integer id);
	void delete(Integer id);
}
