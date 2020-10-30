package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "step")
public class Step{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "label")
	private String label;

	@Column(name = "nextStep")
	private String nextStep;

//	@ManyToOne
//	@JoinColumn(name = "id_component", nullable = false, foreignKey = @ForeignKey(name = "FK_step_component"))
//	private Component component;
//
//	@ManyToOne
//	@JoinColumn(name = "id_alternative", nullable = false, foreignKey = @ForeignKey(name = "FK_step_alternative"))
//	private Alternative alternative;
//
//	@ManyToOne
//	@JoinColumn(name = "id_operation", nullable = false, foreignKey = @ForeignKey(name = "FK_step_operation"))
//	private Operation operation;

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


	public String getNextStep() {
		return nextStep;
	}

	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}
//
//	public Component getComponent() {
//		return component;
//	}
//
//	public void setComponent(Component component) {
//		component = component;
//	}
//
//	public Alternative getAlternative() {
//		return alternative;
//	}
//
//	public void setAlternative(Alternative alternative) {
//		alternative = alternative;
//	}
//
//	public Operation getOperation() {
//		return operation;
//	}
//
//	public void setOperation(Operation operation) {
//		operation = operation;
//	}

}
