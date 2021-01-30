package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.SourceEntity;
import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

@org.springframework.stereotype.Component
@CssImport("./styles/operation-parameter-grid-form.css")
public class OperationParameterForm extends HorizontalLayout {
    private OperationParameter operationParameter;
    private Boolean isValid = false;

    private OptionValueForm optionValueForm = new OptionValueForm();
    private ConvertConditionForm convertConditionForm = new ConvertConditionForm();
    private PropertiesForm propertiesForm = new PropertiesForm();

    private VerticalLayout form = new VerticalLayout();
    private FormLayout elements = new FormLayout();
    private HorizontalLayout actionsLayout = new HorizontalLayout();
    private HorizontalLayout optionValueLayout = new HorizontalLayout();
    private HorizontalLayout convertConditionLayout = new HorizontalLayout();
    private HorizontalLayout propertiesLayout = new HorizontalLayout();
    Accordion optionValuesAccordion = new Accordion();
    Accordion convertConditionsAccordion = new Accordion();
    Accordion propertiesAccordion = new Accordion();
    FormLayout basicInformationOptionValues = new FormLayout();
    FormLayout basicInformationConvertConditions = new FormLayout();
    FormLayout basicInformationProperties = new FormLayout();

    private TextField name = new TextField("Nombre");
    private TextField label = new TextField("Etiqueta");
    private ComboBox<String> visible = new ComboBox<>("Visible");
    private TextField visibleWhenInParameterEqualsCondition = new TextField("Visisble when In Parameter equals Condition");
    private TextField type = new TextField("Type");
    private TextField description = new TextField("Description");
    private TextField value = new TextField("Value");
    private ComboBox<String> enable = new ComboBox<>("Enable");
    private ComboBox<String> required = new ComboBox<>("Required");
    private TextField validateExpression = new TextField("Validate Expression");
    private TextField validateExpressionErrorDescription = new TextField("Validate Expression Error Description");
    private TextField optionValue = new TextField("Option Value");
    private TextField dateFormat = new TextField("Date Format");
    private TextField dateFormatRangeEnd = new TextField("Date Format Range End");
    private TextField dateFormatFinal = new TextField("Date Format Final");
    private ComboBox<SourceEntity> sourceValueEntity = new ComboBox<>("Source Value Entity");
    private TextField sourceValueEntityProperty = new TextField("Source Value Entity Property");
    private ComboBox<String> convert = new ComboBox<>("Convert");
    private TextField valueWhenInParameterEquals = new TextField("Value When In Parameter Equals");

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
        this.visible.setItems("True","False");
        this.enable.setItems("True","False");
        this.required.setItems("True","False");
        this.sourceValueEntity.setItems(SourceEntity.values());
        this.convert.setItems("True","False");

        elements.add(name,label,visibleWhenInParameterEqualsCondition,type,description,value,validateExpression,validateExpressionErrorDescription,optionValue,dateFormat,dateFormatRangeEnd,dateFormatFinal,sourceValueEntityProperty,valueWhenInParameterEquals,enable,required,visible,convert);
        elements.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3),
                new FormLayout.ResponsiveStep("40em", 4));

        optionValueLayout.add(optionValueForm);
        convertConditionLayout.add(convertConditionForm);
        propertiesLayout.add(propertiesForm);

        basicInformationOptionValues.setWidthFull();
        basicInformationOptionValues.add(optionValueLayout);
        optionValuesAccordion.add("Option Values", basicInformationOptionValues);
        optionValuesAccordion.close();

        basicInformationConvertConditions.setWidthFull();
        basicInformationConvertConditions.add(convertConditionLayout);
        convertConditionsAccordion.add("Convert Conditions", basicInformationConvertConditions);
        convertConditionsAccordion.close();

        basicInformationProperties.setWidthFull();
        basicInformationProperties.add(propertiesLayout);
        propertiesAccordion.add("Properties", basicInformationProperties);
        propertiesAccordion.close();

        actionsLayout.add(createButtonsLayout());
    }

    private void configureForm() {
        form.add(elements, optionValuesAccordion, convertConditionsAccordion, propertiesAccordion, actionsLayout);
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

        save.addClickListener(click -> saveOperationParameter());
        close.addClickListener(click -> fireEvent(new OperationParameterForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    public OperationParameter getOperationParameter() {
        return this.operationParameter;
    }

    private void saveOperationParameter(){
        this.operationParameter = new OperationParameter();
        operationParameter.setName(name.getValue());
        if (!label.isEmpty()){
            operationParameter.setLabel(label.getValue());
        }
        if (!visible.isEmpty()){
            operationParameter.setVisible(Boolean.valueOf(visible.getValue()));
        }
        if (!visibleWhenInParameterEqualsCondition.isEmpty()){
            operationParameter.setVisibleWhenInParameterEqualsCondition(visibleWhenInParameterEqualsCondition.getValue());
        }
        operationParameter.setType(type.getValue());
        operationParameter.setDescription(description.getValue());
        if (!value.isEmpty()){
            operationParameter.setValue(value.getValue());
        }
        if (!value.isEmpty()){
            operationParameter.setValue(value.getValue());
        }
        if (!enable.isEmpty()){
            operationParameter.setEnable(Boolean.valueOf(enable.getValue()));
        }
        if (!required.isEmpty()){
            operationParameter.setRequired(Boolean.valueOf(required.getValue()));
        }
        if (!validateExpression.isEmpty()){
            operationParameter.setValidateExpression(validateExpression.getValue());
        }
        if (!validateExpressionErrorDescription.isEmpty()){
            operationParameter.setValidateExpressionErrorDescription(validateExpressionErrorDescription.getValue());
        }
        if (!optionValue.isEmpty()){
            operationParameter.setOptionValue(optionValue.getValue());
        }
        if (!dateFormat.isEmpty()){
            operationParameter.setDateFormat(dateFormat.getValue());
        }
        if (!dateFormatRangeEnd.isEmpty()){
            operationParameter.setDateFormatRangeEnd(dateFormatRangeEnd.getValue());
        }
        if (!dateFormatFinal.isEmpty()){
            operationParameter.setDateFormatFinal(dateFormatFinal.getValue());
        }
        if (!sourceValueEntity.isEmpty()){
            operationParameter.setSourceValueEntity(sourceValueEntity.getValue());
        }
        if (!sourceValueEntityProperty.isEmpty()){
            operationParameter.setSourceValueEntityProperty(sourceValueEntityProperty.getValue());
        }
        if (!convert.isEmpty()){
            operationParameter.setConvert(Boolean.valueOf(convert.getValue()));
        }
        if (!valueWhenInParameterEquals.isEmpty()){
            operationParameter.setValueWhenInParameterEquals(valueWhenInParameterEquals.getValue());
        }

        String incompleteValidation = operationParameter.incompleteValidation();

        if (!incompleteValidation.isEmpty()){
            showErrorMessage(incompleteValidation);
        }

        this.isValid = incompleteValidation.isEmpty();
    }

    private void showErrorMessage(String incompleteValidation){
        Span message = new Span(incompleteValidation);
        Notification notification = new Notification(message);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
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
