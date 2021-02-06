package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.SourceEntity;
import com.proyecto.flowmanagement.backend.persistence.entity.OperationParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
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
public class OperationParameterForm extends VerticalLayout {
    private OperationParameter operationParameter;
    public boolean isValid;
    public boolean editing;

    private OptionValueForm optionValueForm = new OptionValueForm();
    private ConvertConditionForm convertConditionForm = new ConvertConditionForm();
    private PropertiesForm propertiesForm = new PropertiesForm();

    private FormLayout elements = new FormLayout();
    private HorizontalLayout actionsLayout = new HorizontalLayout();

    Accordion optionValuesAccordion = new Accordion();
    Accordion convertConditionsAccordion = new Accordion();
    Accordion propertiesAccordion = new Accordion();
    VerticalLayout optionValueLayout = new VerticalLayout();
    HorizontalLayout optionValueGridLayout = new HorizontalLayout();
    VerticalLayout convertConditionLayout = new VerticalLayout();
    HorizontalLayout convertConditionGridLayout = new HorizontalLayout();
    VerticalLayout propertiesLayout = new VerticalLayout();
    HorizontalLayout propertiesGridLayout = new HorizontalLayout();

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
    private TextField dateFormat = new TextField("Date Format");
    private TextField dateFormatRangeEnd = new TextField("Date Format Range End");
    private TextField dateFormatFinal = new TextField("Date Format Final");
    private ComboBox<SourceEntity> sourceValueEntity = new ComboBox<>("Source Value Entity");
    private TextField sourceValueEntityProperty = new TextField("Source Value Entity Property");
    private ComboBox<String> convert = new ComboBox<>("Convert");
    private TextField valueWhenInParameterEquals = new TextField("Value When In Parameter Equals");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    public OperationParameterForm() {
        this.editing = false;
        this.isValid = false;
        setSizeFull();
        configureElements();
        configureForm();
    }

