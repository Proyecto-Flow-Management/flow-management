package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.backend.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RolService {
    private RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Map<String, Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        findAll().forEach(rol ->
                stats.put(rol.getName(), rol.getEmployees().size()));
        return stats;
    }
}
