package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class StepGridForm  extends VerticalLayout {
    private StepForm stepForm;
    private Button createStep;

    Grid<Step> stepGrid;
    List<Step> stepList;

    public StepGridForm()
    {
        this.stepList = new LinkedList<>();
        setSizeFull();
        configureElements();
    }

    private void configureElements()
    {
        configureGrid();
        createStep = new Button("Crear Step", click -> addStep());

        stepForm = new StepForm();
        stepForm.setVisible(false);
        stepForm.save.addClickListener(buttonClickEvent -> CreateStep());
        stepForm.close.addClickListener(buttonClickEvent -> CloseForm());
        stepGrid = new Grid<>(Step.class);
        stepGrid.setWidth("80%");

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(stepGrid);
        gridLayout.setWidthFull();

        HorizontalLayout stepFormLayout = new HorizontalLayout();
        stepFormLayout.add(stepForm);
        stepFormLayout.setWidthFull();

        HorizontalLayout createStepLayout = new HorizontalLayout();
        createStepLayout.add(createStep);
        createStepLayout.setWidthFull();


        add(createStepLayout, stepFormLayout, gridLayout);
    }

    private void CloseForm() {
        this.stepForm.setVisible(false);
    }

    private void CreateStep() {
        Step newStep = stepForm.getStep();
        stepList.add(newStep);
        updateGrid();
        stepForm.setVisible(false);
    }

    private void updateGrid() {
        stepGrid.setItems(stepList);
    }

    private void addStep() {
        stepGrid.asSingleSelect().clear();
        editStep(new Step());
    }

    private void configureGrid() {
        stepGrid = new Grid<>(Step.class);
        stepGrid.addClassName("user-grid");
        stepGrid.setSizeFull();
        stepGrid.removeAllColumns();

        for(Grid.Column column : stepGrid.getColumns())
        {
            stepGrid.removeColumnByKey(column.getKey());
        }

        stepGrid.addColumn("label");
        stepGrid.addColumn("text");
        stepGrid.asSingleSelect().addValueChangeListener(evt -> editStep(evt.getValue()));
    }

    private void editStep(Step step) {
        if(step == null) {
            closeEditor();
        } else {
            stepForm.setStep(step);
            stepForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        stepForm.setStep(null);
        stepForm.setVisible(false);
        removeClassName("editing");
    }

    public List<Step> getSteps() {
        return this.stepList;
    }
}
