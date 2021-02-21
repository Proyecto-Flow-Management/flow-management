package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.service.IConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.Condition;
import com.proyecto.flowmanagement.backend.persistence.repository.IConditionRepo;

@Service
public class ConditionServiceImpl implements IConditionService {

	@Autowired
	private IConditionRepo repo;
	
	@Override
	public Condition add(Condition condition) {
		return repo.save(condition);
	}

	@Override
	public Condition update(Condition condition) {
		return repo.save(condition);
	}

	@Override
	public List<Condition> getAll() {
		return repo.findAll();
	}

	@Override
	public Condition getById(Integer id) {
		Optional<Condition> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Condition();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
