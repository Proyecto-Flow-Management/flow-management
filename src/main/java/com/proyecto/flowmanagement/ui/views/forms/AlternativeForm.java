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

    public ConditionsForm conditionForm;

    TextField label = new TextField("Label Alternative");

    public List<Step> stepList = new LinkedList<>();

    TextField nextStep = new TextField("Referencia");
    ComboBox<String> option = new ComboBox<>("Referencia");
    ComboBox<Step> stepComboBox = new ComboBox<>("Steps");

    public boolean isValid;

    public Button save = new Button("Guardar");
    public Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");


    public AlternativeForm(List<Step> stepList) {
        isValid = false;
        setClassName("alternativeSection");
        configureElements();
        editing = false;
    }

    public void configureElements() {

        save.addClickListener(buttonClickEvent -> saveAlternative());

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
        elements.add(label, option, nextStep, stepComboBox);

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

    private void saveAlternative() {

        this.alternative = new Alternative();
        
        alternative.setLabel(this.label.getValue());
        alternative.setConditions(this.conditionForm.getConditions());

        if(this.option.getValue() == "nextStep Existente")
        {
            this.alternative.setNextStep(this.stepComboBox.getValue().getTextId());
            this.alternative.setNewStep(false);
        }
        else if(this.option.getValue() == "nextStep Nuevo")
        {
            this.alternative.setNextStep(this.nextStep.getValue());
            this.alternative.setNewStep(true);
        }
        else
        {
            this.alternative.setGuideName(this.nextStep.getValue());
            this.alternative.setNewStep(false);
        }

        String mensajeValidacion = alternative.validacionIncompleta();
        
        if(!mensajeValidacion.isEmpty())
            mostrarMensajeError(mensajeValidacion);

        isValid = mensajeValidacion.isEmpty();
    }

    private void mostrarMensajeError(String mensajeValidacion) {
        Span mensaje = new Span(mensajeValidacion);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public void setAlternative(Alternative alternative) {
        if (alternative != null)
        {
            this.alternative = alternative;

            if(alternative.getNextStep() != null)
            {
                this.nextStep.setValue(alternative.getNextStep());

                if(this.alternative.getNewStep())
                {
                    this.option.setValue("nextStep Nuevo");
                    this.stepComboBox.setVisible(false);
                    this.nextStep.setVisible(true);
                }
                else
                {
                    this.stepComboBox.setVisible(true);
                    this.nextStep.setVisible(false);
                    this.option.setValue("nextStep Existente");
                    Step stepSelected = this.stepList.stream().filter(step -> step.getTextId() == alternative.getNextStep()).findFirst().get();
                    this.stepComboBox.setValue(stepSelected);
                }
            }
            else
            {
                this.stepComboBox.setVisible(false);
                this.nextStep.setVisible(true);
                this.option.setValue("guideName");
                this.nextStep.setValue(this.alternative.getGuideName());
            }

            this.label.setValue(alternative.getLabel());
            editing = true;
            delete.setVisible(true);
            conditionForm.updateForm(alternative);
        }
        else
        {
            this.nextStep.setValue("");
            this.label.setValue("");
        }
    }

    public Alternative getAlternative()
    {
        return this.alternative;
    }

    public void setAsDefault() {
        this.nextStep.setValue("");
        this.label.setValue("");
        this.conditionForm.setAsDefault();
        this.alternative = new Alternative();
        editing = false;
        stepComboBox.setVisible(false);
        option.setValue("nextStep Nuevo");
        nextStep.setVisible(true);
        conditionForm.agregarLayout.setVisible(true);
    }
}
