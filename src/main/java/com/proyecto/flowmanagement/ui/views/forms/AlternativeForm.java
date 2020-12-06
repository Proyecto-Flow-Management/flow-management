package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import sun.invoke.util.VerifyType;

public class AlternativeForm extends VerticalLayout {

    private Alternative alternative;

    UnaryConditionForm unaryConditionForm = new UnaryConditionForm();

    TextField guideName = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");
    TextField nextStep = new TextField("Etiqueta");

    Button addUnaryCondition = new Button("Agregar Condicion");

    public Button save = new Button("Guardar");
    Button delete = new Button("Borrar");
    Button close = new Button("Cancelar");

    Binder<Alternative> binder = new BeanValidationBinder<>(Alternative.class);

    public AlternativeForm() {
        configureElements();
        binder.bindInstanceFields(this);
    }

    public static void setStep(Alternative alternative) {
    }

    public void configureElements() {
        addClassName("alternative-form");
        HorizontalLayout elements = new HorizontalLayout();

        elements.add(guideName, label, nextStep);
        unaryConditionForm = new UnaryConditionForm();
        unaryConditionForm.setVisible(false);

        add(addUnaryCondition, unaryConditionForm, createButtonsLayout());
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
        binder.readBean(alternative);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        addUnaryCondition.addClickListener(buttonClickEvent -> showUnaryForm());

        return new HorizontalLayout(save, delete, close);
    }

    private void showUnaryForm() {
    }

    public boolean isValid() {
        boolean result = false;

        if(!this.label.getValue().isEmpty() &&
           (!this.guideName.getValue().isEmpty() || !this.nextStep.getValue().isEmpty()))
            result = true;

        return false;
    }

    public Alternative getAlternative()
    {
        this.alternative = new Alternative();
        this.alternative.setLabel(label.getValue());
        this.alternative.setConditions(this.unaryConditionForm.unaryCondition);
        return this.alternative;
    }

}
