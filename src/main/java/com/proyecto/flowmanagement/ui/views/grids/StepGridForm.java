package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class StepGridForm  extends VerticalLayout {
    public StepForm stepForm;
    private Button createStep;
    Step editing;
    Grid<Step> stepGrid = new Grid<>(Step.class);
    public List<Step> stepList = new LinkedList<>();

    public StepGridForm()
    {
        setSizeFull();

        configureElements();
    }


    private void configureElements()
    {
        configureGrid();
        createStep = new Button("Crear Step", click -> addStep());
        editing = null;
        stepForm = new StepForm();
        stepForm.setVisible(false);
        stepForm.save.addClickListener(buttonClickEvent -> CreateStep());
        stepForm.close.addClickListener(buttonClickEvent -> CloseForm());
        stepForm.delete.addClickListener(buttonClickEvent -> deleteStep());

        stepForm.alternativeGridForm.createAlternative.addClickListener( buttonClickEvent ->  actualizarListaSteps());

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

    private void actualizarListaSteps()
    {
        this.stepForm.alternativeGridForm.alternativeForm.stepList = this.stepList;
    }

    public void deleteStep()
    {
        this.stepList.remove(editing);
        CloseForm();
        updateGrid();
    }

    private void CloseForm() {
        this.stepForm.setVisible(false);
    }

    private void CreateStep() {
        this.stepGrid.deselectAll();

        if(stepForm.esValido)
        {
            Step newStep = stepForm.getStep();

            if(!stepForm.editing)
            {
                stepList.add(newStep);
            }
            else
            {
                int index = stepList.indexOf(editing);
                this.stepList.set(index, newStep);
                stepForm.editing = false;
            }
            updateGrid();
            stepForm.setVisible(false);
            closeEditor();
        }
    }

    public void updateGrid() {
        if(stepList == null)
            stepList = new LinkedList<>();
        stepGrid.setItems(stepList);
    }

    private void addStep() {
        stepForm.editing = false;
        stepGrid.asSingleSelect().clear();
        stepForm.setAsDefault();
        stepForm.setVisible(true);
        stepForm.delete.setVisible(false);
    }

    private void configureGrid() {
        stepGrid.addClassName("user-grid");
        stepGrid.setColumns("text", "textId","label");
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
            stepForm.editing = true;
            stepForm.setVisible(true);
            editing = step;
            stepForm.delete.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        stepForm.setVisible(false);
        stepForm.setAsDefault();
        removeClassName("editing" );
    }

    public List<Step> getSteps() {
        return this.stepList;
    }
    public Grid<Step> getStepGrid() {
        return this.stepGrid;
    }
    public Button getSaveButton() {
        return this.stepForm.getSaveButton();
    }
}
