package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.ui.views.forms.ConditionParametersForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

public class ParametersGridForm extends VerticalLayout {

    public ConditionParametersForm conditionParametersForm = new ConditionParametersForm();
    public Button createConditionParameter;
    ConditionParameter editing;
    Grid<ConditionParameter> conditionParameterGrid;
    List<ConditionParameter> conditionParameterList;

    public List<ConditionParameter> getConditionParameterList()
    {
        return this.conditionParameterList;
    }

    public ParametersGridForm()
    {
        try
        {
            setSizeFull();

            configureElements();
        }
        catch (Exception ex)
        {
            String esto = ex.getStackTrace().toString();
        }
    }

    private void configureElements() {
        this.conditionParameterList = new LinkedList<>();

        configureGrid();

        createConditionParameter = new Button("Crear Condicion Habilitacion", click -> addParameter());

        conditionParametersForm.setVisible(false);
        conditionParametersForm.save.addClickListener(buttonClickEvent -> save());
        conditionParametersForm.close.addClickListener(buttonClickEvent -> CloseForm());
        conditionParametersForm.delete.addClickListener(buttonClickEvent -> delete());

        setWidthFull();
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(conditionParameterGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout parameterFormLayout = new HorizontalLayout();
        parameterFormLayout.add(conditionParametersForm);
        parameterFormLayout.setWidthFull();

        HorizontalLayout createParameterLyout = new HorizontalLayout();
        createParameterLyout.add(createConditionParameter);
        createParameterLyout.setWidthFull();

        add(createParameterLyout, parameterFormLayout, gridLayout);
    }

    private void delete() {
        ConditionParameter actual = conditionParametersForm.getConditionParameter();
        conditionParameterList.remove(actual);
        updateGrid();
        CloseForm();
    }

    private void CloseForm() {
        this.conditionParametersForm.setVisible(false);
    }

    private void save() {
        if(!conditionParametersForm.isValid)
            return;

        ConditionParameter actual = conditionParametersForm.getConditionParameter();

        if(editing == null)
        {
            conditionParameterList.add(actual);
        }
        else
        {
            int index = conditionParameterList.indexOf(editing);
            conditionParameterList.set(index, actual);
        }

        updateGrid();
        CloseForm();
    }

    public void updateGrid() {
        try{
            if(conditionParameterGrid == null)
            {
                String estos = "1a";
            }
            if(conditionParameterList == null)
            {
                String estos1 = "1a";
            }
            this.conditionParameterGrid.setItems(new LinkedList<>());
            this.conditionParameterGrid.setItems(conditionParameterList);
        }
        catch(Exception ex){
            System.out.println(ex.getStackTrace().toString());
            String esto = ex.getMessage();
        }
    }

    private void addParameter()
    {
        conditionParameterGrid.asSingleSelect().clear();
        conditionParametersForm.setAsDefault();
        conditionParametersForm.setVisible(true);
        conditionParametersForm.delete.setVisible(false);
    }

    private void configureGrid()
    {
        conditionParameterGrid = new Grid<>(ConditionParameter.class);
        conditionParameterGrid.addClassName("user-grid");
        conditionParameterGrid.setSizeFull();
        conditionParameterGrid.setColumns("field","fieldType","operator","value");
        conditionParameterGrid.asSingleSelect().addValueChangeListener(evt -> editParameter(evt.getValue()));
    }

    private void editParameter(ConditionParameter value)
    {
        try{
            conditionParametersForm.delete.setVisible(true);
            this.editing = value;
            this.conditionParametersForm.setAsDefault();
            this.conditionParametersForm.setForm(value);
            this.conditionParametersForm.setVisible(true);

        }
        catch (Exception ex)
        {
            String esto = ex.getMessage();
        }
    }

    public void setasDefault() {
        this.conditionParameterList = new LinkedList<>();
        updateGrid();
    }

    public void setConditionParameterList(List<ConditionParameter> conditionParameter) {
        this.conditionParameterList = conditionParameter;
    }
}

