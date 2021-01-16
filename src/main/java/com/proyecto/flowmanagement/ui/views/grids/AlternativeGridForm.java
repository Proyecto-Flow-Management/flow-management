package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.forms.AlternativeForm;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class AlternativeGridForm extends VerticalLayout {

    public AlternativeForm alternativeForm;
    public Button createAlternative;
    Alternative editAlternative;
    Grid<Alternative> alternativeGrid;
    List<Alternative> alternativeList;
    List<String> stepIdList;

    public AlternativeGridForm()
    {
        setSizeFull();
        configureElements();
    }

    private void configureElements()
    {
        this.alternativeList = new LinkedList<>();
        configureGrid();
        createAlternative = new Button("Crear Alternative", click -> addAlternative());

        alternativeForm = new AlternativeForm();
        alternativeForm.setVisible(false);
        alternativeForm.save.addClickListener(buttonClickEvent -> CreateorSaveAlternative());
        alternativeForm.close.addClickListener(buttonClickEvent -> CloseForm());

        setWidthFull();
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(alternativeGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout alternativeFormLayout = new HorizontalLayout();
        alternativeFormLayout.add(alternativeForm);
        alternativeFormLayout.setWidthFull();

        HorizontalLayout createAlternativeLyout = new HorizontalLayout();
        createAlternativeLyout.add(createAlternative);
        createAlternativeLyout.setWidthFull();

        add(createAlternativeLyout, alternativeFormLayout, gridLayout);
    }

    private void CloseForm() {
        this.alternativeForm.setVisible(false);
    }

    private void CreateorSaveAlternative() {
        if (alternativeForm.isValid()) {
            Alternative newAlternative = alternativeForm.getAlternative();

            if (alternativeForm.editing) {
                int index = alternativeList.indexOf(editAlternative);
                alternativeList.set(index, newAlternative);
                updateGrid();
            } else {
                alternativeList.add(newAlternative);
                updateGrid();
                closeEditor();
            }
        }
    }

    private void updateGrid() {
        if (alternativeList != null) {
            alternativeGrid.setItems(alternativeList);
        }
        else{
            alternativeGrid.setItems(new LinkedList<>());
        }
    }

    public void updateAlternatives(List alternativeList){
        this.alternativeList = alternativeList;
        updateGrid();
    }

    public void setStepIdList (List stepIdList){
        this.stepIdList = stepIdList;
    }

    private void addAlternative() {
        alternativeGrid.asSingleSelect().clear();
        editAlternative(null);
    }

    private void configureGrid() {
        alternativeGrid = new Grid<>(Alternative.class);
        alternativeGrid.addClassName("user-grid");
        alternativeGrid.setSizeFull();
        alternativeGrid.setColumns("label","nextStep","guideName");
        alternativeGrid.asSingleSelect().addValueChangeListener(evt -> editAlternative(evt.getValue()));
    }

    private void editAlternative(Alternative alternative) {
        alternativeForm.setVisible(true);
        alternativeForm.setStepIdCombo(stepIdList);

        if(alternative != null) {
            this.editAlternative = alternative;
            alternativeForm.setAlternative(alternative);
            addClassName("editing");
        }
        else
        {
            alternativeForm.setAlternative(null);
        }
    }

    public void setAsDefault() {
        this.alternativeList = new LinkedList<Alternative>();
        this.alternativeGrid.setItems(alternativeList);
    }

    private void closeEditor() {
        alternativeForm.setAlternative(null);
        alternativeForm.setVisible(false);
        removeClassName("editing");
    }

    public List<Alternative> getAlternatives() {
        return this.alternativeList;
    }
}