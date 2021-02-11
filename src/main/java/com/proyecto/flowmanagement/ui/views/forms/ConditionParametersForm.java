package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.util.LinkedList;
import java.util.List;

public class ConditionParametersForm  extends VerticalLayout {

    ConditionParameter conditionParameter;

    public Button save = new Button("Guardar");
    public Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");
    public boolean isValid;
    TextField field;
    TextField fieldType;
    TextField operator;
    TextField value;

    public ConditionParametersForm(){

        setSizeFull();

        configureElement();

        configureButtons();

        configureForm();
    }

    private void configureForm()
    {
        HorizontalLayout camposLayout = new HorizontalLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        camposLayout.add(field,fieldType,operator,value);
        botonesLayout.add(save,close,delete);

        add(camposLayout,botonesLayout);
    }

    private void configureElement() {

        isValid = false;

        conditionParameter = new ConditionParameter();

        field = new TextField("Field");
        fieldType = new TextField("Type");
        operator = new TextField("Operator");
        value = new TextField("Value");
    }

    private void configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> save());
    }

    private void save()
    {
        ConditionParameter newConditionParameter = new ConditionParameter();
        newConditionParameter.setOperator(operator.getValue());
        newConditionParameter.setFieldType(fieldType.getValue());
        newConditionParameter.setField(field.getValue());
        newConditionParameter.setValue(value.getValue());
        conditionParameter = newConditionParameter;

        String mensajesError = conditionParameter.validarConditionParameter();

        isValid = mensajesError.isEmpty();

        if(!isValid)
            mostrarMensajeError(mensajesError);
    }

    private void mostrarMensajeError(String mensajesError) {
        Span mensaje = new Span(mensajesError);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public ConditionParameter getConditionParameter()
    {
        return this.conditionParameter;
    }


    public void setAsDefault() {
        this.operator.setValue("");
        this.field.setValue("");
        this.fieldType.setValue("");
        this.value.setValue("");
        this.conditionParameter = new ConditionParameter();
    }

    public void setForm(ConditionParameter value) {
        this.operator.setValue(value.getOperator());
        this.field.setValue(value.getField());
        this.fieldType.setValue(value.getFieldType());
        this.value.setValue(value.getValue());
        this.conditionParameter = value;
    }
}
