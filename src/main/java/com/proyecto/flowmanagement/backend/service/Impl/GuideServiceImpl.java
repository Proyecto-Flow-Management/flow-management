package com.proyecto.flowmanagement.backend.service.Impl;
import java.util.List;
import java.util.Optional;

import com.proyecto.flowmanagement.backend.persistence.repository.IGuideRepo;
import com.proyecto.flowmanagement.backend.service.IGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;

@Service
public class GuideServiceImpl implements IGuideService {

	@Autowired
	private IGuideRepo repo;
	
	@Override
	public Guide add(Guide guide) {
		return repo.save(guide);
	}

	@Override
	public Guide update(Guide guide) {
		return repo.save(guide);
	}

	@Override
	public List<Guide> getAll() {
		return repo.findAll();
	}

	@Override
	public Guide getById(Integer id) {
		Optional<Guide> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Guide();
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}

}
