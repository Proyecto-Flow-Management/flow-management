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

    private Alternative alternative;

    ConditionsGridForm conditionsGridForm = new ConditionsGridForm();

    TextField guideName = new TextField("Guida Nombre Alternative");
    TextField label = new TextField("Label Alternative");
    TextField nextStep = new TextField("nextStep Alternative");

    public Button save = new Button("Guardar");
    Button delete = new Button("Borrar");
    public Button close = new Button("Cancelar");

    Binder<Alternative> binder = new BeanValidationBinder<>(Alternative.class);


    public AlternativeForm() {
        setClassName("alternativeSection");
        configureElements();
        binder.bindInstanceFields(this);
    }

    public void configureElements() {

        VerticalLayout form = new VerticalLayout();

        HorizontalLayout elements = new HorizontalLayout();

        HorizontalLayout conditionsLayout = new HorizontalLayout();

        HorizontalLayout actionsLayout = new HorizontalLayout();

        elements.add(guideName, label, nextStep);

        actionsLayout.add(save,close,delete);

        conditionsLayout.setWidthFull();
        conditionsLayout.add(conditionsGridForm);

        form.add(elements,conditionsLayout, actionsLayout);

        add(form);
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
        binder.readBean(alternative);
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
        this.alternative = new Alternative();
        this.alternative.setLabel(label.getValue());
        return this.alternative;
    }

}
