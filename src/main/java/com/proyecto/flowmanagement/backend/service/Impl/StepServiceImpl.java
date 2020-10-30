package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.repository.IStepRepo;
import com.proyecto.flowmanagement.backend.service.IStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class StepServiceImpl implements IStepService {
	private static final Logger LOGGER = Logger.getLogger(Step.class.getName());
	
	@Autowired
	private IStepRepo repo;
	
	@Override
	public Step add(Step step) {
		return repo.save(step);
	}

	@Override
	public Step update(Step step) { return repo.save(step); }

	public void save(Step step) {
		if (step == null) {
			LOGGER.log(Level.SEVERE,
					"User is null. Are you sure you have connected your form to the application?");
			return;
		}
		repo.save(step);
	}

	@Override
	public List<Step> getAll() {
		return repo.findAll();
	}


	@Override
	public Step getById(Integer id) {
		Optional<Step> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Step();
	}

	@Override
	public void delete(Step step) {
		repo.delete(step);
	}

}
