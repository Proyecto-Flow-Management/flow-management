package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import springfox.documentation.spring.web.readers.operation.OperationReader;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "step")
public class Step extends AbstractEntity{

	@Column(name = "name")
	private String text;
	
	@Column(name = "label")
	private String label;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	private List<Operation> operations;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	private List<Alternative> alternatives;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	private List<StepDocument> stepDocuments;

	public String getText() {
		return text;
	}

	public void setText(String name) {
		this.text = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Alternative> getAlternatives() {return this.alternatives;}

	public void setAlternative(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	public List<Operation> getOperations() {return this.operations;}

	public void setOperation(List<Operation> operations) {
		this.operations = operations;
	}

	public List<StepDocument> getDocuments() {
		return this.stepDocuments;
	}

	public void setStepDocuments(List<StepDocument> stepDocuments) {
		this.stepDocuments = stepDocuments;
	}

}
