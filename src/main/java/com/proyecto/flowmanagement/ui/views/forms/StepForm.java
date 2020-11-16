package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@org.springframework.stereotype.Component
@Route(value = "CreateStep", layout = MainLayout.class)
@PageTitle("CreateStep | Flow Management")
public class StepForm extends VerticalLayout {
    private Step step;

    TextField name = new TextField("Name");
    TextField label = new TextField("Label");
//    TextField nextStep = new TextField("NextStep");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Step> binder = new BeanValidationBinder<>(Step.class);

    public StepForm() {
        addClassName("step-form");

        binder.bindInstanceFields(this);
        generateStyle();

        add(name,
                label,
//                nextStep,
                createButtonsLayout());
    }

    public void setStep(Step step) {
        this.step = step;
        binder.readBean(step);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(event -> UI.getCurrent().navigate("StepList"));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(step);
            fireEvent(new SaveEvent(this, step));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void generateStyle(){
        setAlignItems(Alignment.CENTER);
        name.setWidth("60%");
        label.setWidth("60%");
    }

    // Events
    public static abstract class StepFormEvent extends ComponentEvent<StepForm> {
        private Step step;

        protected StepFormEvent(StepForm source, Step step) {
            super(source, false);
            this.step = step;
        }

        public Step getStep() {
            return step;
        }
    }

    public static class SaveEvent extends StepFormEvent {
        SaveEvent(StepForm source, Step step) {
            super(source, step);
        }
    }

    public static class DeleteEvent extends StepFormEvent {
        DeleteEvent(StepForm source, Step step) {
            super(source, step);
        }

    }

    public static class CloseEvent extends StepFormEvent {
        CloseEvent(StepForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}