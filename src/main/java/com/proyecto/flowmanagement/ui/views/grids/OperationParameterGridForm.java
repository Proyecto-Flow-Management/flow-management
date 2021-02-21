package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.OperationParameterForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

public class OperationParameterGridForm extends VerticalLayout {

    public Button createOperationParameter;
    public OperationParameterForm operationParameterForm;
    OperationParameter editing;
    boolean isOutParameter;
    Grid<OperationParameter> operationParameterGrid = new Grid<>(OperationParameter.class);;
    List<OperationParameter> operationParameterList;

    public OperationParameterGridForm(String buttonLabel, boolean isOutParameter)
    {
        this.isOutParameter = isOutParameter;
        this.operationParameterList = new LinkedList<>();
        setSizeFull();
        configureElements(buttonLabel);
    }

    private void configureElements(String buttonLabel) {

        configureGrid();
        createOperationParameter = new Button(buttonLabel, click -> addOperationParameter());

        editing = null;

        operationParameterForm = new OperationParameterForm();
        operationParameterForm.setVisible(false);
        operationParameterForm.save.addClickListener(buttonClickEvent -> CreateOperationParameter());
        operationParameterForm.close.addClickListener(buttonClickEvent -> CloseForm());
        operationParameterForm.delete.addClickListener(buttonClickEvent -> deleteOperationParameter());


        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(operationParameterGrid);
        gridLayout.setWidthFull();

        HorizontalLayout operationFormLayout = new HorizontalLayout();
        operationFormLayout.add(operationParameterForm);
        operationFormLayout.setWidthFull();

        HorizontalLayout createOperationLayout = new HorizontalLayout();
        createOperationLayout.add(createOperationParameter);
        createOperationLayout.setWidthFull();


        add(createOperationLayout, operationFormLayout, gridLayout);
    }

    private void updateGrid() {
        operationParameterGrid.setItems(operationParameterList);
    }

    public void deleteOperationParameter()
    {
        this.operationParameterList.remove(editing);
        CloseForm();
        updateGrid();
    }

    private void CloseForm() {
        this.operationParameterForm.setVisible(false);
    }

    private void CreateOperationParameter() {
        this.operationParameterGrid.deselectAll();
        if (operationParameterForm.isValid) {
            OperationParameter newOperationParameter = operationParameterForm.getOperationParameter();
            newOperationParameter.setOutParameter(this.isOutParameter);
            newOperationParameter.setProperty(false);
            if(!operationParameterForm.editing){
                operationParameterList.add(newOperationParameter);
            }
            else{
                int index = operationParameterList.indexOf(editing);
                this.operationParameterList.set(index, newOperationParameter);
                operationParameterForm.editing = false;
            }
            updateGrid();
            operationParameterForm.setVisible(false);
            closeEditor();
        }
    }

    public void setAsDefault() {
        this.operationParameterForm.setAsDefault();
        this.operationParameterList = new LinkedList<>();
        updateGrid();
    }

    public void setOperationsParameters(List<OperationParameter> operationsParameters) {
        this.operationParameterList = operationsParameters;
        updateGrid();
    }

    private void addOperationParameter() {
        operationParameterForm.editing = false;
        operationParameterGrid.asSingleSelect().clear();
        this.operationParameterForm.setAsDefault();
        operationParameterForm.setVisible(true);
        operationParameterForm.delete.setVisible(false);
//        editOperationParameter(new OperationParameter());
    }

        private void configureGrid() {
        operationParameterGrid.addClassName("user-grid");
        operationParameterGrid.setColumns("name", "label");
        operationParameterGrid.setWidth("80%");
        operationParameterGrid.asSingleSelect().addValueChangeListener(evt -> editOperationParameter(evt.getValue()));
    }

    private void editOperationParameter(OperationParameter operationParameter) {
        if(operationParameter == null) {
            closeEditor();
        } else {
            operationParameterForm.setOperationParameter(operationParameter);
            operationParameterForm.editing = true;
            operationParameterForm.setVisible(true);
            editing = operationParameter;
            operationParameterForm.delete.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        operationParameterForm.setVisible(false);
        operationParameterForm.setAsDefault();
        removeClassName("editing");
    }

    public List<OperationParameter> getOperationsParameter() {
        return this.operationParameterList;
    }
}
