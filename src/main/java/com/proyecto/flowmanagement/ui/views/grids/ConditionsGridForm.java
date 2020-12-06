package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConditionsGridForm extends VerticalLayout {

    Button addUnaryCondition = new Button("Agregar Condicion");

    Grid<Step> conditionGrid;

    public ConditionsGridForm()
    {
        configureElements();
    }

    private void configureElements() {
        configureGrid();
        addUnaryCondition = new Button("Crear Condicion", click -> add());

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(conditionGrid);
        gridLayout.setWidthFull();

        HorizontalLayout createStepLayout = new HorizontalLayout();
        createStepLayout.add(addUnaryCondition);
        createStepLayout.setWidthFull();

        add(createStepLayout, gridLayout);
    }

    private void configureGrid() {
        conditionGrid = new Grid<>(Step.class);
        conditionGrid.setWidthFull();
    }
}
