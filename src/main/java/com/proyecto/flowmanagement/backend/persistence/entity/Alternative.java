package com.proyecto.flowmanagement.backend.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "alternative")
public class Alternative  extends AbstractEntity{
	
	@Column(name = "guide_name")
	private String guideName;
	
	@Column(name = "label")
	private String label;

	@Column(name = "next_step")
	private String nextStep;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="alternative_unary_id")
	private List<UnaryCondition> conditions;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="alternative_binary_id")
	private List<BinaryCondition> binaryConditions;

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public String getNextStep() {
		return nextStep;
	}

	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<UnaryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<UnaryCondition> conditions) {
		this.conditions = conditions;
	}

	public List<BinaryCondition> getBinaryConditions() {
		return binaryConditions;
	}

	public void setBinaryConditions(List<BinaryCondition> binaryConditions) {
		this.binaryConditions = binaryConditions;
	}
}
