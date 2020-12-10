package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.AlternativeGridForm;
import com.proyecto.flowmanagement.ui.views.grids.DocumentsGridForm;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@org.springframework.stereotype.Component
@PageTitle("CreateStep | Flow Management")
@CssImport("./styles/step-grid-form.css")
public class StepForm extends HorizontalLayout {

    private Step step = new Step();

    AlternativeGridForm alternativeGridForm = new AlternativeGridForm();
    DocumentsGridForm documentsGridForm = new DocumentsGridForm();
    OperationGridForm operationGridForm = new OperationGridForm();

    VerticalLayout form = new VerticalLayout();
    HorizontalLayout elements = new HorizontalLayout();
    HorizontalLayout alternativeGridLayout = new HorizontalLayout();
    HorizontalLayout stepDocumentsLayout = new HorizontalLayout();
    HorizontalLayout operationsLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();

    TextField text = new TextField("Texto Step");
    TextField textId = new TextField("TextId Step");
    TextField label = new TextField("Label Step");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");


    public StepForm() {

        setSizeFull();

        configureElements();

        configureAlternatives();

        configureDocuments();

        configureOperations();

        configureForm();
    }

    private void configureForm() {
        form.add(elements,
                alternativeGridLayout,
                operationsLayout,
                stepDocumentsLayout,
                actionsLayout);

        add(form);
    }

    private void configureOperations() {
        operationsLayout.setWidthFull();
        operationsLayout.add(operationGridForm);
    }

    private void configureDocuments() {
        stepDocumentsLayout.setWidthFull();
        stepDocumentsLayout.add(documentsGridForm);
    }

    private void configureAlternatives() {
        alternativeGridForm.setWidthFull();
        alternativeGridLayout.setWidthFull();
        alternativeGridLayout.add(alternativeGridForm);
    }

    private void configureElements() {
        addClassName("stepSection");
        this.text.setValue("");
        this.textId.setValue("");
        this.label.setValue("");

        elements.add(text,textId,label);
        actionsLayout.add(createButtonsLayout());
    }

    private Component createButtonsLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new StepForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
      if(isValid())
      {
          step.setText(text.getValue());
          step.setTextId(textId.getValue());
          step.setLabel(label.getValue());
          step.setAlternatives(alternativeGridForm.getAlternatives());
          step.setStepDocuments(documentsGridForm.getDocuments());
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