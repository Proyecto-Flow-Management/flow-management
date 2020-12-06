package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.AlternativeForm;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class AlternativeGridForm extends VerticalLayout {

    private AlternativeForm alternativeForm;
    public Button createAlternative;

    Grid<Alternative> alternativeGrid;
    List<Alternative> alternativeList;

    public AlternativeGridForm()
    {
        setSizeFull();
        configureElements();
    }

    private void configureElements()
    {
        configureGrid();
        createAlternative = new Button("Crear Alternative", click -> addAlternative());

        alternativeForm = new AlternativeForm();
        alternativeForm.setVisible(false);
        alternativeForm.save.addClickListener(buttonClickEvent -> CreateAlternative());
        alternativeGrid = new Grid<>(Alternative.class);
        alternativeGrid.setWidthFull();

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(alternativeGrid);
        gridLayout.setWidthFull();

        HorizontalLayout stepFormLayout = new HorizontalLayout();
        stepFormLayout.add(alternativeForm);
        stepFormLayout.setWidthFull();

        HorizontalLayout createStepLayout = new HorizontalLayout();
        createStepLayout.add(createAlternative);
        createStepLayout.setWidthFull();

        add(createStepLayout, stepFormLayout, gridLayout);
    }

    private void CreateAlternative() {
        Alternative newAlternative = alternativeForm.getAlternative();
        alternativeList.add(newAlternative);
        updateGrid();
        alternativeForm.setVisible(false);
    }

    private void updateGrid() {
        alternativeGrid.setItems(alternativeList);
    }

    private void addAlternative() {
        alternativeGrid.asSingleSelect().clear();
        editAlternative(new Alternative());
    }

    private void configureGrid() {
        alternativeGrid = new Grid<>(Alternative.class);
        alternativeGrid.addClassName("user-grid");
        alternativeGrid.setSizeFull();
        alternativeGrid.asSingleSelect().addValueChangeListener(evt -> editAlternative(evt.getValue()));
    }

    private void editAlternative(Alternative alternative) {
        if(alternative == null) {
            closeEditor();
        } else {
            alternativeForm.setStep(alternative);
            alternativeForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        alternativeForm.setStep(null);
        alternativeForm.setVisible(false);
        removeClassName("editing");
    }
}