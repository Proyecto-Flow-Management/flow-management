package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.OperationForm;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

public class OperationGridForm extends VerticalLayout {
    private Button createOperation;
    public OperationForm operationForm;

    Grid<Operation> operationGrid;
    List<Operation> operationList;

    public OperationGridForm()
    {
        this.operationList = new LinkedList<>();
        setSizeFull();
        configureElements();
    }

    private void configureElements() {

        configureGrid();
        createOperation = new Button("Crear Operation", click -> addOperation());

        operationForm = new OperationForm();
        operationForm.setVisible(false);
        operationForm.save.addClickListener(buttonClickEvent -> CreateOperation());
        operationForm.close.addClickListener(buttonClickEvent -> CloseForm());

        operationGrid = new Grid<>(Operation.class);
        operationGrid.setWidth("80%");

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(operationGrid);
        gridLayout.setWidthFull();

        HorizontalLayout operationFormLayout = new HorizontalLayout();
        operationFormLayout.add(operationForm);
        operationFormLayout.setWidthFull();

        HorizontalLayout createOperationLayout = new HorizontalLayout();
        createOperationLayout.add(createOperation);
        createOperationLayout.setWidthFull();


        add(createOperationLayout, operationFormLayout, gridLayout);
    }

    private void CloseForm() {
    }

    private void CreateOperation() {
    }

    private void addOperation() {
    }

    private void configureGrid() {
    }

    public List<Operation> getOperations() {
        return this.operationList;
    }
}
