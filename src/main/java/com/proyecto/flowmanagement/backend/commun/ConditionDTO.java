package com.proyecto.flowmanagement.backend.commun;

import com.vaadin.flow.data.provider.hierarchy.TreeData;

public class ConditionDTO {
    String name;
    ConditionDTO childA;
    ConditionDTO childB;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConditionDTO getChildA() {
        return childA;
    }

    public void setChildA(ConditionDTO childA) {
        this.childA = childA;
    }

    public ConditionDTO getChildB() {
        return childB;
    }

    public void setChildB(ConditionDTO childB) {
        this.childB = childB;
    }

    public ConditionDTO(String name, ConditionDTO childA, ConditionDTO childB ) {
        this.name = name;
        this.childA = childA;
        this.childB = childB;
    }

    public ConditionDTO(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
