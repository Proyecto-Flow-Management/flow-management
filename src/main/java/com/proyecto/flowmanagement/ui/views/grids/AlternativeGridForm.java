package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.forms.AlternativeForm;
import com.proyecto.flowmanagement.ui.views.forms.StepForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
@CssImport("./styles/step-panel.css")
public class AlternativeGridForm extends HorizontalLayout {

    public AlternativeForm alternativeForm;
    public Button createAlternative;
    Alternative editAlternative;
    Grid<Alternative> alternativeGrid;
    List<Alternative> alternativeList;

    List<Step> stepList;

    VerticalLayout stepSecctionLayout = new VerticalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();

    public AlternativeGridForm()
    {
        stepList = new LinkedList<>();
        setSizeFull();
        configureElements();
    }

    private void configureElements()
    {
        this.alternativeList = new LinkedList<>();
        configureGrid();
        createAlternative = new Button("Crear Alternative", click -> addAlternative());

        alternativeForm = new AlternativeForm(stepList);
        alternativeForm.setVisible(false);
        alternativeForm.save.addClickListener(buttonClickEvent -> CreateorSaveAlternative());
        alternativeForm.close.addClickListener(buttonClickEvent -> CloseForm());
        alternativeForm.delete.addClickListener(buttonClickEvent -> deleteAlternative());

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

        basicInformation.setWidthFull();
        stepSecctionLayout.setWidthFull();
        stepSecctionLayout.setId("step-Layout");
        stepSecctionLayout.add(createAlternativeLyout, alternativeFormLayout, gridLayout);
        setWidthFull();
        basicInformation.add(stepSecctionLayout);
        accordion.add("Alternatives", basicInformation);
        accordion.close();
        add(accordion);
    }

    private void deleteAlternative() {
        this.alternativeList.remove(editAlternative);
        CloseForm();
        updateGrid();
    }

    private void CloseForm() {
        this.alternativeForm.setVisible(false);
    }

    private void CreateorSaveAlternative() {
        if(alternativeForm.isValid)
        {
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

            CloseForm();
        }
    }

    private void updateGrid() {
        alternativeGrid.setItems(alternativeList);
    }

    private void addAlternative() {
        alternativeGrid.asSingleSelect().clear();
        this.editAlternative = null;
        alternativeForm.setAsDefault();
        alternativeForm.setVisible(true);

    }

    private void configureGrid() {
        alternativeGrid = new Grid<>(Alternative.class);
        alternativeGrid.addClassName("user-grid");
        alternativeGrid.setSizeFull();
        alternativeGrid.setColumns("label");

        alternativeGrid.addColumn(alternative ->
                {
                    boolean step = alternative.getGuideName() == null || alternative.getGuideName().isEmpty();
                    return step ? "" : alternative.getGuideName();}).setHeader("Next Guide").setSortable(true);

        alternativeGrid.addColumn(alternative ->
                { return alternative.getGuideName() != null && !alternative.getGuideName().isEmpty()? "" : alternative.getNextStep();}).setHeader("Next Step").setSortable(true);

        alternativeGrid.asSingleSelect().addValueChangeListener(evt -> editAlternative(evt.getValue()));
    }

    private void editAlternative(Alternative alternative) {

        if(alternative != null) {
            alternativeForm.setVisible(true);
            alternativeForm.conditionForm.agregarLayout.setVisible(false);
            alternativeForm.isValid = false;

            this.editAlternative = alternative;
            alternativeForm.setAlternative(alternative);
            addClassName("editing");
        }
        else if(alternativeForm.editing)
        {
            alternativeForm.setVisible(false);
        }
    }

    private void closeEditor() {
        alternativeForm.setAlternative(null);
        alternativeForm.setVisible(false);
        removeClassName("editing");
    }

    public List<Alternative> getAlternatives() {
        return this.alternativeList;
    }

    public void setAsDefault() {
        this.alternativeList = new LinkedList<>();
        updateGrid();
    }

    public void loadAlternative(List<Alternative> alternatives)
    {
        this.alternativeList = alternatives;
        if(alternativeList == null)
            alternativeList = new LinkedList<>();
        updateGrid();
        this.accordion.close();
    }
}