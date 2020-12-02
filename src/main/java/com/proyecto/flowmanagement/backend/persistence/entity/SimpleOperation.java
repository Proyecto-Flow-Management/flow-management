package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.def.SimpleOperationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "simple_operation")
public class SimpleOperation extends Operation{

    @Column(name = "type")
    private SimpleOperationType type;

    @Column(name = "service")
    private String service;

    public SimpleOperationType getType() {
        return type;
    }

    public void setType(SimpleOperationType type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
