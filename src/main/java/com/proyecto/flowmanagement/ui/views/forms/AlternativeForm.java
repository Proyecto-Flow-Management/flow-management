package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.ui.views.grids.ConditionsGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import sun.invoke.util.VerifyType;

@CssImport("./styles/alternative-form.css")
public class AlternativeForm extends VerticalLayout {
    public boolean editing;
    private Alternative alternative;

    ConditionsGridForm conditionsGridForm = new ConditionsGridForm();

    TextField guideName = new TextField("Guida Nombre Alternative");
    TextField label = new TextField("Label Alternative");
    TextField nextStep = new TextField("nextStep Alternative");

    public Button save = new Button("Guardar");
    Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");

    Binder<Alternative> binder = new BeanValidationBinder<>(Alternative.class);


    public AlternativeForm() {
        setClassName("alternativeSection");
        configureElements();
        editing = false;
        binder.bindInstanceFields(this);
    }

    public void configureElements() {

        save.addClickListener(buttonClickEvent -> validAndSave());

        VerticalLayout form = new VerticalLayout();

        HorizontalLayout elements = new HorizontalLayout();

        HorizontalLayout conditionsLayout = new HorizontalLayout();

        HorizontalLayout actionsLayout = new HorizontalLayout();

        elements.add(guideName, label, nextStep);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        actionsLayout.add(save,close,delete);

        conditionsLayout.setWidthFull();

        conditionsLayout.add(conditionsGridForm);

        form.add(elements,conditionsLayout, actionsLayout);

        add(form);
    }

    private void validAndSave() {
        this.alternative = new Alternative();
        alternative.setLabel(this.label.getValue());
        alternative.setGuideName(this.guideName.getValue());
        alternative.setNextStep(this.nextStep.getValue());
    }

    public void setAlternative(Alternative alternative) {
        if(alternative != null)
        {
            this.alternative = alternative;
            this.guideName.setValue(alternative.getGuideName());
            this.nextStep.setValue(alternative.getNextStep());
            this.label.setValue(alternative.getLabel());
            editing = true;
            delete.setVisible(true);
            this.conditionsGridForm = new ConditionsGridForm(alternative);
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

        if(!this.label.getValue().isEmpty() &&
           (!this.guideName.getValue().isEmpty() || !this.nextStep.getValue().isEmpty()))
            result = true;

        return false;
    }

    public Alternative getAlternative()
    {
       return this.alternative;
    }

}
