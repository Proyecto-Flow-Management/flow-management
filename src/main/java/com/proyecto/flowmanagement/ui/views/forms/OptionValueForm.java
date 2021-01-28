package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.OptionValue;
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

@CssImport("./styles/option-value-form.css")
public class OptionValueForm extends VerticalLayout {

    HorizontalLayout actionsLayout = new HorizontalLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    HorizontalLayout gridLayout = new HorizontalLayout();

    List<OptionValue> optionValues = new LinkedList<>();
    Grid<OptionValue> optionValuesGrid = new Grid<>();
    OptionValue editing = new OptionValue();

    TextField optionValue = new TextField("OptionValue");
    Button add = new Button("Crear");
    Button delete = new Button("Eliminar");
    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");


    public OptionValueForm()
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
        actionsLayout.add(optionValue);

        ppal.add(actionsLayout,buttonsLayout);

        gridLayout.add(optionValuesGrid);

        add(ppal, gridLayout);
    }

    private void configureElements()
    {
        buttonsLayout.setClassName("buttonsLayout");
        this.optionValue.setVisible(true);
        this.optionValue.setValue("");
        this.add.setVisible(true);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        this.cancel.setVisible(false);
        this.save.setVisible(false);
    }

    private void configureForEditing()
    {
        buttonsLayout.setClassName("buttonsEditingLayout");
        this.optionValue.setVisible(true);
        this.add.setVisible(false);
        this.save.setVisible(true);
        this.delete.setVisible(true);
        this.cancel.setVisible(true);
        this.save.setVisible(true);
    }

    private void configureGrid() {
        optionValuesGrid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        delete.addClickListener(buttonClickEvent -> eliminar());
        cancel.addClickListener(buttonClickEvent -> cancel());
        add.addClickListener(buttonClickEvent -> agregar());
        save.addClickListener(buttonClickEvent -> guardar());

        optionValuesGrid.addColumn(value-> {  return value.getOptionValueName(); }).setHeader("OptionValue").setSortable(true);
    }

    private void guardar()
    {
        if(!optionValue.getValue().isEmpty())
        {
            int index = optionValues.indexOf(this.editing);
            editing.setOptionValueName(this.optionValue.getValue());
            optionValues.set(index, editing);
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

    private void agregar()
    {
        OptionValue newOptionValue = new OptionValue();
        newOptionValue.setOptionValueName(this.optionValue.getValue());
        optionValues.add(newOptionValue);
        updateGrid();
        configureElements();
    }

    private void cancel()
    {
        this.optionValue.setValue("");
        this.optionValuesGrid.deselectAll();
    }

    private void eliminar()
    {
        this.optionValues.remove(editing);
        updateGrid();
        configureElements();
    }

    private void edit(OptionValue value)
    {
        if(value != null)
        {
            configureForEditing();
            this.editing = value;
            this.optionValue.setValue(value.getOptionValueName());
        }
    }

    public void updateGrid()
    {
        try
        {
            optionValuesGrid.setItems(optionValues);
        }
        catch (Exception ex)
        {
            String esto = ex.getMessage();
        }
    }

    public void addName()
    {
        if(!optionValue.getValue().isEmpty())
        {
            String nuevo = this.optionValue.getValue();
            OptionValue opV = new OptionValue();

            opV.setOptionValueName(nuevo);
            optionValues.add(opV);
            updateGrid();
            this.optionValue.setValue("");
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

    public List<OptionValue> getOptionValues()
    {
        return this.optionValues;
    }
}
