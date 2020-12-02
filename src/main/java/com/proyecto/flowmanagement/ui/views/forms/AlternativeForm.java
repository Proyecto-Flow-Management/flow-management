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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class AlternativeForm extends FormLayout {
    private Alternative alternative;


    TextField name = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");

    Button save = new Button("Guardar");
    Button delete = new Button("Borrar");
    Button close = new Button("Cancelar");

    Binder<Alternative> binder = new BeanValidationBinder<>(Alternative.class);

    public AlternativeForm() {
        addClassName("alternative-form");

        binder.bindInstanceFields(this);

        add(name,
                label,
                createButtonsLayout());
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

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new AlternativeForm.DeleteEvent(this, alternative)));
        close.addClickListener(click -> fireEvent(new AlternativeForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(alternative);
            fireEvent(new AlternativeForm.SaveEvent(this, alternative));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class AlternativeFormEvent extends ComponentEvent<AlternativeForm> {
        private Alternative alternative;

        protected AlternativeFormEvent(AlternativeForm source, Alternative alternative) {
            super(source, false);
            this.alternative = alternative;
        }

        public Alternative getAlternative() {
            return alternative;
        }
    }

    public static class SaveEvent extends AlternativeForm.AlternativeFormEvent {
        SaveEvent(AlternativeForm source, Alternative alternative) {
            super(source, alternative);
        }
    }

    public static class DeleteEvent extends AlternativeForm.AlternativeFormEvent {
        DeleteEvent(AlternativeForm source, Alternative alternative) {
            super(source, alternative);
        }

    }

    public static class CloseEvent extends AlternativeForm.AlternativeFormEvent {
        CloseEvent(AlternativeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
