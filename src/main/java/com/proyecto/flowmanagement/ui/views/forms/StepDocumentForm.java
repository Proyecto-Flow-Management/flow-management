package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class StepDocumentForm extends VerticalLayout {
    private StepDocument stepDocument;

    TextField url = new TextField("Url");

    Button save = new Button("Guardar");
//    Button delete = new Button("Borrar");
    Button close = new Button("Cancelar");

    Binder<StepDocument> binder = new BeanValidationBinder<>(StepDocument.class);

    public StepDocumentForm() {
        addClassName("stepdocument-form");

        setSizeFull();
        HorizontalLayout contentFields = new HorizontalLayout(url);
        binder.bindInstanceFields(this);

        add(contentFields, createButtonsLayout());
    }

    public void setStepDocument(StepDocument stepDocument) {
        this.stepDocument = stepDocument;
        binder.readBean(stepDocument);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(stepDocument);
            fireEvent(new StepDocumentForm.SaveEvent(this, stepDocument));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
//        delete.addClickListener(click -> fireEvent(new StepForm.DeleteEvent(this, step)));
        close.addClickListener(click -> fireEvent(new StepDocumentForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
//        return new HorizontalLayout(save, delete, close);
    }


    // Events
    public static abstract class StepDocumentFormEvent extends ComponentEvent<StepDocumentForm> {
        private StepDocument stepDocument;

        protected StepDocumentFormEvent(StepDocumentForm source, StepDocument step) {
            super(source, false);
            this.stepDocument = step;
        }

        public StepDocument getStepDocument() {
            return stepDocument;
        }
    }

    public static class SaveEvent extends StepDocumentForm.StepDocumentFormEvent {
        SaveEvent(StepDocumentForm source, StepDocument stepDocument) {
            super(source, stepDocument);
        }
    }

    public static class DeleteEvent extends StepDocumentForm.StepDocumentFormEvent {
        DeleteEvent(StepDocumentForm source, StepDocument stepDocument) {
            super(source, stepDocument);
        }

    }

    public static class CloseEvent extends StepDocumentForm.StepDocumentFormEvent {
        CloseEvent(StepDocumentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
