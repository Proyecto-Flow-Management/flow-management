package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name= "operation_parameter")
public class Operation_Parameter {
    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;

    @Column(name = "visible")
    private boolean visible;

    @Column(name = "visible_when_in_parameter_equals_condition")
    private String visibleWhenCondition;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "value")
    private String value;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "required")
    private boolean required;

    @Column(name = "validate_expression")
    private String validateExpression;

    @Column(name = "validate_expression_error_description")
    private String validateExpressionErrorDescription;

    @Column(name = "date_format")
    private String dateFormat;

    @Column(name = "source_value_entity_property")
    private String sourceValueEntityProperty;

    @Column(name = "convert")
    private boolean convert;

    @ManyToOne
    @JoinColumn(name = "operation_type_id")
    private Operation_Type operationType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getVisibleWhenCondition() {
        return visibleWhenCondition;
    }

    public void setVisibleWhenCondition(String visibleWhenCondition) {
        this.visibleWhenCondition = visibleWhenCondition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getValidateExpression() {
        return validateExpression;
    }

    public void setValidateExpression(String validateExpression) {
        this.validateExpression = validateExpression;
    }

    public String getValidateExpressionErrorDescription() {
        return validateExpressionErrorDescription;
    }

    public void setValidateExpressionErrorDescription(String validateExpressionErrorDescription) {
        this.validateExpressionErrorDescription = validateExpressionErrorDescription;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getSourceValueEntityProperty() {
        return sourceValueEntityProperty;
    }

    public void setSourceValueEntityProperty(String sourceValueEntityProperty) {
        this.sourceValueEntityProperty = sourceValueEntityProperty;
    }

    public boolean isConvert() {
        return convert;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public Operation_Type getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation_Type operationType) {
        this.operationType = operationType;
    }
}
