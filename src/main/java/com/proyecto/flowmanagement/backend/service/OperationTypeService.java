package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation_Type;
import com.proyecto.flowmanagement.backend.persistence.repository.OperationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OperationTypeService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    public List<Operation_Type> findAll() {
        return operationTypeRepository.findAll();
    }
//
//    public Operation_Type find(String filterText) {
//        return operationTypeRepository.search(filterText).get(0);
//    }
//
//    public List<Operation_Type> findAll(String filterText) {
//        return operationTypeRepository.search(filterText);
//    }

    public long count() {
        return operationTypeRepository.count();
    }

    public void delete(Operation_Type operation_type) {
        operationTypeRepository.delete(operation_type);
    }

    public void save(Operation_Type operation_type) {
        if (operation_type == null) {
            LOGGER.log(Level.SEVERE,
                    "User is null. Are you sure you have connected your form to the application?");
            return;
        }
        operationTypeRepository.save(operation_type);
    }
}
