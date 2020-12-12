package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;
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

    Grid<OperationParameter> operationParameterGrid = new Grid<>(OperationParameter.class);;
    List<OperationParameter> operationParameterList;

    public OperationParameterGridForm(String buttonLabel)
    {
        this.operationParameterList = new LinkedList<>();
        setSizeFull();
        configureElements(buttonLabel);
    }

    private void configureElements(String buttonLabel) {

        configureGrid();
        createOperationParameter = new Button(buttonLabel, click -> addOperationParameter());

        operationParameterForm = new OperationParameterForm();
        operationParameterForm.setVisible(false);
        operationParameterForm.save.addClickListener(buttonClickEvent -> CreateOperationParameter());
        operationParameterForm.close.addClickListener(buttonClickEvent -> CloseForm());


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

    private void CloseForm() {
        this.operationParameterForm.setVisible(false);
    }

    private void CreateOperationParameter() {
        OperationParameter newOperationParameter = operationParameterForm.getOperationParameter();
        operationParameterList.add(newOperationParameter);
        updateGrid();
        operationParameterForm.setVisible(false);
    }

    private void addOperationParameter() {
        operationParameterGrid.asSingleSelect().clear();
        editOperationParameter(new OperationParameter());
    }

        private void configureGrid() {
//        operationParameterGrid = new Grid<>(OperationParameter.class);
        operationParameterGrid.addClassName("user-grid");
        operationParameterGrid.setColumns("name", "label");
//        operationParameterGrid.setSizeFull();
        operationParameterGrid.setWidth("80%");
        operationParameterGrid.asSingleSelect().addValueChangeListener(evt -> editOperationParameter(evt.getValue()));
    }

    private void editOperationParameter(OperationParameter operationParameter) {
        if(operationParameter == null) {
            closeEditor();
        } else {
            operationParameterForm.setOperationParameter(operationParameter);
            operationParameterForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        operationParameterForm.setOperationParameter(null);
        operationParameterForm.setVisible(false);
        removeClassName("editing");
    }

    public List<OperationParameter> getOperationsParameter() {
        return this.operationParameterList;
    }
}
