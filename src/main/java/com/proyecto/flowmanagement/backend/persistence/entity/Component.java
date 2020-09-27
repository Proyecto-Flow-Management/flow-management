package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "component")
public class Component extends AbstractEntity {

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

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Component_Type type;

    public Component_Type getComponentType() {
        return type;
    }

    public void setLabel(Component_Type type) {
        this.type = type;
    }
}
