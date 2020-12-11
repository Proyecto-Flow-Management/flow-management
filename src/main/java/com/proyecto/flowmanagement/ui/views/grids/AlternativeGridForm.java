package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.ui.views.forms.AlternativeForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class AlternativeGridForm extends VerticalLayout {

    private AlternativeForm alternativeForm;
    public Button createAlternative;

    Grid<Alternative> alternativeGrid = new Grid<>(Alternative.class);
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
        alternativeForm.close.addClickListener(buttonClickEvent -> CloseForm());

        setWidthFull();
        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(alternativeGrid);
        gridLayout.setWidthFull();

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
        alternativeGrid.setColumns("guideName","label","nextStep");
        alternativeGrid.setWidth("80%");
        alternativeGrid.asSingleSelect().addValueChangeListener(evt -> editAlternative(evt.getValue()));
    }

    private void editAlternative(Alternative alternative) {
        if(alternative == null) {
            closeEditor();
        } else {
            alternativeForm.setAlternative(alternative);
            alternativeForm.setVisible(true);
            addClassName("editing");
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
}