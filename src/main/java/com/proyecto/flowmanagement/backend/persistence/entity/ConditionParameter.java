package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "condition_parameter")
public class ConditionParameter  extends AbstractEntity   implements Serializable {
	
	@Column(name = "field")
    private String field;
	
	@Column(name = "fieldType")
    private String fieldType;

	@Column(name = "operator")
	private String operator;

	@Column(name = "value")
	private String value;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public String validarConditionParameter()
	{
		String erroresEncontrados = "";

		if(this.field.trim().isEmpty())
			erroresEncontrados = "El campo Field es obligatorio";

		if(this.fieldType.trim().isEmpty())
			erroresEncontrados = "El campo FieldType es obligatorio";

		if(this.operator.trim().isEmpty())
			erroresEncontrados = "El campo Operator es obligatorio";

		if(this.value.trim().isEmpty())
			erroresEncontrados = "El campo Value es obligatorio";

		return erroresEncontrados;
	}
}
