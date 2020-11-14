package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import springfox.documentation.spring.web.readers.operation.OperationReader;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "step")
public class Step extends AbstractEntity{

	@Column(name = "label")
	private String label;
	
	@Column(name = "text")
	private String text;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	private List<Operation> operations;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	private List<Alternative> alternatives;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	private List<StepDocument> stepDocuments;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	public List<StepDocument> getStepDocuments() {
		return stepDocuments;
	}

	public void setStepDocuments(List<StepDocument> stepDocuments) {
		this.stepDocuments = stepDocuments;
	}
}
