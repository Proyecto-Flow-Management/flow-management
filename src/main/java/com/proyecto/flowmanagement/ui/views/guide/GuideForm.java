package com.proyecto.flowmanagement.ui.views.guide;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class GuideForm extends FormLayout {
    private Guide guide;

    TextField name = new TextField("Nombre de la Guia");
    TextField label = new TextField("Etiqueta");
    TextField mainStep = new TextField("Paso principal");
    ComboBox<Step> step = new ComboBox<>("Step");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Guide> binder = new BeanValidationBinder<>(Guide.class);

    public GuideForm(List<Step> steps) {
        addClassName("guide-form");

        binder.bindInstanceFields(this);
        step.setItems(steps);
        step.setItemLabelGenerator(Step::getName);

        add(name,
                label,
                mainStep,
                step,
                createButtonsLayout());
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
        binder.readBean(guide);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new GuideForm.DeleteEvent(this, guide)));
        close.addClickListener(click -> fireEvent(new GuideForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(guide);
            fireEvent(new GuideForm.SaveEvent(this, guide));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class GuideFormEvent extends ComponentEvent<GuideForm> {
        private Guide guide;

        protected GuideFormEvent(GuideForm source, Guide guide) {
            super(source, false);
            this.guide = guide;
        }

        public Guide getGuide() {
            return guide;
        }
    }

    public static class SaveEvent extends GuideForm.GuideFormEvent {
        SaveEvent(GuideForm source, Guide guide) {
            super(source, guide);
        }
    }

    public static class DeleteEvent extends GuideForm.GuideFormEvent {
        DeleteEvent(GuideForm source, Guide guide) {
            super(source, guide);
        }

    }

    public static class CloseEvent extends GuideForm.GuideFormEvent {
        CloseEvent(GuideForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}