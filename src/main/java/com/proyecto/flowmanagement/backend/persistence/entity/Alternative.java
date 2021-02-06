package com.proyecto.flowmanagement.backend.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
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

	@Column(name = "newStep")
	private boolean newStep;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="alternative_unary_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Condition> conditions;

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

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(Condition condition)
	{
		if(this.getConditions() == null)
			this.conditions = new LinkedList<>();

		this.conditions.add(condition);
	}


	public void setUnaryCondition(Condition oldCondition, Condition newCondition)
	{
		for (Condition actual: this.conditions) {
			if(actual == oldCondition)
			{
				int index = this.conditions.indexOf(actual);
				this.conditions.set(index, newCondition);
			}
			else if(actual.setUnaryCondition(oldCondition,newCondition));
				break;
		}
	}

	public void setBinaryCondition(Condition oldCondition, Condition newCondition)
	{
		for (Condition actual : this.conditions) {
			if(actual == oldCondition)
			{
				int index = this.conditions.indexOf(actual);
				this.conditions.set(index, newCondition);
			}
			else if(actual.setBinaryCondition(oldCondition,newCondition));
				break;
		}
	}

	public void deleteCondition(Condition eliminar)
	{
		for (Condition actual: this.conditions) {
			if(actual == eliminar)
				conditions.remove(actual);
			else if(actual.deleteCondition(eliminar));
			break;
		}
	}

	public List<String> validarAltarnative()
	{
		List<String> erroresEncontrados = new LinkedList<>();

		if(this.label.trim().isEmpty())
			erroresEncontrados.add("El campo Label es obligatorio");

		if(this.getNextStep().trim().isEmpty() && this.nextStep.trim().isEmpty())
			erroresEncontrados.add("Debe seleccionar una guia o un step para el Alternative");

		return erroresEncontrados;
	}

	public String validacionIncompleta()
	{
		String retorno = "";

		if(this.label.trim().isEmpty())
			retorno = "El campo Label es obligatorio";

		return retorno;
	}

	public boolean isNewStep() {
		return newStep;
	}

	public void setNewStep(boolean newStep) {
		this.newStep = newStep;
	}

	public boolean getNewStep() {
		return this.newStep;
	}
}
