package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.ui.views.forms.OperationForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class OperationGridForm extends VerticalLayout {
    private Button createOperation;
    public OperationForm operationForm = new OperationForm();

    public List<String> alternatives = new LinkedList<String>();

    Grid<Operation> operationGrid;
    List<Operation> operationList;
    Operation editOperation;

    public OperationGridForm()
    {
        this.operationList = new LinkedList<>();
        setSizeFull();
        configureElements();
    }

    private void configureElements() {

        configureGrid();
        createOperation = new Button("Crear Operation", click -> addOperation());

        //operationForm = new OperationForm(alternatives);
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

        add(createOperationLayout, operationFormLayout, gridLayout);
    }

    private void updateGrid() {
        operationGrid.setItems(operationList);
    }

    private void CloseForm() {
        this.operationForm.setVisible(false);
    }

    /*private void CreateorSaveOperation() {
        if (operationForm.isValid()) {
            Operation newOperation = operationForm.getOperation();

            if (operationForm.editing) {
                int index = operationList.indexOf(editOperation);
                operationList.set(index, newOperation);
                updateGrid();
            } else {
                operationList.add(newOperation);
                updateGrid();
                closeEditor();
            }
        }
    }*/

    private void CreateOperation() {
        if (operationForm.isValid) {
            Operation newOperation = operationForm.getOperation();
            operationList.add(newOperation);
            updateGrid();
            CloseForm();
        }
    }

    /*private void CreateOperation() {
        if (operationForm.isValid) {
            Operation newOperation = operationForm.getOperation();

            if(!operationForm.editing){
                operationList.add(newOperation);
            }
            else {
                //OJO!
                int index = operationList.indexOf(operationForm.editing);
                this.operationList.set(index, newOperation);
                operationForm.editing = false;
            }
            updateGrid();
            operationForm.setVisible(false);
        }
    }*/

    private void addOperation() {
        operationGrid.asSingleSelect().clear();
        operationForm.setVisible(true);
        operationForm.setAsDefault();
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
            this.operationForm.alternatives = this.alternatives;
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
}
