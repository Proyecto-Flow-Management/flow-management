package com.proyecto.flowmanagement.ui.views.forms;


import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class UnaryConditionForm extends FormLayout {

    UnaryCondition unaryCondition;
    TextField operationNameText = new TextField("Operation Name");
    TextField fieldText = new TextField("First name");
    TextField fieldTypeText = new TextField("Last name");
    TextField operatorText = new TextField("Email");
    TextField valueText = new TextField("Email");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public UnaryConditionForm() {
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
            this.unaryCondition = new UnaryCondition();

            ConditionParameter conditionParameter = new ConditionParameter();
            conditionParameter = new ConditionParameter();
            conditionParameter.setValue(valueText.getValue());
            conditionParameter.setField(fieldText.getValue());
            conditionParameter.setFieldType(fieldTypeText.getValue());
            conditionParameter.setOperator(operatorText.getValue());

            unaryCondition.setOperationName(operationNameText.getValue());
            this.unaryCondition.setConditionParameter(conditionParameter);
        }

    }

    private boolean isValid() {
        boolean result = false;

        if( !fieldText.getValue().isEmpty() &&
            !fieldTypeText.getValue().isEmpty() &&
            !operatorText.getValue().isEmpty() &&
            !valueText.getValue().isEmpty() &&
            !operationNameText.getValue().isEmpty())
            result = true;

        return result;
    }
}
