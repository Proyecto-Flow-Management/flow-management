package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;

import java.util.List;

public interface IStepService {
	Step add(Step step);
	Step update(Step step);
	List<Step> getAll();
	Step getById(Integer id);
	void delete(Integer id);
}
