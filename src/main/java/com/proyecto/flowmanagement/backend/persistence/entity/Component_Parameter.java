package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "component_parameter")
public class Component_Parameter extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;

    @Column(name = "value")
    private String value;

    public Component_Parameter(String name, String label, String value) {
        this.name = name;
        this.label = label;
        this.value = value;
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
