package com.proyecto.flowmanagement.backend.persistence.entity;

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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="alternative_id")
	private UnaryCondition conditions;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="binary_condition_id")
	private BinaryCondition binaryConditions;

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

	public UnaryCondition getConditions() {
		return conditions;
	}

	public void setConditions(UnaryCondition conditions) {
		this.conditions = conditions;
	}

	public BinaryCondition getBinaryConditions() {
		return binaryConditions;
	}

	public void setBinaryConditions(BinaryCondition binaryConditions) {
		this.binaryConditions = binaryConditions;
	}
}
