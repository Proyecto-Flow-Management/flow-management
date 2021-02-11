package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.SourceEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "operation_parameter")
public class OperationParameter extends AbstractEntity   implements Serializable {

	@Column(name = "name")
    private String name;
	
	@Column(name = "label")
    private String label;
	
	@Column(name = "visible_value")
    private Boolean visible;

	@Column(name = "visible_when_equals")
	private String visibleWhenInParameterEqualsCondition;

	@Column(name = "type_value")
	private String type;

	@Column(name = "description_value")
	private String description;

	@Column(name = "value")
	private String value;

	@Column(name = "enable")
	private Boolean enable;

	@Column(name = "required")
	private Boolean required;

	@Column(name = "validate_expression")
	private String validateExpression;

	@Column(name = "validate_expression_error_description")
	private String validateExpressionErrorDescription;

	@Column(name = "option_value")
	private String optionValue;

	@Column(name = "date_format")
	private String dateFormat;

	@Column(name = "date_format_range_end")
	private String dateFormatRangeEnd;

	@Column(name = "date_format_final")
	private String dateFormatFinal;

	@Column(name = "source_value_entity")
	private SourceEntity sourceValueEntity;

	@Column(name = "source_value_entity_property")
	private String sourceValueEntityProperty;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="operation_parameter_id")
	private List<OperationParameter> properties;

	@Column(name = "convert_value")
	private Boolean convert;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="operation_parameter_id")
	private List<Convertion> convertCondition;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="operation_parameter_id")
	private List<ValidateCrossFieldCondition> validateCrossFieldCondition;

	@Column(name = "value_when_in_parameter_equals")
	private String valueWhenInParameterEquals;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "operation_parameter_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<OptionValue> optionValues;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_operation_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<ConvertCondition> convertConditions;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_operation_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<Properties> propertiesNames;

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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getVisibleWhenInParameterEqualsCondition() {
		return visibleWhenInParameterEqualsCondition;
	}

	public void setVisibleWhenInParameterEqualsCondition(String visibleWhenInParameterEqualsCondition) {
		this.visibleWhenInParameterEqualsCondition = visibleWhenInParameterEqualsCondition;
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

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
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

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormatRangeEnd() {
		return dateFormatRangeEnd;
	}

	public void setDateFormatRangeEnd(String dateFormatRangeEnd) {
		this.dateFormatRangeEnd = dateFormatRangeEnd;
	}

	public String getDateFormatFinal() {
		return dateFormatFinal;
	}

	public void setDateFormatFinal(String dateFormatFinal) {
		this.dateFormatFinal = dateFormatFinal;
	}

	public SourceEntity getSourceValueEntity() {
		return sourceValueEntity;
	}

	public void setSourceValueEntity(SourceEntity sourceValueEntity) {
		this.sourceValueEntity = sourceValueEntity;
	}

	public String getSourceValueEntityProperty() {
		return sourceValueEntityProperty;
	}

	public void setSourceValueEntityProperty(String sourceValueEntityProperty) {
		this.sourceValueEntityProperty = sourceValueEntityProperty;
	}

	public List<OperationParameter> getProperties() {
		return properties;
	}

	public void setProperties(List<OperationParameter> properties) {
		this.properties = properties;
	}

	public Boolean getConvert() {
		return convert;
	}

	public void setConvert(Boolean convert) {
		this.convert = convert;
	}

	public List<Convertion> getConvertCondition() {
		return convertCondition;
	}

	public void setConvertCondition(List<Convertion> convertCondition) {
		this.convertCondition = convertCondition;
	}

	public List<ValidateCrossFieldCondition> getValidateCrossFieldCondition() {
		return validateCrossFieldCondition;
	}

	public void setValidateCrossFieldCondition(List<ValidateCrossFieldCondition> validateCrossFieldCondition) {
		this.validateCrossFieldCondition = validateCrossFieldCondition;
	}

	public String getValueWhenInParameterEquals() {
		return valueWhenInParameterEquals;
	}

	public void setValueWhenInParameterEquals(String valueWhenInParameterEquals) {
		this.valueWhenInParameterEquals = valueWhenInParameterEquals;
	}

	public List<OptionValue> getOptionValues() {
		return optionValues;
	}

	public void setOptionValues(List<OptionValue> optionValues) {
		this.optionValues = optionValues;
	}

	public List<ConvertCondition> getConvertConditions() {
		return convertConditions;
	}

	public void setConvertConditions(List<ConvertCondition> convertConditions) {
		this.convertConditions = convertConditions;
	}

	public List<Properties> getPropertiesNames() {
		return propertiesNames;
	}

	public void setPropertiesNames(List<Properties> propertiesNames) {
		this.propertiesNames = propertiesNames;
	}

	public List<String> validateOperationParameter() {

		List<String> encounteredErrors = new LinkedList<>();

		if(name.isEmpty())
			encounteredErrors.add("El campo Name es obligatorio");

		if (type.isEmpty())
			encounteredErrors.add("El campo Type es obligatorio");

		if (description.isEmpty())
			encounteredErrors.add("El campo Description es obligatorio");

		return encounteredErrors;
	}


	public String incompleteValidation(){
		if (name.isEmpty())
			return "El campo Name es obligatorio";

		if (type.isEmpty())
			return "El campo Type es obligatorio";

		if (description.isEmpty())
			return "El campo Description es obligatorio";

		return "";
    }
}
