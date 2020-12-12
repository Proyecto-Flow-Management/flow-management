package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.BinaryCondition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/binary-form.css")
public class BinaryConditionForm extends VerticalLayout {

    BinaryCondition binaryCondition = new BinaryCondition();
public boolean editing;
    TextField operationNameText = new TextField("Operation Name");
    
    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    Binder<BinaryCondition> binder = new BeanValidationBinder<>(BinaryCondition.class);

    public BinaryConditionForm() {
        configureElements();
    }

    private void configureElements() {
        editing = false;
        addClassName("binaryForm");
        this.operationNameText.setValue("");
        add(operationNameText, createButtonsLayout());
    }
    private Component createButtonsLayout() {
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.setVisible(false);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(click -> validateAndSave());
        return new HorizontalLayout(save, close, delete);
    }

    private void validateAndSave() {
        if(isValid())
        {
            binaryCondition = new BinaryCondition();
            binaryCondition.setOperator(operationNameText.getValue());
        }

    }

    public void setBinaryCondition(BinaryCondition binaryCondition) {
        if(binaryCondition != null){
            this.editing = true;
            this.operationNameText.setValue(binaryCondition.getOperator());
            this.delete.setVisible(true);
        }
        else{
            this.operationNameText.setValue("");
            this.editing=false;
        }
    }


    public BinaryCondition getBinaryCondition()
    {
        return this.binaryCondition;
    }

    private boolean isValid() {
        boolean result = false;

        if(!operationNameText.getValue().isEmpty())
            result = true;

        return result;
    }

    // Events
    public static abstract class BinaryConditionFormEvent extends ComponentEvent<BinaryConditionForm> {
        private BinaryCondition binaryCondition;

        protected BinaryConditionFormEvent(BinaryConditionForm source, BinaryCondition binaryCondition) {
            super(source, false);
            this.binaryCondition = binaryCondition;
        }

        public BinaryCondition getBinaryCondition() {
            return binaryCondition;
        }
    }

    public static class SaveEvent extends BinaryConditionForm.BinaryConditionFormEvent {
        SaveEvent(BinaryConditionForm source, BinaryCondition BinaryCondition) {
            super(source, BinaryCondition);
        }
    }

    public static class DeleteEvent extends BinaryConditionForm.BinaryConditionFormEvent {
        DeleteEvent(BinaryConditionForm source, BinaryCondition BinaryCondition) {
            super(source, BinaryCondition);
        }

    }

    public static class CloseEvent extends BinaryConditionForm.BinaryConditionFormEvent {
        CloseEvent(BinaryConditionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
