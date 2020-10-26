package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IConditionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionType;
import com.proyecto.flowmanagement.backend.persistence.repository.IConditionTypeRepo;

@Service
public class ConditionTypeServiceImpl implements IConditionTypeService {
	
	@Autowired
	private IConditionTypeRepo repo;
	
	@Override
	public ConditionType add(ConditionType conditionType) {
		return repo.save(conditionType);
	}

	@Override
	public ConditionType update(ConditionType conditionType) {
		return repo.save(conditionType);
	}

	@Override
	public List<ConditionType> getAll() {
		return repo.findAll();
	}

	@Override
	public ConditionType getById(Integer id) {
		Optional<ConditionType> op = repo.findById(id);
		return op.isPresent() ? op.get() : new ConditionType();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}

}
