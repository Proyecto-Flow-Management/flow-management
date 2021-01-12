package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.BinaryCondition;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.grids.AlternativeGridForm;
import com.proyecto.flowmanagement.ui.views.grids.BinaryGridCondition;
import com.proyecto.flowmanagement.ui.views.grids.DocumentsGridForm;
import com.proyecto.flowmanagement.ui.views.grids.UnaryGridCondition;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class ConditionForm extends HorizontalLayout {

    UnaryGridCondition unaryGridCondition;
    BinaryGridCondition binaryGridCondition;

    VerticalLayout form = new VerticalLayout();
    HorizontalLayout unaryGridLayout = new HorizontalLayout();
    HorizontalLayout binaryGridLayout = new HorizontalLayout();

    public ConditionForm()
    {
        setSizeFull();

        configureUnaryCondition();

        configureBinaryCondition();

        configureForm();
    }

    public ConditionForm(Alternative alternative)
    {
        setSizeFull();

        configureUnaryCondition();

        configureBinaryCondition();

        configureForm();
    }

    private void configureBinaryCondition() {
        binaryGridLayout.setWidthFull();
        binaryGridLayout.setWidthFull();
        binaryGridCondition = new BinaryGridCondition();
        binaryGridLayout.add(binaryGridCondition);
    }

    private void configureUnaryCondition() {
        unaryGridLayout.setWidthFull();
        unaryGridLayout.setWidthFull();
        unaryGridCondition = new UnaryGridCondition();
        unaryGridLayout.add(unaryGridCondition);
    }

    private void configureForm() {
        form.add(unaryGridLayout, binaryGridLayout);
        add(form);
    }

    public List<UnaryCondition> getUnaryConditions() {
        return this.unaryGridCondition.getUnaryConditionList();
    }

    public List<BinaryCondition> getBinaryConditions() {
        return this.binaryGridCondition.getBinaryConditions();
    }

    public void setAsDefault() {
        unaryGridCondition.setAsDefault();
        binaryGridCondition.setAsDefault();
    }

    public void setConditions(Alternative alternative) {
        unaryGridCondition.setUnaryCondition(alternative.getConditions());
        binaryGridCondition.setBinaryCondition(alternative.getBinaryConditions());
    }
}
