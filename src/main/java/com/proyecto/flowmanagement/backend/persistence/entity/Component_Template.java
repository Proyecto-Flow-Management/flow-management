package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "component_template")
public class Component_Template {

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;

    @OneToMany
    @JoinColumn(name = "component_id")
    private Component component;

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

    public Component getComponentParameter() {
        return component;
    }

    public void setLabel(Component component) {
        this.component = component;
    }

}
