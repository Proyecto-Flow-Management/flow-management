package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Groups;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/groups-form.css")
public class CandidatesGrupsForm extends VerticalLayout {

    HorizontalLayout actionsLayout = new HorizontalLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    HorizontalLayout gridLayout = new HorizontalLayout();

    List<Groups> groupsNames = new LinkedList<>();
    Grid<Groups> groupGrid = new Grid<>();
    Groups editing;

    TextField group = new TextField("GroupName");
    Button add = new Button("+");
    Button delete = new Button("Eliminar");
    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");


    public CandidatesGrupsForm()
    {
        configureElements();

        configureGrid();

        configureForm();
    }

    private void configureForm()
    {
        gridLayout.setWidthFull();

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout ppal = new HorizontalLayout();
        ppal.setWidthFull();

        buttonsLayout.add(add,delete,save,cancel);
        actionsLayout.add(group);

        ppal.add(actionsLayout,buttonsLayout);

        gridLayout.add(groupGrid);

        add(ppal, gridLayout);
    }

    private void configureElements()
    {
        buttonsLayout.setClassName("buttonsLayouts");
        editing = null;
        this.group.setVisible(true);
        this.group.setValue("");
        this.add.setVisible(true);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        this.cancel.setVisible(false);
        this.save.setVisible(false);
    }

    private void configureForEditing()
    {
        this.group.setVisible(true);
        this.add.setVisible(false);
        this.save.setVisible(true);
        this.delete.setVisible(true);
        this.cancel.setVisible(true);
        this.save.setVisible(true);
    }

    private void configureGrid() {
        groupGrid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        delete.addClickListener(buttonClickEvent -> eliminar());
        cancel.addClickListener(buttonClickEvent -> cancel());
        add.addClickListener(buttonClickEvent -> addName());
        save.addClickListener(buttonClickEvent -> guardar());

        groupGrid.addColumn(value-> {  return value.getGroupName(); }).setHeader("GroupName").setSortable(true);
    }

    private void guardar()
    {
        this.groupGrid.deselectAll();
        if(!group.getValue().isEmpty())
        {
            int index = groupsNames.indexOf(this.editing);
            editing.setGroupName(this.group.getValue());
            groupsNames.set(index, editing);
            updateGrid();
            configureElements();
        }
        else
        {
            Span content = new Span("Debe ingresar algun valor");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    private void cancel()
    {
        this.group.setValue("");
        this.groupGrid.deselectAll();
        this.configureElements();
    }

    private void eliminar()
    {
        this.groupsNames.remove(editing);
        updateGrid();
        configureElements();
    }

    private void edit(Groups value)
    {
        if(value != null)
        {
            configureForEditing();
            this.editing = value;
            this.group.setValue(value.getGroupName());
        }
    }

    public void updateGrid()
    {
        try
        {
            groupGrid.setItems(groupsNames);
        }
        catch (Exception ex)
        {
            String esto = ex.getMessage();
        }
    }

    public void addName()
    {
        if(!group.getValue().isEmpty())
        {
            String nuevo = this.group.getValue();
            Groups grp = new Groups();

            grp.setGroupName(nuevo);
            groupsNames.add(grp);
            updateGrid();
            this.group.setValue("");
        }
        else
        {
            Span content = new Span("Debe ingresar algun valor");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    public void setAsDefault() {
        group.clear();
        this.groupsNames = new LinkedList<>();
        updateGrid();
    }

    public List<Groups> getGroupsNames()
    {
        return this.groupsNames;
    }

    public void setGroupsNames(List<Groups> groupsNames){
        this.groupsNames = groupsNames;
        updateGrid();
    }
}
