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
    private Button createStep;
    public StepForm stepForm;
    Step editStep;

    Grid<Step> stepGrid = new Grid<>(Step.class);
    List<Step> stepList = new LinkedList<>();
    List<String> guideNameList;
    List<String> createGuides;

    public StepGridForm()
    {
        setSizeFull();
        configureElements();
    }


    private void configureElements()
    {
        this.stepList = new LinkedList<>();
        this.createGuides = new LinkedList<>();
        configureGrid();
        createStep = new Button("Crear Step", click -> addStep());

        stepForm = new StepForm();
        stepForm.setVisible(false);
        stepForm.save.addClickListener(buttonClickEvent -> CreateorSaveStep());
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

    private void updateGrid() {
        stepGrid.setItems(stepList);
    }

    private void CloseForm() {
        this.stepForm.setVisible(false);
    }

    private void CreateorSaveStep() {
        if (!stepForm.emptyForm()) {
            Step newStep = stepForm.getStep();
            setCreateGuides();

            if (stepForm.editing) {
                int index = stepList.indexOf(editStep);
                stepList.set(index, newStep);
                updateGrid();
            } else {
                stepList.add(newStep);
                updateGrid();
            }
            closeEditor();
        }
    }

    private void addStep() {
        stepGrid.asSingleSelect().clear();
        stepForm.setStepIdList(getStepIdList());
        stepForm.setGuideNameList(this.guideNameList);
        editStep(null);
    }

    private void configureGrid() {
        stepGrid = new Grid<>(Step.class);
        stepGrid.addClassName("user-grid");
//        stepGrid.setSizeFull();
        stepGrid.setColumns("label", "text", "textId");
        stepGrid.setWidth("80%");

        stepGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        stepGrid.asSingleSelect().addValueChangeListener(evt -> editStep(evt.getValue()));

        stepGrid.asSingleSelect();
    }

    private void editStep(Step step) {
        stepForm.setVisible(true);
        stepForm.setStepIdList(getStepIdList());
        stepForm.setGuideNameList(this.guideNameList);

        if(step != null) {
            this.editStep = step;
            stepForm.setStep(step);
            addClassName("editing");
        } else {
            stepForm.setStep(null);
        }
    }

    public void setGuideNameList (List guideNameList){
        this.guideNameList = guideNameList;
    }

    private List<String> getStepIdList(){
        List<String> stepIdList = new LinkedList<>();
        for (Step step:
             stepList) {
            stepIdList.add(step.getTextId());
        }
        return stepIdList;
    }

    public List<String> getCreateGuides(){
        return this.createGuides;
    }

    public void setCreateGuides(){
        this.createGuides = stepForm.getCreateGuides();
    }

    private void closeEditor() {
        stepForm.setStep(null);
        stepForm.setVisible(false);
        removeClassName("editing");
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
