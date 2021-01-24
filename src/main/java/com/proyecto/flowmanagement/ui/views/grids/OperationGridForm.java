package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.ui.views.forms.OperationForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class OperationGridForm extends HorizontalLayout {
    private Button createOperation;
    public OperationForm operationForm;

    public List<String> alternatives;

    Grid<Operation> operationGrid;
    public List<Operation> operationList;


    VerticalLayout operationSecctionLayout = new VerticalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();

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

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(operationGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout operationFormLayout = new HorizontalLayout();
        operationFormLayout.add(operationForm);
        operationFormLayout.setWidthFull();

        HorizontalLayout createOperationLayout = new HorizontalLayout();
        createOperationLayout.add(createOperation);
        createOperationLayout.setWidthFull();

        basicInformation.setWidthFull();
        operationSecctionLayout.setWidthFull();
        operationSecctionLayout.setId("step-Layout");
        operationSecctionLayout.add(createOperationLayout, operationFormLayout, gridLayout);
        setWidthFull();
        basicInformation.add(operationSecctionLayout);
        accordion.add("Operations", basicInformation);
        accordion.close();
        add(accordion);
    }

    public void updateGrid() {
        if(operationList == null)
            operationList = new LinkedList<>();
        operationGrid.setItems(operationList);
    }

    private void CloseForm() {
        this.operationForm.setVisible(false);
    }

    private void CreateOperation() {
        if (operationForm.isValid()) {
            Operation newOperation = operationForm.getOperation();
            operationList.add(newOperation);
            updateGrid();
            CloseForm();
        }
    }

    private void addOperation() {
        operationGrid.asSingleSelect().clear();
        operationForm.setVisible(true);
        operationForm = new OperationForm(alternatives);
        editOperation(new Operation());
    }

    private void configureGrid() {
        operationGrid = new Grid<>(Operation.class);
        operationGrid.addClassName("user-grid");
        operationGrid.setColumns("name","label");
        operationGrid.setSizeFull();
        operationGrid.asSingleSelect().addValueChangeListener(evt -> editOperation(evt.getValue()));
    }

    private void editOperation(Operation operation) {
        if(operation == null) {
            closeEditor();
        } else {
            this.operationForm.alteratives = this.alternatives;
            operationForm.setOperation(operation);
            operationForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        operationForm.setOperation(null);
        operationForm.setVisible(false);
        removeClassName("editing");
    }

    public List<Operation> getOperations() {
        return this.operationList;
    }

    public void setAsDefault() {
        this.operationList = new LinkedList<>();
        updateGrid();
    }
}
