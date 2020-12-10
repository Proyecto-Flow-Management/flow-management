package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

public class ConditionParameterForm  extends FormLayout {

    ConditionParameter conditionParameter;
    TextField fieldText = new TextField("First name");
    TextField fieldTypeText = new TextField("Last name");
    TextField operatorText = new TextField("Email");
    TextField valueText = new TextField("Email");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public ConditionParameterForm() {
        configureElements();
    }

    private void configureElements() {
        add(fieldText,fieldTypeText,operatorText,valueText,createButtonsLayout());
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(click -> validateAndSave());
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if(isValid())
        {
            this.conditionParameter = new ConditionParameter();
            this.conditionParameter.setValue(valueText.getValue());
            this.conditionParameter.setField(fieldText.getValue());
            this.conditionParameter.setFieldType(fieldTypeText.getValue());
            this.conditionParameter.setOperator(operatorText.getValue());
        }

    }

    private boolean isValid() {
        boolean result = false;

        if( !fieldText.getValue().isEmpty() &&
            !fieldTypeText.getValue().isEmpty() &&
            !operatorText.getValue().isEmpty() &&
            !valueText.getValue().isEmpty())
             result = true;

        return result;
    }

}
