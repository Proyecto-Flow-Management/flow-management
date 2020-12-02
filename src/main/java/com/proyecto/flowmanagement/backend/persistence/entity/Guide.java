package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "guide")
public class Guide extends AbstractEntity {
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "label")
	private String label;

	@Column(name = "mainStep")
	private String mainStep;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="guide_id")
	@Fetch(FetchMode.SELECT)
	private List<Step> steps;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="guide_id")
	private List<Operation> operations;

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

	public String getMainStep() {
		return mainStep;
	}

	public void setMainStep(String mainStep) {
		this.mainStep = mainStep;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
}
