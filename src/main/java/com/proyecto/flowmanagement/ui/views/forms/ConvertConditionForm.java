package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.ConvertCondition;
import com.proyecto.flowmanagement.backend.persistence.entity.Groups;
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

@CssImport("./styles/convert-condition-form.css")
public class ConvertConditionForm extends VerticalLayout {

    HorizontalLayout actionsLayout = new HorizontalLayout();
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    HorizontalLayout gridLayout = new HorizontalLayout();

    List<ConvertCondition> convertConditions = new LinkedList<>();
    Grid<ConvertCondition> convertConditionsGrid = new Grid<>();
    ConvertCondition editing = new ConvertCondition();

    TextField condition = new TextField("Condition");
    TextField sourceUnit = new TextField("SourceUnit");
    TextField destinationUnit = new TextField("DestinationUnit");
    Button add = new Button("Crear");
    Button delete = new Button("Eliminar");
    Button save = new Button("Guardar");
    Button cancel = new Button("Cancelar");


    public ConvertConditionForm()
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
        actionsLayout.add(condition, sourceUnit, destinationUnit);

        ppal.add(actionsLayout,buttonsLayout);

        gridLayout.add(convertConditionsGrid);

        add(ppal, gridLayout);
    }

    private void configureElements()
    {
        buttonsLayout.setClassName("buttonsLayout");
        this.condition.setVisible(true);
        this.condition.setValue("");
        this.sourceUnit.setVisible(true);
        this.sourceUnit.setValue("");
        this.destinationUnit.setVisible(true);
        this.destinationUnit.setValue("");
        this.add.setVisible(true);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        this.cancel.setVisible(false);
        this.save.setVisible(false);
    }

    private void configureForEditing()
    {
        buttonsLayout.setClassName("buttonsEditingLayout");
        this.condition.setVisible(true);
        this.sourceUnit.setVisible(true);
        this.destinationUnit.setVisible(true);
        this.add.setVisible(false);
        this.save.setVisible(true);
        this.delete.setVisible(true);
        this.cancel.setVisible(true);
        this.save.setVisible(true);
    }

    private void configureGrid() {
        convertConditionsGrid.asSingleSelect().addValueChangeListener(evt -> edit(evt.getValue()));
        delete.addClickListener(buttonClickEvent -> eliminar());
        cancel.addClickListener(buttonClickEvent -> cancel());
        add.addClickListener(buttonClickEvent -> agregar());
        save.addClickListener(buttonClickEvent -> guardar());

        convertConditionsGrid.addColumn(value-> {  return value.getCondition(); }).setHeader("Condition").setSortable(true);
        convertConditionsGrid.addColumn(value-> {  return value.getSourceUnit(); }).setHeader("SourceUnit").setSortable(true);
        convertConditionsGrid.addColumn(value-> {  return value.getDestinationUnit(); }).setHeader("DestinationUnit").setSortable(true);
    }

    private void guardar()
    {
        if(!condition.getValue().isEmpty() && !sourceUnit.getValue().isEmpty() && !destinationUnit.getValue().isEmpty())
        {
            int index = convertConditions.indexOf(this.editing);
            editing.setCondition(this.condition.getValue());
            editing.setSourceUnit(this.sourceUnit.getValue());
            editing.setDestinationUnit(this.destinationUnit.getValue());
            convertConditions.set(index, editing);
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
        ConvertCondition newConvertCondition = new ConvertCondition();
        newConvertCondition.setCondition(this.condition.getValue());
        newConvertCondition.setSourceUnit(this.sourceUnit.getValue());
        newConvertCondition.setDestinationUnit(this.destinationUnit.getValue());
        convertConditions.add(newConvertCondition);
        updateGrid();
        configureElements();
    }

    private void cancel()
    {
        this.condition.setValue("");
        this.sourceUnit.setValue("");
        this.destinationUnit.setValue("");
        this.convertConditionsGrid.deselectAll();
    }

    private void eliminar()
    {
        this.convertConditions.remove(editing);
        updateGrid();
        configureElements();
    }

    private void edit(ConvertCondition value)
    {
        if(value != null)
        {
            configureForEditing();
            this.editing = value;
            this.condition.setValue(value.getCondition());
            this.sourceUnit.setValue(value.getSourceUnit());
            this.destinationUnit.setValue(value.getDestinationUnit());
        }
    }

    public void updateGrid()
    {
        try
        {
            convertConditionsGrid.setItems(convertConditions);
        }
        catch (Exception ex)
        {
            String esto = ex.getMessage();
        }
    }

    public void addName()
    {
        if(!condition.getValue().isEmpty() && !sourceUnit.getValue().isEmpty() && !destinationUnit.getValue().isEmpty())
        {
            String nuevoCondition = this.condition.getValue();
            String nuevoSourceUnit = this.sourceUnit.getValue();
            String nuevoDestinationUnit = this.destinationUnit.getValue();
            ConvertCondition ccn = new ConvertCondition();

            ccn.setCondition(nuevoCondition);
            ccn.setSourceUnit(nuevoSourceUnit);
            ccn.setDestinationUnit(nuevoDestinationUnit);
            convertConditions.add(ccn);
            updateGrid();
            this.condition.setValue("");
            this.sourceUnit.setValue("");
            this.destinationUnit.setValue("");
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

    public List<ConvertCondition> getConvertConditions()
    {
        return this.convertConditions;
    }
}
