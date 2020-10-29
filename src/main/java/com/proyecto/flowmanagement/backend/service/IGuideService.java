package com.proyecto.flowmanagement.backend.service;

import java.util.List;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;

public interface IGuideService {
	
	Guide add(Guide guide);
	Guide update(Guide guide);
	List<Guide> getAll();
	Guide getById(Integer id);
	void delete(Guide guide);
}
