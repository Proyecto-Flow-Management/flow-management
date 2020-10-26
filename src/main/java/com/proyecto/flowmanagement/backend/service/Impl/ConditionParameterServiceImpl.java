package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IConditionParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.backend.persistence.repository.IConditionParameterRepo;

@Service
public class ConditionParameterServiceImpl implements IConditionParameterService {

	@Autowired
	private IConditionParameterRepo repo;
	
	@Override
	public ConditionParameter add(ConditionParameter conditionParameter) {
		return repo.save(conditionParameter);
	}

	@Override
	public ConditionParameter update(ConditionParameter conditionParameter) {
		return repo.save(conditionParameter);
	}

	@Override
	public List<ConditionParameter> getAll() {
		return repo.findAll();
	}

	@Override
	public ConditionParameter getById(Integer id) {
		Optional<ConditionParameter> op = repo.findById(id);
		return op.isPresent() ? op.get() : new ConditionParameter();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