    private void configureElements() {
        addClassName("operationParameterSection");
        delete.setVisible(false);
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

        elements.add(name,label,visible,type,visibleWhenInParameterEqualsCondition,description,value,validateExpression,validateExpressionErrorDescription,dateFormat,dateFormatRangeEnd,dateFormatFinal,sourceValueEntity,sourceValueEntityProperty,valueWhenInParameterEquals,enable,required,convert);
        elements.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3),
                new FormLayout.ResponsiveStep("40em", 4));

        optionValueLayout.setWidthFull();
        optionValueGridLayout.setWidthFull();
        optionValueGridLayout.add(optionValueForm);
        optionValueLayout.add(optionValueGridLayout);
        optionValuesAccordion.setWidthFull();
        optionValuesAccordion.add("Option Values", optionValueLayout);
        optionValuesAccordion.close();

        convertConditionLayout.setWidthFull();
        convertConditionGridLayout.setWidthFull();
        convertConditionGridLayout.add(convertConditionForm);
        convertConditionLayout.add(convertConditionGridLayout);
        convertConditionsAccordion.setWidthFull();
        convertConditionsAccordion.add("Convert Conditions", convertConditionLayout);
        convertConditionsAccordion.close();

        propertiesLayout.setWidthFull();
        propertiesGridLayout.setWidthFull();
        propertiesGridLayout.add(propertiesForm);
        propertiesLayout.add(propertiesGridLayout);
        propertiesAccordion.setWidthFull();
        propertiesAccordion.add("Properties", propertiesLayout);
        propertiesAccordion.close();

        actionsLayout.add(createButtonsLayout());
    }

    private void configureForm() {
        add(elements, optionValuesAccordion, convertConditionsAccordion, propertiesAccordion, actionsLayout);
    }

    public void setOperationParameter(OperationParameter operationParameter) {
        this.operationParameter = operationParameter;
        if (operationParameter.getName() != null){
            this.name.setValue(operationParameter.getName());
        }
        else {
            this.name.clear();
        }
        if (operationParameter.getLabel() != null){
            this.label.setValue(operationParameter.getLabel());
        }
        else {
            this.label.clear();
        }
        if (operationParameter.getVisibleWhenInParameterEqualsCondition() != null){
            this.visibleWhenInParameterEqualsCondition.setValue(operationParameter.getVisibleWhenInParameterEqualsCondition().toString());
        }
        else {
            this.visibleWhenInParameterEqualsCondition.clear();
        }
        if (operationParameter.getType() != null){
            this.type.setValue(operationParameter.getType());
        }
        else {
            this.type.clear();
        }
        if (operationParameter.getDescription() != null){
            this.description.setValue(operationParameter.getDescription());
        }
        else {
            this.description.clear();
        }
        if (operationParameter.getValue() != null){
            this.value.setValue(operationParameter.getValue());
        }
        else {
            this.value.clear();
        }
        if (operationParameter.getValidateExpression() != null){
            this.validateExpression.setValue(operationParameter.getValidateExpression());
        }
        else {
            this.validateExpression.clear();
        }
        if (operationParameter.getValidateExpressionErrorDescription() != null){
            this.validateExpressionErrorDescription.setValue(operationParameter.getValidateExpressionErrorDescription());
        }
        else {
            this.validateExpressionErrorDescription.clear();
        }
        if (operationParameter.getDateFormat() != null){
            this.dateFormat.setValue(operationParameter.getDateFormat());
        }
        else {
            this.dateFormat.clear();
        }
        if (operationParameter.getDateFormatRangeEnd() != null){
            this.dateFormatRangeEnd.setValue(operationParameter.getDateFormatRangeEnd());
        }
        else {
            this.dateFormatRangeEnd.clear();
        }
        if (operationParameter.getDateFormatFinal() != null){
            this.dateFormatFinal.setValue(operationParameter.getDateFormatFinal());
        }
        else {
            this.dateFormatFinal.clear();
        }
        if (operationParameter.getSourceValueEntity() != null){
            this.sourceValueEntity.setValue(operationParameter.getSourceValueEntity());
        }
        else {
            this.sourceValueEntity.clear();
        }
        if (operationParameter.getValueWhenInParameterEquals() != null){
            this.valueWhenInParameterEquals.setValue(operationParameter.getValueWhenInParameterEquals());
        }
        else {
            this.valueWhenInParameterEquals.clear();
        }
        if (operationParameter.getEnable() != null){
            this.enable.setValue(operationParameter.getEnable().toString());
        }
        else {
            this.enable.clear();
        }
        if (operationParameter.getRequired() != null){
            this.required.setValue(operationParameter.getRequired().toString());
        }
        else {
            this.required.clear();
        }
        if (operationParameter.getVisible() != null){
            this.visible.setValue(operationParameter.getVisible().toString());
        }
        else {
            this.visible.clear();
        }
        if (operationParameter.getConvert() != null){
            this.convert.setValue(operationParameter.getConvert().toString());
        }
        else {
            this.convert.clear();
        }
        this.optionValueForm.setOptionValues(operationParameter.getOptionValues());
        this.convertConditionForm.setConvertConditions(operationParameter.getConvertConditions());
//        this.propertiesForm.setProperties(operationParameter.getProperties());
    }

    public void setAsDefault() {
        this.name.clear();
        this.label.clear();
        this.visibleWhenInParameterEqualsCondition.clear();
        this.type.clear();
        this.description.clear();
        this.value.clear();
        this.validateExpression.clear();
        this.validateExpressionErrorDescription.clear();
        this.dateFormat.clear();
        this.dateFormatRangeEnd.clear();
        this.dateFormatFinal.clear();
        this.sourceValueEntity.clear();
        this.valueWhenInParameterEquals.clear();
        this.enable.clear();
        this.required.clear();
        this.visible.clear();
        this.convert.clear();
        this.optionValueForm.setAsDefault();
        this.convertConditionForm.setAsDefault();
        this.propertiesForm.setAsDefault();
        this.propertiesAccordion.close();
        this.optionValuesAccordion.close();
        this.convertConditionsAccordion.close();
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> saveOperationParameter());
        close.addClickListener(click -> fireEvent(new OperationParameterForm.CloseEvent(this)));

        return new HorizontalLayout(save, close, delete);
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

        operationParameter.setOptionValues(optionValueForm.getOptionValues());
        operationParameter.setConvertConditions(convertConditionForm.getConvertConditions());
        this.optionValuesAccordion.close();
        this.convertConditionsAccordion.close();
        this.propertiesAccordion.close();

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
