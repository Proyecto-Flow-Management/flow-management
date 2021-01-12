package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.ui.views.grids.ConditionsGridForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/alternative-form.css")
public class AlternativeForm extends VerticalLayout {

    public boolean editing;
    private Alternative alternative;

    ConditionForm conditionForm = new ConditionForm();

    TextField guideName = new TextField("Guia Nombre Alternative");
    TextField label = new TextField("Label Alternative");
    TextField nextStep = new TextField("nextStep Alternative");

    public Button save = new Button("Guardar");
    Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");


    public AlternativeForm() {
        setClassName("alternativeSection");
        configureElements();
        editing = false;
    }

    public void configureElements() {

        save.addClickListener(buttonClickEvent -> validateAndSave());

        VerticalLayout form = new VerticalLayout();

        HorizontalLayout elements = new HorizontalLayout();

        HorizontalLayout conditionsLayout = new HorizontalLayout();

        HorizontalLayout actionsLayout = new HorizontalLayout();

        this.label.setRequired(true);
        this.label.setErrorMessage("Este campo es obligatorio.");

        elements.add(guideName, label, nextStep);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        actionsLayout.add(save,close,delete);

        conditionsLayout.setWidthFull();

        conditionsLayout.add(conditionForm);

        form.add(elements,conditionsLayout, actionsLayout);

        add(form);
    }

    private void validateAndSave() {
        if (isValid()){
            this.alternative = new Alternative();
            alternative.setLabel(this.label.getValue());
            alternative.setGuideName(this.guideName.getValue());
            alternative.setNextStep(this.nextStep.getValue());
        }
        else {
            Span content = new Span("Algún valor ingresado no es correcto o falta completar campos.");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    public void setAlternative(Alternative alternative) {
        if (alternative != null)
        {
            this.alternative = alternative;
            this.guideName.setValue(alternative.getGuideName());
            this.nextStep.setValue(alternative.getNextStep());
            this.label.setValue(alternative.getLabel());
            this.conditionForm.setConditions(alternative);
            editing = true;
            delete.setVisible(true);
        }
        else
        {
            this.guideName.setValue("");
            this.nextStep.setValue("");
            this.label.setValue("");
            this.conditionForm.setAsDefault();
        }
    }

    public boolean isValid() {
        boolean result = false;

        if(validateFields())
            result = true;

        return result;
    }

    public boolean validateFields(){
        boolean result = false;

        if(!this.label.getValue().isEmpty() &&
                (!this.guideName.getValue().isEmpty() || !this.nextStep.getValue().isEmpty()))
            result = true;

        return result;
    }

    public Alternative getAlternative()
    {
        this.alternative.setConditions(conditionForm.getUnaryConditions());
        this.alternative.setBinaryConditions(conditionForm.getBinaryConditions());
        return this.alternative;
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
