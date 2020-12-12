package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

@org.springframework.stereotype.Component
@CssImport("./styles/operation-parameter-grid-form.css")
public class OperationParameterForm extends HorizontalLayout {
    private OperationParameter operationParameter;

//    AlternativeGridForm alternativeGridForm = new AlternativeGridForm();

    VerticalLayout form = new VerticalLayout();
    FormLayout elements = new FormLayout();
//    HorizontalLayout alternativeGridLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();

    TextField name = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");
    Checkbox visible = new Checkbox("Visible");
    TextField visibleWhenInParameterEqualsCondition = new TextField("Visisble when In Parameter equals Condition");
    TextField type = new TextField("Type");
    TextField description = new TextField("Description");
    TextField value = new TextField("Value");
    Checkbox enable = new Checkbox("Enable");
    Checkbox required = new Checkbox("Required");
    TextField validateExpression = new TextField("Validate Expression");
    TextField validateExpressionErrorDescription = new TextField("Validate Expression Error Description");
    TextField optionValue = new TextField("Option Value");
    TextField dateFormat = new TextField("Date Format");
    TextField dateFormatRangeEnd = new TextField("Date Format Range End");
    TextField dateFormatFinal = new TextField("Date Format Final");
    TextField sourceValueEntityProperty = new TextField("Source Value Entity Property");
    Checkbox convert = new Checkbox("Convert");
    TextField valueWhenInParameterEquals = new TextField("Value When In Parameter Equals");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");


    public OperationParameterForm() {
        setSizeFull();

        configureElements();

        configureForm();

    }

    private void configureElements() {
        addClassName("operationParameterSection");
        this.name.setValue("");
        this.label.setValue("");
        this.visible.setValue(false);
        this.visibleWhenInParameterEqualsCondition.setValue("");
        this.type.setValue("");
        this.description.setValue("");
        this.value.setValue("");
        this.enable.setValue(false);
        this.required.setValue(false);
        this.validateExpression.setValue("");
        this.validateExpressionErrorDescription.setValue("");
        this.optionValue.setValue("");
        this.dateFormat.setValue("");
        this.dateFormatRangeEnd.setValue("");
        this.dateFormatFinal.setValue("");
        this.sourceValueEntityProperty.setValue("");
        this.convert.setValue(false);
        this.valueWhenInParameterEquals.setValue("");
        elements.add(name,label,visibleWhenInParameterEqualsCondition,type,description,value,validateExpression,validateExpressionErrorDescription,optionValue,dateFormat,dateFormatRangeEnd,dateFormatFinal,sourceValueEntityProperty,valueWhenInParameterEquals,enable,required,visible,convert);
        actionsLayout.add(createButtonsLayout());
    }

    private void configureForm() {
        form.add(elements,
                actionsLayout);

        add(form);
    }

    public void setOperationParameter(OperationParameter operationParameter) {
        this.operationParameter = operationParameter;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new OperationParameterForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    public OperationParameter getOperationParameter() {
        return this.operationParameter;
    }

    private void validateAndSave() {
        if(isValid())
        {
            operationParameter.setName(name.getValue());
            operationParameter.setLabel(label.getValue());
            operationParameter.setVisible(visible.getValue());
            operationParameter.setVisibleWhenInParameterEqualsCondition(visibleWhenInParameterEqualsCondition.getValue());
            operationParameter.setType(type.getValue());
            operationParameter.setDescription(description.getValue());
            operationParameter.setValue(value.getValue());
            operationParameter.setEnable(enable.getValue());
            operationParameter.setRequired(required.getValue());
            operationParameter.setValidateExpression(validateExpression.getValue());
            operationParameter.setValidateExpressionErrorDescription(validateExpressionErrorDescription.getValue());
            operationParameter.setOptionValue(optionValue.getValue());
            operationParameter.setDateFormat(dateFormat.getValue());
            operationParameter.setDateFormatRangeEnd(dateFormatRangeEnd.getValue());
            operationParameter.setDateFormatFinal(dateFormatFinal.getValue());
            operationParameter.setSourceValueEntityProperty(sourceValueEntityProperty.getValue());
            operationParameter.setConvert(convert.getValue());
            operationParameter.setValueWhenInParameterEquals(valueWhenInParameterEquals.getValue());
        }
    }

    private boolean isValid() {
        boolean result = false;

        if(!name.getValue().isEmpty() &&
                !label.getValue().isEmpty() &&
                !visibleWhenInParameterEqualsCondition.getValue().isEmpty() &&
                !type.getValue().isEmpty() &&
                !description.getValue().isEmpty() &&
                !value.getValue().isEmpty() &&
                !validateExpression.getValue().isEmpty() &&
                !validateExpressionErrorDescription.getValue().isEmpty() &&
                !optionValue.getValue().isEmpty() &&
                !dateFormat.getValue().isEmpty() &&
                !dateFormatRangeEnd.getValue().isEmpty() &&
                !dateFormatFinal.getValue().isEmpty() &&
                !sourceValueEntityProperty.getValue().isEmpty() &&
                !valueWhenInParameterEquals.getValue().isEmpty())
            result = true;

        return true;
    }

    // Events
    public static abstract class OperationParameterFormEvent extends ComponentEvent<OperationParameterForm> {
        private OperationParameter operationParameter;

        protected OperationParameterFormEvent(OperationParameterForm source, OperationParameter operationParameter) {
            super(source, false);
            this.operationParameter = operationParameter;
        }

        public OperationParameter getOperationParameter() {
            return operationParameter;
        }
    }

    public static class SaveEvent extends OperationParameterForm.OperationParameterFormEvent {
        SaveEvent(OperationParameterForm source, OperationParameter operationParameter) {
            super(source, operationParameter);
        }
    }

    public static class DeleteEvent extends OperationParameterForm.OperationParameterFormEvent {
        DeleteEvent(OperationParameterForm source, OperationParameter operationParameter) {
            super(source, operationParameter);
        }

    }

    public static class CloseEvent extends OperationParameterForm.OperationParameterFormEvent {
        CloseEvent(OperationParameterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
