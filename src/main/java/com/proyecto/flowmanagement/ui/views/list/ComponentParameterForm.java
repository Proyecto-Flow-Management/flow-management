package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Component_Parameter;
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

public class ComponentParameterForm extends FormLayout {
    private Component_Parameter componentParameter;

    TextField name = new TextField("Name");
    TextField label = new TextField("Label");
    TextField value = new TextField("Value");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Component_Parameter> binder = new BeanValidationBinder<>(Component_Parameter.class);

    public ComponentParameterForm() {
        addClassName("componentparameter-form");

        binder.bindInstanceFields(this);

        add(name,
                label,
                value,
                createButtonsLayout());
    }

    public void setComponentParameter(Component_Parameter componentParameter) {
        this.componentParameter = componentParameter;
        binder.readBean(componentParameter);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, componentParameter)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(componentParameter);
            fireEvent(new SaveEvent(this, componentParameter));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class ComponentParameterFormEvent extends ComponentEvent<ComponentParameterForm> {
        private Component_Parameter componentParameter;

        protected ComponentParameterFormEvent(ComponentParameterForm source, Component_Parameter componentParameter) {
            super(source, false);
            this.componentParameter = componentParameter;
        }

        public Component_Parameter getComponentParameter() {
            return componentParameter;
        }
    }

    public static class SaveEvent extends ComponentParameterFormEvent {
        SaveEvent(ComponentParameterForm source, Component_Parameter componentParameter) {
            super(source, componentParameter);
        }
    }

    public static class DeleteEvent extends ComponentParameterFormEvent {
        DeleteEvent(ComponentParameterForm source, Component_Parameter componentParameter) {
            super(source, componentParameter);
        }

    }

    public static class CloseEvent extends ComponentParameterFormEvent {
        CloseEvent(ComponentParameterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
