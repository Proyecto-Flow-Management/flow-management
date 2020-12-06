package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.AlternativeGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
public class StepForm extends HorizontalLayout {

    private Step step;

    AlternativeGridForm alternativeGridForm = new AlternativeGridForm();

    TextField text = new TextField("Nombre");
    TextField textId = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");


    public StepForm() {
        this.step = new Step();
        configureElements();
    }

    private void configureElements() {
        VerticalLayout form = new VerticalLayout();
        HorizontalLayout elements = new HorizontalLayout();
        HorizontalLayout alternativeGridLayout = new HorizontalLayout();
        HorizontalLayout actionsLayout = new HorizontalLayout();

        addClassName("step-form");
        this.text.setValue("");
        this.textId.setValue("");
        this.label.setValue("");
        setSizeFull();

        elements.add(text,textId,label);

        alternativeGridLayout.add(alternativeGridForm);

        actionsLayout.add(createButtonsLayout());

        form.add(elements,alternativeGridLayout,actionsLayout);

        add(form);
    }

    private Component createButtonsLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new StepForm.CloseEvent(this)));

        //alternativeForm.save.addClickListener(buttonClickEvent -> addAlternative());

        return new HorizontalLayout(save, close);
    }

    private void addAlternative() {
        //if(alternativeForm.isValid())
       // {
        //    this.step.addAlternative(alternativeForm.getAlternative());
        //    updateAlternativeGrid();
       // }
    }

    private void updateAlternativeGrid() {

    }

    private void validateAndSave() {
      if(isValid())
      {
          step.setText(text.getValue());
          step.setTextId(textId.getValue());
          step.setLabel(label.getValue());
      }
    }

    private boolean isValid() {
        boolean result = false;

        if(!text.getValue().isEmpty() &&
           !textId.getValue().isEmpty() &&
           !label.getValue().isEmpty())
            result = true;

        return result;
    }

    public void setStep(Step step) {
    }

    public Step getStep() {
        return this.step;
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

    public static class SaveEvent extends StepForm.StepFormEvent {
        SaveEvent(StepForm source, Step step) {
            super(source, step);
        }
    }

    public static class DeleteEvent extends StepForm.StepFormEvent {
        DeleteEvent(StepForm source, Step step) {
            super(source, step);
        }

    }

    public static class CloseEvent extends StepForm.StepFormEvent {
        CloseEvent(StepForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}