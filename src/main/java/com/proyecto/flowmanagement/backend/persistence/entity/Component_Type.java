package com.proyecto.flowmanagement.backend.persistence.entity;


import javax.persistence.*;

@Entity
@Table(name = "component_type")
public class Component_Type extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;

    @ManyToOne
    @JoinColumn(name = "parameter_id")
    private Component_Parameter parameter;

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

    public Component_Parameter getComponentParameter() {
        return parameter;
    }

    public void setLabel(Component_Parameter parameter) {
        this.parameter = parameter;
    }
}
