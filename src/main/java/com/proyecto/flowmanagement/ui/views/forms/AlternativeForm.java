package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
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
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/alternative-form.css")
public class AlternativeForm extends VerticalLayout {

    public boolean editing;
    private Alternative alternative;

    ConditionsForm conditionForm;

    TextField guideName = new TextField("Guia Nombre Alternative");
    TextField label = new TextField("Label Alternative");

    public List<Step> stepList = new LinkedList<>();

    TextField nextStep = new TextField("Tipo");
    ComboBox<String> option = new ComboBox<>("Referencia");
    ComboBox<Step> stepComboBox = new ComboBox<>("Steps");

    Autocomplete autocomplete;

    public Button save = new Button("Guardar");
    Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");


    public AlternativeForm(List<Step> stepList) {
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

        List<String> options = new LinkedList<>();
        options.add("nextStep Existente");
        options.add("nextStep Nuevo");
        options.add("guideName");

        option.addValueChangeListener(b -> configurarOpcion(b.getValue()));

        option.setItems(options);
        stepComboBox.setVisible(false);
        elements.add(guideName, label, option, nextStep, stepComboBox);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        actionsLayout.add(save,close,delete);

        conditionsLayout.setWidthFull();

        conditionForm = new ConditionsForm();

        conditionsLayout.add(conditionForm);

        form.add(elements,conditionsLayout, actionsLayout);

        add(form);
    }

    private void configurarOpcion(String valor) {
        if(valor == "nextStep Existente")
        {
            this.stepComboBox.setItems(this.stepList);
            this.stepComboBox.setVisible(true);
            this.nextStep.setVisible(false);
        }
        else
        {
            this.stepComboBox.setVisible(false);
            this.nextStep.setVisible(true);
        }
    }

    private List<String> findOptions(String text) {
        return this.stepList.stream().
                filter(step -> step.getLabel().contains(text)).map(x -> x.getLabel())
                .collect(Collectors.toList());
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
            editing = true;
            delete.setVisible(true);
        }
        else
        {
            this.guideName.setValue("");
            this.nextStep.setValue("");
            this.label.setValue("");
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
