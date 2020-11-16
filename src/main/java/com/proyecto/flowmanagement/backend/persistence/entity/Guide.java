package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "guide")
public class Guide extends AbstractEntity {
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "label")
	private String label;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="guide_id")
	private List<Step> steps;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="guide_id")
	private List<Alternative> alternatives;

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

		public void setSteps(List<Step> steps){ this.steps = steps;}

	public List<Alternative> getAlternatives() {return this.alternatives;}

	public void setAlternative(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	public List<Operation> getOperations() {return this.operations;}

	public void setOperation(List<Operation> operations) {
		this.operations = operations;
	}
}
