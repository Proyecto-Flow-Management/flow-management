package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "operation_type")
public class Operation_Type extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}