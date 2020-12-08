package com.proyecto.flowmanagement.ui.views.forms;


import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class UnaryConditionForm extends FormLayout {

    UnaryCondition unaryCondition = new UnaryCondition();

    TextField operationNameText = new TextField("Operation Name");
    TextField fieldText = new TextField("Field Text");
    TextField fieldTypeText = new TextField("Field Type Text");
    TextField operatorText = new TextField("Operator Text");
    TextField valueText = new TextField("Value Text");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");

    Binder<UnaryCondition> binder = new BeanValidationBinder<>(UnaryCondition.class);

    public UnaryConditionForm() {
        configureElements();
    }

    private void configureElements() {
        addClassName("unaryConditionSection");
        this.operationNameText.setValue("");
        this.fieldText.setValue("");
        this.fieldTypeText.setValue("");
        this.operatorText.setValue("");
        this.valueText.setValue("");
        add(operationNameText, fieldText,fieldTypeText,operatorText,valueText,createButtonsLayout());
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
            ConditionParameter conditionParameter = new ConditionParameter();
            conditionParameter.setValue(valueText.getValue());
            conditionParameter.setField(fieldText.getValue());
            conditionParameter.setFieldType(fieldTypeText.getValue());
            conditionParameter.setOperator(operatorText.getValue());

            unaryCondition.setOperationName(operationNameText.getValue());
            unaryCondition.setConditionParameter(conditionParameter);
        }

    }

    public void setUnaryCondition(UnaryCondition unaryCondition) {
//        this.unaryCondition = unaryCondition;
//        binder.readBean(unaryCondition);
        this.operationNameText.setValue("");
        this.fieldText.setValue("");
        this.fieldTypeText.setValue("");
        this.operatorText.setValue("");
        this.valueText.setValue("");
    }


    public UnaryCondition getUnaryCondition()
    {
//        this.unaryCondition = new UnaryCondition();
//        this.unaryCondition.setOperationName(operationNameText.getValue());
        return this.unaryCondition;
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

    // Events
    public static abstract class UnaryConditionFormEvent extends ComponentEvent<UnaryConditionForm> {
        private UnaryCondition unaryCondition;

        protected UnaryConditionFormEvent(UnaryConditionForm source, UnaryCondition unaryCondition) {
            super(source, false);
            this.unaryCondition = unaryCondition;
        }

        public UnaryCondition getUnaryCondition() {
            return unaryCondition;
        }
    }

    public static class SaveEvent extends UnaryConditionForm.UnaryConditionFormEvent {
        SaveEvent(UnaryConditionForm source, UnaryCondition unaryCondition) {
            super(source, unaryCondition);
        }
    }

    public static class DeleteEvent extends UnaryConditionForm.UnaryConditionFormEvent {
        DeleteEvent(UnaryConditionForm source, UnaryCondition unaryCondition) {
            super(source, unaryCondition);
        }

    }

    public static class CloseEvent extends UnaryConditionForm.UnaryConditionFormEvent {
        CloseEvent(UnaryConditionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
