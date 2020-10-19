package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation_Type;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class OperationTypeForm extends FormLayout {
    private Operation_Type operationType;

    TextField name = new TextField("Name");
    TextField label = new TextField("Label");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Operation_Type> binder = new BeanValidationBinder<>(Operation_Type.class);

    public OperationTypeForm() {
        addClassName("operationtype-form");

        binder.bindInstanceFields(this);

        add(name,
                label,
                createButtonsLayout());
    }

    public void setOperationType(Operation_Type operationType) {
        this.operationType = operationType;
        binder.readBean(operationType);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, operationType)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(operationType);
            fireEvent(new SaveEvent(this, operationType));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class OperationTypeFormEvent extends ComponentEvent<OperationTypeForm> {
        private Operation_Type operationType;

        protected OperationTypeFormEvent(OperationTypeForm source, Operation_Type operationType) {
            super(source, false);
            this.operationType = operationType;
        }

        public Operation_Type getOperationType() {
            return operationType;
        }
    }

    public static class SaveEvent extends OperationTypeFormEvent {
        SaveEvent(OperationTypeForm source, Operation_Type operationType) {
            super(source, operationType);
        }
    }

    public static class DeleteEvent extends OperationTypeFormEvent {
        DeleteEvent(OperationTypeForm source, Operation_Type operationType) {
            super(source, operationType);
        }

    }

    public static class CloseEvent extends OperationTypeFormEvent {
        CloseEvent(OperationTypeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
