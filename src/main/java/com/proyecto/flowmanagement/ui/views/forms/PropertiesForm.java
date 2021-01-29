package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Groups;
import com.proyecto.flowmanagement.backend.persistence.entity.Properties;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.ui.CheckBox;

import javax.persistence.criteria.From;
import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/properties-form.css")
public class PropertiesForm extends VerticalLayout {
    FormLayout actionsLayout = new FormLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    HorizontalLayout gridLayout = new HorizontalLayout();

    List<Properties> properties = new LinkedList<>();
    Grid<Properties> propertiesGrid = new Grid<>();
    Properties editing = new Properties();

    TextField name = new TextField("Name");
    TextField label = new TextField("Label");
    ComboBox<String> visible = new ComboBox<>("Visible");
    TextField type = new TextField("Type");
    Button add = new Button("Crear");
    Button delete = new Button("Eliminar");
    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");


    public PropertiesForm()
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

        VerticalLayout ppal = new VerticalLayout();
        ppal.setWidthFull();

        buttonsLayout.add(add,delete,save,cancel);
        actionsLayout.add(name, label, visible, type);
        actionsLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3),
                new FormLayout.ResponsiveStep("40em", 4));

        ppal.add(actionsLayout,buttonsLayout);

        gridLayout.add(propertiesGrid);

        add(ppal, gridLayout);
    }

    private void configureElements()
    {
        buttonsLayout.setClassName("buttonsLayout");
        this.name.setVisible(true);
        this.name.setValue("");
        this.label.setVisible(true);
        this.label.setValue("");
        this.visible.setItems("True","False");
        visible.addClassName("vaadin-text-field-container");
        this.visible.setVisible(true);
        this.visible.setValue("False");
        this.type.setVisible(true);
        this.type.setValue("");
        this.add.setVisible(true);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        this.cancel.setVisible(false);
        this.save.setVisible(false);
    }

    private void configureForEditing()
    {
        buttonsLayout.setClassName("buttonsEditingLayout");
        this.name.setVisible(true);
        this.label.setVisible(true);
        this.visible.setVisible(true);
        this.type.setVisible(true);
        this.add.setVisible(false);
        this.save.setVisible(true);
        this.delete.setVisible(true);
        this.cancel.setVisible(true);
        this.save.setVisible(true);
    }

    private void configureGrid() {
        propertiesGrid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        delete.addClickListener(buttonClickEvent -> eliminar());
        cancel.addClickListener(buttonClickEvent -> cancel());
        add.addClickListener(buttonClickEvent -> addProperty());
        save.addClickListener(buttonClickEvent -> guardar());

        propertiesGrid.addColumn(value-> {  return value.getName(); }).setHeader("Name").setSortable(true);
        propertiesGrid.addColumn(value-> {  return value.getLabel(); }).setHeader("Label").setSortable(true);
        propertiesGrid.addColumn(value-> {  return value.getVisible(); }).setHeader("Visible").setSortable(true);
        propertiesGrid.addColumn(value-> {  return value.getType(); }).setHeader("Type").setSortable(true);
    }

    private void guardar()
    {
        if(!name.getValue().trim().isEmpty() && !label.getValue().trim().isEmpty() && !type.getValue().trim().isEmpty())
        {
            int index = properties.indexOf(this.editing);
            editing.setName(this.name.getValue());
            editing.setLabel(this.label.getValue());
            editing.setVisible(Boolean.valueOf(visible.getValue()));
            editing.setType(this.type.getValue());
            properties.set(index, editing);
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
        this.name.setValue("");
        this.label.setValue("");
        this.visible.setValue("False");
        this.type.setValue("");
        this.propertiesGrid.deselectAll();
        configureElements();
    }

    private void eliminar()
    {
        this.properties.remove(editing);
        updateGrid();
        configureElements();
    }

    private void edit(Properties value)
    {
        if(value != null)
        {
            configureForEditing();
            this.editing = value;
            this.name.setValue(value.getName());
            this.label.setValue(value.getLabel());
            this.visible.setValue(value.getVisible().toString());
            this.type.setValue(value.getType());
        }
    }

    public void updateGrid()
    {
        try
        {
            propertiesGrid.setItems(properties);
        }
        catch (Exception ex)
        {
            String esto = ex.getMessage();
        }
    }

    public void addProperty()
    {
        if(!name.getValue().trim().isEmpty() && !label.getValue().trim().isEmpty() && !type.getValue().trim().isEmpty())
        {
            String nuevoName = this.name.getValue();
            String nuevoLabel = this.label.getValue();
            Boolean nuevoVisible = Boolean.valueOf(this.visible.getValue());
            String nuevoType = this.type.getValue();
            Properties prp = new Properties();

            prp.setName(nuevoName);
            prp.setLabel(nuevoLabel);
            prp.setVisible(nuevoVisible);
            prp.setType(nuevoType);
            properties.add(prp);
            updateGrid();
            this.name.setValue("");
            this.label.setValue("");
            this.visible.setValue("False");
            this.type.setValue("");
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

    public List<Properties> getProperties()
    {
        return this.properties;
    }
}
