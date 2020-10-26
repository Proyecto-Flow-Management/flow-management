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


@Entity
@Table(name = "conditionn")
public class Condition {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "label")
	private String label;
	
	@ManyToOne
	@JoinColumn(name = "id_condition_type", nullable = false, foreignKey = @ForeignKey(name = "FK_condition_condition_type"))
	private ConditionType conditionType;
	
	@ManyToOne
	@JoinColumn(name = "id_alternative_parameter", nullable = false, foreignKey = @ForeignKey(name = "FK_alternative_parameter_condition_type"))
	private AlternativeParameter alternativeParameter;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public ConditionType getConditionType() {
		return conditionType;
	}
	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}
	public AlternativeParameter getAlternativeParameter() {
		return alternativeParameter;
	}
	public void setAlternativeParameter(AlternativeParameter alternativeParameter) {
		alternativeParameter = alternativeParameter;
	}
	
}
