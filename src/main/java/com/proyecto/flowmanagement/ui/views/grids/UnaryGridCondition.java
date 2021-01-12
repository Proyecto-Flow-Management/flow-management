package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.forms.AlternativeForm;
import com.proyecto.flowmanagement.ui.views.forms.UnaryConditionForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

public class UnaryGridCondition  extends VerticalLayout  {

    private UnaryConditionForm unaryForm;
    public Button createUnaryCondition;
    UnaryCondition editUnary;

    Grid<UnaryCondition> unaryConditionGrid;
    List<UnaryCondition> unaryConditionList;

    public UnaryGridCondition()
    {
        setSizeFull();
        configureElements();
    }

    public UnaryGridCondition(Alternative alternative)
    {
        setSizeFull();
        configureElements();
    }

    public void configureElements()
    {
        this.unaryConditionList = new LinkedList<>();
        configureGrid();
        createUnaryCondition = new Button("Crear Unary", click -> addUnary());

        unaryForm = new UnaryConditionForm();
        unaryForm.setVisible(false);
        unaryForm.save.addClickListener(buttonClickEvent -> CreateorSaveUnary());
        unaryForm.close.addClickListener(buttonClickEvent -> CloseForm());
        unaryForm.delete.addClickListener(buttonClickEvent -> eliminarUnary());

        setWidthFull();
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(unaryConditionGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout unaryFormLayout = new HorizontalLayout();
        unaryFormLayout.add(unaryForm);
        unaryFormLayout.setWidthFull();

        HorizontalLayout createUnaryLyout = new HorizontalLayout();
        createUnaryLyout.add(createUnaryCondition);
        createUnaryLyout.setWidthFull();

        add(createUnaryCondition, unaryFormLayout, gridLayout);
    }

    private void eliminarUnary() {
        unaryConditionList.remove(editUnary);
        updateGrid();
        closeEditor();
    }

    private void CloseForm()  {
        this.unaryForm.setVisible(false);
    }

    private void CreateorSaveUnary() {

        UnaryCondition newUnary = unaryForm.getUnaryCondition();

        if(unaryForm.editing)
        {
            int index = unaryConditionList.indexOf(editUnary);
            unaryConditionList.set(index, newUnary);
            updateGrid();
            CloseForm();
        }
        else
        {
            unaryConditionList.add(newUnary);
            updateGrid();
            CloseForm();
        }
    }

    private void closeEditor() {
        unaryForm.setUnaryCondition(null);
        unaryForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateGrid() {
        unaryConditionGrid.setItems(unaryConditionList);
    }

    private void addUnary() {
        unaryConditionGrid.asSingleSelect().clear();
        editUnary(null);
    }

    private void configureGrid() {
        unaryConditionGrid = new Grid<>(UnaryCondition.class);
        unaryConditionGrid.addClassName("user-grid");
        unaryConditionGrid.setSizeFull();
        unaryConditionGrid.setColumns("operationName");
        unaryConditionGrid.asSingleSelect().addValueChangeListener(evt -> editUnary(evt.getValue()));
    }

    private void editUnary(UnaryCondition unary) {
        unaryForm.setVisible(true);

        if(unary != null) {
            this.editUnary = unary;
            unaryForm.setUnaryCondition(unary);
            addClassName("editing");
        }
        else
        {
            unaryForm.setUnaryCondition(null);
        }
    }

    public List<UnaryCondition> getUnaryConditionList(){
        return this.unaryConditionList;
    }

    public void setAsDefault() {
        this.unaryConditionList = new LinkedList<UnaryCondition>();
        this.unaryConditionGrid.setItems(unaryConditionList);
    }

    public void setUnaryCondition(List<UnaryCondition> conditions) {
        this.unaryForm.setVisible(false);
        this.unaryConditionList = conditions;
        this.unaryConditionGrid.setItems(unaryConditionList);
    }
}
