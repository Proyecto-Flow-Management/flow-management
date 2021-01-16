package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.ui.views.grids.ConditionsGridForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/alternative-form.css")
public class AlternativeForm extends VerticalLayout {

    public boolean editing;
    private Alternative alternative;
    List<String> stepIdList;

    ConditionForm conditionForm = new ConditionForm();

    TextField label = new TextField("Label Alternative");
    ComboBox transitionCombo = new ComboBox("Transición");
    TextField guideNameText = new TextField("guideName");
    ComboBox stepIdCombo = new ComboBox("stepId");

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
        transitionCombo.addValueChangeListener(event -> showTransitionFields());

        VerticalLayout form = new VerticalLayout();

        HorizontalLayout elements = new HorizontalLayout();

        HorizontalLayout conditionsLayout = new HorizontalLayout();

        HorizontalLayout actionsLayout = new HorizontalLayout();

        this.label.setRequired(true);
        this.label.setErrorMessage("Este campo es obligatorio.");
        this.guideNameText.setRequired(true);
        this.guideNameText.setErrorMessage("Este campo es obligatorio.");
        this.stepIdCombo.setRequired(true);
        this.stepIdCombo.setErrorMessage("Este campo es obligatorio.");
        this.transitionCombo.setItems("guideName","stepId");
        this.transitionCombo.setValue("stepId");

        elements.add(label, transitionCombo, stepIdCombo, guideNameText);

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

    private void showTransitionFields() {
        if (this.transitionCombo.getValue() == "guideName"){
            this.guideNameText.setVisible(true);
            this.stepIdCombo.setVisible(false);
        }
        else if (this.transitionCombo.getValue() == "stepId"){
            this.guideNameText.setVisible(false);
            this.stepIdCombo.setVisible(true);
        }
    }

    public void setStepIdCombo(List stepIds) {
        this.stepIdList = stepIds;
        this.stepIdCombo.setItems(stepIds);
    }

    private void validateAndSave() {
        if (isValid()){
            this.alternative = new Alternative();
            alternative.setLabel(this.label.getValue());
            if (this.transitionCombo.getValue() == "guideName") {
                alternative.setGuideName(this.guideNameText.getValue());
            }
            else if (this.transitionCombo.getValue() == "stepId"){
                alternative.setNextStep(this.stepIdCombo.getValue().toString());
            }
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
            if (this.alternative.getGuideName()!=null){
                this.transitionCombo.setValue("guideName");
                this.guideNameText.setValue(alternative.getGuideName());
                this.guideNameText.setVisible(true);
                this.stepIdCombo.setVisible(false);
            }
            else if (this.alternative.getNextStep()!=null){
                this.transitionCombo.setValue("stepId");
                this.stepIdCombo.setValue(alternative.getNextStep());
                this.stepIdCombo.setVisible(true);
                this.guideNameText.setVisible(false);
            }
            this.label.setValue(alternative.getLabel());
            this.conditionForm.setConditions(alternative);
            editing = true;
            delete.setVisible(true);
        }
        else
        {
            this.guideNameText.setValue("");
            this.stepIdCombo.setValue("");
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

//        if(!this.label.getValue().isEmpty() &&
//                (!this.guideName.getValue().isEmpty() || !this.nextStep.getValue().isEmpty()))
//            result = true;
//
        if(!this.label.getValue().isEmpty() &&
                ((this.transitionCombo.getValue() == "guideName" && !this.guideNameText.getValue().isEmpty()))
                    || (this.transitionCombo.getValue() == "stepId" && !this.stepIdCombo.isEmpty() && this.stepIdList.size() > 1))
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
