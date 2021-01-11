package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
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
    ComboBox<String> visible = new ComboBox<>("Visible");
    TextField visibleWhenInParameterEqualsCondition = new TextField("Visisble when In Parameter equals Condition");
    TextField type = new TextField("Type");
    TextField description = new TextField("Description");
    TextField value = new TextField("Value");
    ComboBox<String> enable = new ComboBox<>("Enable");
    ComboBox<String> required = new ComboBox<>("Required");
    TextField validateExpression = new TextField("Validate Expression");
    TextField validateExpressionErrorDescription = new TextField("Validate Expression Error Description");
    TextField optionValue = new TextField("Option Value");
    TextField dateFormat = new TextField("Date Format");
    TextField dateFormatRangeEnd = new TextField("Date Format Range End");
    TextField dateFormatFinal = new TextField("Date Format Final");
    TextField sourceValueEntityProperty = new TextField("Source Value Entity Property");
    ComboBox<String> convert = new ComboBox<>("Convert");
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
        this.name.setRequired(true);
        this.name.setErrorMessage("Este campo es obligatorio.");
        this.type.setRequired(true);
        this.type.setErrorMessage("Este campo es obligatorio.");
        this.description.setRequired(true);
        this.description.setErrorMessage("Este campo es obligatorio.");
        this.visible.setItems("True","False","Null");
        this.visible.setValue("Null");
        this.enable.setItems("True","False","Null");
        this.enable.setValue("Null");
        this.required.setItems("True","False","Null");
        this.required.setValue("Null");
        this.convert.setItems("True","False","Null");
        this.convert.setValue("Null");

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
            if (this.visible.getValue() == "True"){
                operationParameter.setVisible(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operationParameter.setVisible(Boolean.FALSE);
            }
            operationParameter.setVisibleWhenInParameterEqualsCondition(visibleWhenInParameterEqualsCondition.getValue());
            operationParameter.setType(type.getValue());
            operationParameter.setDescription(description.getValue());
            operationParameter.setValue(value.getValue());
            if (this.visible.getValue() == "True"){
                operationParameter.setEnable(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operationParameter.setEnable(Boolean.FALSE);
            }
            if (this.visible.getValue() == "True"){
                operationParameter.setRequired(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operationParameter.setRequired(Boolean.FALSE);
            }
            operationParameter.setValidateExpression(validateExpression.getValue());
            operationParameter.setValidateExpressionErrorDescription(validateExpressionErrorDescription.getValue());
            operationParameter.setOptionValue(optionValue.getValue());
            operationParameter.setDateFormat(dateFormat.getValue());
            operationParameter.setDateFormatRangeEnd(dateFormatRangeEnd.getValue());
            operationParameter.setDateFormatFinal(dateFormatFinal.getValue());
            operationParameter.setSourceValueEntityProperty(sourceValueEntityProperty.getValue());
            if (this.visible.getValue() == "True"){
                operationParameter.setConvert(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operationParameter.setConvert(Boolean.FALSE);
            }
            operationParameter.setValueWhenInParameterEquals(valueWhenInParameterEquals.getValue());
        }
        else {
            Span content = new Span("Algún valor ingresado no es correcto o falta completar campos.");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
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

        if(!name.getValue().isEmpty() &&
                !type.getValue().isEmpty() &&
                !description.getValue().isEmpty())
            result = true;

        return result;
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
