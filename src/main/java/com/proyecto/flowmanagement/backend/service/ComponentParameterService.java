package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.persistence.entity.Component_Parameter;
import com.proyecto.flowmanagement.backend.persistence.repository.ComponentParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ComponentParameterService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private ComponentParameterRepository componentParameterRepository;

    public List<Component_Parameter> findAll() {
        return componentParameterRepository.findAll();
    }
//
//    public Component_Parameter find(String filterText) {
//        return ComponentParameterRepository.search(filterText).get(0);
//    }
//
//    public List<Component_Parameter> findAll(String filterText) {
//        return ComponentParameterRepository.search(filterText);
//    }

    public long count() {
        return componentParameterRepository.count();
    }

    public void delete(Component_Parameter Component_Parameter) {
        componentParameterRepository.delete(Component_Parameter);
    }

    public void save(Component_Parameter Component_Parameter) {
        if (Component_Parameter == null) {
            LOGGER.log(Level.SEVERE,
                    "User is null. Are you sure you have connected your form to the application?");
            return;
        }
        componentParameterRepository.save(Component_Parameter);
    }
}
