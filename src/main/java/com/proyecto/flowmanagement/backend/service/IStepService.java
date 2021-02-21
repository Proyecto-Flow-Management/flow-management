package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;

public interface IStepService {
	Step add(Step step);
	Step update(Step step);
	List<Step> getAll();
	Step getById(Long id);
	void delete(Long id);
}
