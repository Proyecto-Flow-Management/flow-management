package com.proyecto.flowmanagement.ui.views.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/groups-form.css")
public class OperationNotifyIdsForm extends VerticalLayout {

    List<String> operationId;
    List<String> operationInGrid;

    ComboBox<String> comboOperation = new ComboBox<>("Operation Ids");
    Grid<String> gridOperation;
    Button addOperation;
    Button deleteOperation;
    String editing;

    public OperationNotifyIdsForm(List<String> alternatives)
    {
        this.operationId = alternatives;
        this.operationInGrid = alternatives;
        //this.alternativesInGrid = new LinkedList<>();
        setSizeFull();

        configureElements();

        configureGrids();
    }

    private void configureElements() {

        setWidthFull();
        comboOperation.setItems();

        addOperation = new Button("+");
        addOperation.addClickListener(buttonClickEvent -> addAlternativeToGrid());


        deleteOperation = new Button("Eliminar");
        deleteOperation.setVisible(false);
        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setClassName("buttonsidsAlternativesLayouts");
        actions.add(addOperation, deleteOperation);

        HorizontalLayout principal = new HorizontalLayout();
        principal.add(comboOperation,actions);

        HorizontalLayout gridAlternativesLayout = new HorizontalLayout();
        gridAlternativesLayout.setWidthFull();
        gridOperation = new Grid<String>();
        gridOperation.setItems(operationId);
        gridAlternativesLayout.add(gridOperation);

        add(principal,gridAlternativesLayout);
    }

    private void addAlternativeToGrid() {
        String nuevo = comboOperation.getValue();

        if(nuevo != null)
        {
            operationId.remove(nuevo);
            operationInGrid.add(nuevo);

            gridOperation.setItems(operationInGrid);
            comboOperation.setItems(operationId);

            updateElements();
        }
        else
            mostrarError("Debe seleccionar un Operation");
    }

    private void mostrarError(String mensaje) {
        Span content = new Span(mensaje);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void configureGrids() {

        gridOperation.addColumn(nombre -> nombre).setHeader("Operation Id");
        gridOperation.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        deleteOperation.addClickListener(buttonClickEvent -> deleteEvent());
    }

    private void deleteEvent() {
        operationId.add(editing);
        operationInGrid.remove(editing);
        editing = "";
        gridOperation.setItems(operationInGrid);
        updateElements();
    }

    private void edit(String value) {
        this.editing = value;
        this.deleteOperation.setVisible(true);
    }

    public void updateElements(){
        gridOperation.setItems(operationInGrid);
        comboOperation.setItems(operationId);
    }

    public void updateElements(List<String> ids){
        gridOperation.setItems(operationInGrid);
        comboOperation.setItems(ids);
    }

    public void setAlternativesId(List<String> alternativesId)
    {
        this.operationId = alternativesId;
        this.comboOperation.setItems(alternativesId);
    }

    public void setAsDefault(){
        operationInGrid = new LinkedList<>();
        gridOperation.setItems(operationInGrid);
    }
}
