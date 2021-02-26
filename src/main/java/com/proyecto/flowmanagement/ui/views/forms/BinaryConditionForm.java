package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.TypeOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.Condition;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/binary-form.css")
public class BinaryConditionForm extends HorizontalLayout {

    public boolean editing;
    Condition binaryCondition = new Condition();
    
    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");

    ComboBox<String> operationNameText = new ComboBox<>("Operation Name");

    public boolean isValid;

    VerticalLayout form = new VerticalLayout();
    HorizontalLayout fields = new HorizontalLayout();
    HorizontalLayout actions = new HorizontalLayout();

    public BinaryConditionForm() {
        isValid = false;
        editing = false;
        List<String> opcionesList = new LinkedList<>();
        opcionesList.add("OR");
        opcionesList.add("AND");
        operationNameText.setItems(opcionesList);
        setWidthFull();

        configureElements();

        configureForm();
    }

    private void configureForm() {
        form.add(fields, actions);
        add(form);
    }

    private void configureElements() {
        editing = false;
        fields.add(operationNameText);
        addClassName("binaryForm");
        createButtonsLayout();
        this.operationNameText.setValue("");
    }
    private void createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(click -> save());
        actions.add(save, close);
    }

    private void save() {
        binaryCondition = new Condition();
        binaryCondition.setOperation(operationNameText.getValue());
        binaryCondition.setType(TypeOperation.binaryCondition);

        String mensajeError = binaryCondition.validarUnaryIncompleto();

        isValid = mensajeError.isEmpty();

        if(!isValid)
            mostrarMensajeError(mensajeError);
    }

    private void mostrarMensajeError(String mensajeError) {
        Span mensaje = new Span(mensajeError);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public void setBinaryCondition(Condition binaryCondition) {
        this.operationNameText.setValue(binaryCondition.getOperation());
    }


    public Condition getBinaryCondition()
    {
        return this.binaryCondition;
    }

    private boolean isValid() {
        boolean result = false;

        if(!operationNameText.getValue().isEmpty())
            result = true;

        return result;
    }

    public void setAsDefault() {
        this.operationNameText.setValue("");
    }
}
