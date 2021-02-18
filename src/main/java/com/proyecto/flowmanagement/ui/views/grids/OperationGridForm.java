package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
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
import java.util.stream.Collectors;

@CssImport("./styles/general.css")
public class OperationGridForm extends HorizontalLayout {
    public Button createOperation;
    public OperationForm operationForm;
    Operation editing;
    public List<String> alternatives = new LinkedList<String>();
    public List<String> operations = new LinkedList<String>();

    Grid<Operation> operationGrid;
    public List<Operation> operationList;


    VerticalLayout operationSecctionLayout = new VerticalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
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

        editing = null;
        operationForm = new OperationForm();
        operationForm.setVisible(false);
        operationForm.save.addClickListener(buttonClickEvent -> CreateOperation());
        operationForm.close.addClickListener(buttonClickEvent -> CloseForm());
        operationForm.delete.addClickListener(buttonClickEvent -> deleteOperation());

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

    public void deleteOperation()
    {
        this.operationList.remove(editing);
        CloseForm();
        updateGrid();
    }

    private void CloseForm() {
        this.operationForm.setVisible(false);
    }

    private void CreateOperation() {
        this.operationGrid.deselectAll();

        if (operationForm.isValid) {
            Operation newOperation = operationForm.getOperation();
            if (!operationForm.editing) {
                operationList.add(newOperation);
            }
            else {
                int index = operationList.indexOf(editing);
                this.operationList.set(index, newOperation);
                operationForm.editing = false;
            }
            updateGrid();
            CloseForm();
        }
    }

    private void addOperation() {
        operationForm.editing = false;
        operationGrid.asSingleSelect().clear();
        operationForm.setVisible(true);
        operationForm.setAsDefault();
        operationForm.delete.setVisible(false);
        if(this.operations != null && this.operations.size()>0)
        {
            operationForm.operationNotifyIdsForm.updateElements(this.operations);
            this.operationForm.operationNotifyIdsFormAccordion.setVisible(true);
        }
        else
        {
            operationForm.operationNotifyIdsForm.updateElements(this.operations);
            this.operationForm.operationNotifyIdsFormAccordion.setVisible(false);
        }
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
            operationForm.setOperation(operation);
            List<String> operationsIntoOperaation;

            if(operation.getOperationNotifyIds() != null && operation.getOperationNotifyIds().size() >0)
                operationsIntoOperaation = operation.getOperationNotifyIds().stream().map(o -> o.getName()).collect(Collectors.toList());
            else
                operationsIntoOperaation = new LinkedList<>();

            List<String> correctsOperations = operations.stream().filter(o -> operation.getName() != o && !operationsIntoOperaation.contains(o)).collect(Collectors.toList());
            operationForm.operationNotifyIdsForm.updateElements(correctsOperations);
            operationForm.editing = true;
            operationForm.setVisible(true);
            editing = operation;
            operationForm.delete.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        operationForm.setVisible(false);
        operationForm.setAsDefault();
        removeClassName("editing" );
    }

    public List<Operation> getOperations() {
        return this.operationList;
    }

    public void loadOperations(List<Operation> operations)
    {
        this.operationList = operations;
        if(operationList == null)
            operationList = new LinkedList<>();
        updateGrid();
        this.accordion.close();
    }

    public void setAsDefault() {
        this.operationList = new LinkedList<>();
        updateGrid();
    }

    public void updateAlternativesIds(List<String> ids)
    {
        this.operationForm.alternativesIdsAccordion.setVisible(true);
        this.operationForm.alternatives = ids;
        this.operationForm.alternativeIdsForm.updateElements(ids);;
    }



    public void updateOperationsIds(List<String> ids)
    {
        this.operationForm.operations = ids;
        this.operationForm.operationNotifyIdsForm.updateElements(ids);;
    }
}
