package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import sun.invoke.util.VerifyType;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AlternativeIdsForm extends HorizontalLayout {

    List<String> alternativesId;
    List<String> alternativesInGrid;

    ComboBox<String> comboAlternatives = new ComboBox<>("Alternatives Ids");
    Grid<String> gridAlternatives;
    Button addAlternative;
    Button deleteAlternative;
    String editing;
    VerticalLayout form = new VerticalLayout();

    public AlternativeIdsForm(List<String> alternatives)
    {
        this.alternativesId = alternatives;
        this.alternativesInGrid = new LinkedList<>();
        setSizeFull();

        configureElements();

        configureGrids();
    }

    private void configureElements() {

        comboAlternatives.setItems();

        addAlternative = new Button("+");
        addAlternative.addClickListener(buttonClickEvent -> addAlternativeToGrid());

        HorizontalLayout principal = new HorizontalLayout();
        principal.add(comboAlternatives, addAlternative);

        HorizontalLayout gridAlternativesLayout = new HorizontalLayout();
        gridAlternatives = new Grid<String>();
        gridAlternatives.setItems(alternativesId);
        gridAlternativesLayout.add(gridAlternatives);

        HorizontalLayout eliminarLayout = new HorizontalLayout();
        deleteAlternative = new Button("Eliminar");
        deleteAlternative.setVisible(false);
        eliminarLayout.add(deleteAlternative);

        form.add(principal,gridAlternativesLayout,eliminarLayout);

        add(form);
    }

    private void addAlternativeToGrid() {
        String nuevo = comboAlternatives.getValue();
        alternativesId.remove(nuevo);
        alternativesInGrid.add(nuevo);

        gridAlternatives.setItems(alternativesInGrid);
        comboAlternatives.setItems(alternativesId);

        updateElements();
    }

    private void configureGrids() {
        gridAlternatives.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        deleteAlternative.addClickListener(buttonClickEvent -> deleteEvent());
    }

    private void deleteEvent() {
        alternativesId.add(editing);
        alternativesInGrid.remove(editing);
        editing = "";
        gridAlternatives.setItems(alternativesInGrid);
        updateElements();
    }

    private void edit(String value) {
        this.editing = value;
        this.deleteAlternative.setVisible(true);
    }

    public void updateElements(){
        gridAlternatives.setItems(alternativesInGrid);
        comboAlternatives.setItems(alternativesId);
    }
}
