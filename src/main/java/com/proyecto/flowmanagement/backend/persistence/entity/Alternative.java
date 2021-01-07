package com.proyecto.flowmanagement.backend.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="alternative_unary_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<UnaryCondition> conditions;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="alternative_binary_id")
	@LazyCollection(LazyCollectionOption.FALSE)
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
