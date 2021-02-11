package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/groups-form.css")
public class AlternativeIdsForm extends VerticalLayout {

    List<String> alternativesId;
    List<String> alternativesInGrid;

    ComboBox<String> comboAlternatives = new ComboBox<>("Alternatives Ids");
    Grid<String> gridAlternatives;
    Button addAlternative;
    Button deleteAlternative;
    String editing;

    public AlternativeIdsForm(List<String> alternatives)
    {
        this.alternativesId = alternatives;
        this.alternativesInGrid = alternatives;
        //this.alternativesInGrid = new LinkedList<>();
        setSizeFull();

        configureElements();

        configureGrids();
    }

    private void configureElements() {

        setWidthFull();
        comboAlternatives.setItems();

        addAlternative = new Button("+");
        addAlternative.addClickListener(buttonClickEvent -> addAlternativeToGrid());


        deleteAlternative = new Button("Eliminar");
        deleteAlternative.setVisible(false);
        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setClassName("buttonsidsAlternativesLayouts");
        actions.add(addAlternative,deleteAlternative);

        HorizontalLayout principal = new HorizontalLayout();
        principal.add(comboAlternatives,actions);

        HorizontalLayout gridAlternativesLayout = new HorizontalLayout();
        gridAlternativesLayout.setWidthFull();
        gridAlternatives = new Grid<String>();
        gridAlternatives.setItems(alternativesId);
        gridAlternativesLayout.add(gridAlternatives);

        add(principal,gridAlternativesLayout);
    }

    private void addAlternativeToGrid() {
        String nuevo = comboAlternatives.getValue();

        if(nuevo != null)
        {
            alternativesId.remove(nuevo);
            alternativesInGrid.add(nuevo);

            gridAlternatives.setItems(alternativesInGrid);
            comboAlternatives.setItems(alternativesId);

            updateElements();
        }
        else
            mostrarError("Debe seleccionar un Alternative");
    }

    private void mostrarError(String mensaje) {
        Span content = new Span(mensaje);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void configureGrids() {

        gridAlternatives.addColumn(nombre -> nombre).setHeader("Alternative Id");
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

    public void updateElements(List<String> ids){
        gridAlternatives.setItems(alternativesInGrid);
        comboAlternatives.setItems(ids);
    }

    public void setAlternativesId(List<String> alternativesId)
    {
        this.alternativesId = alternativesId;
        this.comboAlternatives.setItems(alternativesId);
    }

    public void setAsDefault(){
        alternativesInGrid = new LinkedList<>();
        gridAlternatives.setItems(alternativesInGrid);
    }
}
