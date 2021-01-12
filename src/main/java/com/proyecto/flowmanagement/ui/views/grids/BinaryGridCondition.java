package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.BinaryCondition;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.forms.BinaryConditionForm;
import com.proyecto.flowmanagement.ui.views.forms.UnaryConditionForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

public class BinaryGridCondition  extends VerticalLayout {

    private BinaryConditionForm binaryForm;
    public Button createUnaryCondition;
    BinaryCondition editBinary;

    Grid<BinaryCondition> binaryConditionGrid;
    List<BinaryCondition> binaryConditionList;

    public BinaryGridCondition()
    {
        setSizeFull();
        configureElements();
    }

    public BinaryGridCondition(Alternative alternative)
    {
        setSizeFull();
        configureElements();
    }

    public void configureElements()
    {
        this.binaryConditionList = new LinkedList<>();
        configureGrid();
        createUnaryCondition = new Button("Crear Binary", click -> addUnary());

        binaryForm = new BinaryConditionForm();
        binaryForm.setVisible(false);
        binaryForm.save.addClickListener(buttonClickEvent -> CreateorSaveUnary());
        binaryForm.delete.addClickListener(buttonClickEvent -> eliminarBinary());
        binaryForm.close.addClickListener(buttonClickEvent -> CloseForm());

        setWidthFull();
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(binaryConditionGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout unaryFormLayout = new HorizontalLayout();
        unaryFormLayout.add(binaryForm);
        unaryFormLayout.setWidthFull();

        HorizontalLayout createUnaryLyout = new HorizontalLayout();
        createUnaryLyout.add(createUnaryCondition);
        createUnaryLyout.setWidthFull();

        add(createUnaryCondition, unaryFormLayout, gridLayout);
    }

    private void eliminarBinary() {
        binaryConditionList.remove(editBinary);
        updateGrid();
        closeEditor();
    }

    private void CloseForm()  {
        this.binaryForm.setVisible(false);
    }

    private void CreateorSaveUnary() {
        if (binaryForm.isValid()) {
            BinaryCondition newBinary = binaryForm.getBinaryCondition();

            if (binaryForm.editing) {
                int index = binaryConditionList.indexOf(editBinary);
                binaryConditionList.set(index, newBinary);
                CloseForm();
                updateGrid();
            } else {
                binaryConditionList.add(newBinary);
                updateGrid();
                CloseForm();
            }
        }
    }

    private void closeEditor() {
        binaryForm.setBinaryCondition(null);
        binaryForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateGrid() {
        binaryConditionGrid.setItems(binaryConditionList);
    }

    private void addUnary() {
        binaryConditionGrid.asSingleSelect().clear();
        editUnary(null);
    }

    private void configureGrid() {
        binaryConditionGrid = new Grid<>(BinaryCondition.class);
        binaryConditionGrid.addClassName("user-grid");
        binaryConditionGrid.setSizeFull();
        binaryConditionGrid.setColumns("operator");
        binaryConditionGrid.asSingleSelect().addValueChangeListener(evt -> editUnary(evt.getValue()));
    }

    private void editUnary(BinaryCondition biary) {
        binaryForm.setVisible(true);

        if(biary != null) {
            this.editBinary = biary;
            binaryForm.setBinaryCondition(biary);
            addClassName("editing");
        }
        else
        {
            binaryForm.setBinaryCondition(null);
        }
    }

    public List<BinaryCondition> getBinaryConditions() {
        return this.binaryConditionList;
    }

    public void setAsDefault() {
        this.binaryConditionList = new LinkedList<BinaryCondition>();
        this.binaryConditionGrid.setItems(binaryConditionList);
    }

    public void setBinaryCondition(List<BinaryCondition> conditions) {
        this.binaryConditionList = conditions;
        this.binaryConditionGrid.setItems(binaryConditionList);
    }
}
