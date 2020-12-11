package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class StepGridForm  extends VerticalLayout {
    private StepForm stepForm;
    private Button createStep;

    Grid<Step> stepGrid = new Grid<>(Step.class);
    List<Step> stepList = new LinkedList<>();

    public StepGridForm()
    {
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

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(stepGrid);
        gridLayout.addClassName("gridHorizontalLayout");
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
//        stepGrid = new Grid<>(Step.class);
        stepGrid.addClassName("user-grid");
//        stepGrid.setSizeFull();
        stepGrid.setColumns("label", "text", "textId");
        stepGrid.setWidth("80%");

        stepGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        stepGrid.asSingleSelect().addValueChangeListener(evt -> editStep(evt.getValue()));

        stepGrid.asSingleSelect();
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
