package com.proyecto.flowmanagement.backend.commun;

import com.proyecto.flowmanagement.backend.persistence.entity.Condition;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;

public class CustomTree{
    TreeData<ConditionDTO> test;

    public CustomTree()
    {
        TreeGrid<ConditionDTO> foo = new TreeGrid<ConditionDTO>(ConditionDTO.class);
    }
}
