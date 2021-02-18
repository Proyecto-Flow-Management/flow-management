package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.grids.ConditionsGridForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.ui.Window;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/alternative-form.css")
public class AlternativeForm extends VerticalLayout {

    public boolean editing;
    private Alternative alternative;

    public ConditionsForm conditionForm;
    Accordion conditionFormAccordion = new Accordion();


    public List<Step> stepList = new LinkedList<>();
    public List<Guide> guideList = new LinkedList<>();
    public List<Guide> systemGuideList = new LinkedList<>();

    TextField label = new TextField("Label Alternative");
    TextField nextStep = new TextField("Referencia");
    ComboBox<String> option = new ComboBox<>("Referencia");
    ComboBox<Step> stepComboBox = new ComboBox<>("Steps");
    ComboBox<Guide> guideComboBox = new ComboBox<>("Guias");
    ComboBox<Guide> systemGuideComboBox = new ComboBox<>("Guias Sistema");

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

        List<String> options = new LinkedList<>();
        options.add("nextStep Existente");
        options.add("nextStep Nuevo");
        options.add("Guia Nueva");
        options.add("Guia Existente");
        options.add("Guia Sistema");

        option.addValueChangeListener(b -> configurarOpcion(b.getValue()));

        option.setItems(options);
        stepComboBox.setItemLabelGenerator(step -> step.getTextId());
        stepComboBox.setVisible(false);
        guideComboBox.setVisible(false);
        systemGuideComboBox.setVisible(false);
        elements.add(label, option, nextStep, stepComboBox, guideComboBox, systemGuideComboBox);
        elements.setWidthFull();
        nextStep.setMinWidth("25%");
        nextStep.setMaxWidth("40%");
        stepComboBox.setMinWidth("25%");
        stepComboBox.setMaxWidth("40%");
        guideComboBox.setMinWidth("35%");
        guideComboBox.setMaxWidth("75%");
        systemGuideComboBox.setMinWidth("35%");
        systemGuideComboBox.setMaxWidth("75%");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        actionsLayout.add(save,close,delete);

        conditionsLayout.setWidthFull();

        conditionForm = new ConditionsForm();

        conditionsLayout.add(conditionForm);

        conditionFormAccordion.setWidthFull();
        conditionFormAccordion.close();
        conditionFormAccordion.add("Conditions", conditionsLayout);

        form.setClassName("alternative-form");
        form.add(elements);

        add(form, conditionFormAccordion, actionsLayout);
    }

    private void configurarOpcion(String valor) {
        if(valor == "nextStep Existente")
        {
            this.stepComboBox.setItems(this.stepList);
            this.stepComboBox.setVisible(true);
            this.nextStep.setVisible(false);
            this.guideComboBox.setVisible(false);
            this.systemGuideComboBox.setVisible(false);
        }
        else if(valor == "Guia Existente"){
            if (this.guideList!=null){
                this.guideComboBox.setItems(this.guideList);
            }
            this.guideComboBox.setVisible(true);
            this.nextStep.setVisible(false);
            this.systemGuideComboBox.setVisible(false);
        }
        else if(valor == "Guia Sistema"){
            if (this.systemGuideList!=null){
                this.systemGuideComboBox.setItems(this.systemGuideList);
            }
            this.systemGuideComboBox.setVisible(true);
            this.nextStep.setVisible(false);
            this.guideComboBox.setVisible(false);
        }
        else
        {
            this.stepComboBox.setVisible(false);
            this.guideComboBox.setVisible(false);
            this.systemGuideComboBox.setVisible(false);
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
            this.alternative.setNewGuide(false);
            this.alternative.setSystemGuide(false);
        }
        else if(this.option.getValue() == "Guia Existente")
        {
            this.alternative.setGuideName(this.guideComboBox.getValue().getName());
            this.alternative.setNewStep(false);
            this.alternative.setNewGuide(false);
            this.alternative.setSystemGuide(false);
        }
        else if(this.option.getValue() == "Guia Sistema")
        {
            this.alternative.setGuideName(this.systemGuideComboBox.getValue().getName());
            this.alternative.setNewStep(false);
            this.alternative.setNewGuide(false);
            this.alternative.setSystemGuide(true);
        }
        else if(this.option.getValue() == "nextStep Nuevo")
        {
            this.alternative.setNextStep(this.nextStep.getValue());
            this.alternative.setNewStep(true);
            this.alternative.setNewGuide(false);
            this.alternative.setSystemGuide(false);
        }
        else
        {
            this.alternative.setGuideName(this.nextStep.getValue());
            this.alternative.setNewStep(false);
            this.alternative.setNewGuide(true);
            this.alternative.setSystemGuide(false);
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
                    this.guideComboBox.setVisible(false);
                    this.systemGuideComboBox.setVisible(false);
                    this.nextStep.setVisible(true);
                }
                else
                {
                    this.stepComboBox.setVisible(true);
                    this.nextStep.setVisible(false);
                    this.guideComboBox.setVisible(false);
                    this.systemGuideComboBox.setVisible(false);
                    this.option.setValue("nextStep Existente");
                    if (!stepList.isEmpty()){
                        Step stepSelected = this.stepList.stream().filter(step -> step.getTextId().equals(alternative.getNextStep())).findFirst().get();
                        this.stepComboBox.setValue(stepSelected);
                    }
                }
            }
            else if(alternative.isSystemGuide()){
                this.stepComboBox.setVisible(false);
                this.nextStep.setVisible(false);
                this.guideComboBox.setVisible(false);
                this.systemGuideComboBox.setVisible(true);
                this.option.setValue("Guia Sistema");
                Guide guideSelected = this.systemGuideList.stream().filter(guide -> guide.getName().equals(alternative.getGuideName())).findFirst().get();
                this.systemGuideComboBox.setValue(guideSelected);
            }
            else if (alternative.isNewGuide()){
                this.stepComboBox.setVisible(false);
                this.guideComboBox.setVisible(false);
                this.systemGuideComboBox.setVisible(false);
                this.nextStep.setVisible(true);
                this.option.setValue("Guia Nueva");
                this.nextStep.setValue(this.alternative.getGuideName());
            }
            else
            {
                this.label.setValue(alternative.getLabel());
                this.stepComboBox.setVisible(false);
                this.nextStep.setVisible(false);
                this.guideComboBox.setVisible(true);
                this.systemGuideComboBox.setVisible(false);
                this.option.setValue("Guia Existente");
                if(guideList != null && guideList.size()>0)
                {
                    Guide guideSelected = this.guideList.stream().filter(guide -> guide.getName().equals(alternative.getGuideName())).findFirst().get();
                    this.guideComboBox.setValue(guideSelected);
                }
            }

            this.label.setValue(alternative.getLabel());
            editing = true;
            delete.setVisible(true);
            conditionForm.updateForm(alternative);
        }
        else
        {
            this.nextStep.clear();
            this.label.clear();
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
        guideComboBox.setVisible(false);
        systemGuideComboBox.setVisible(false);
        option.setValue("nextStep Nuevo");
        nextStep.setVisible(true);
        conditionForm.agregarLayout.setVisible(true);
        conditionFormAccordion.close();
    }
}
