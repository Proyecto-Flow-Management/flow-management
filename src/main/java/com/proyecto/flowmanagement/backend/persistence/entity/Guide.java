package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
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
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Step> steps;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="guide_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Operation> operations;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="guide_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Guide> guides;

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

	public List<Guide> getGuides() {
		return guides;
	}

	public void setGuides(List<Guide> guides) {
		this.guides = guides;
	}

	public List<String> validarGuia()
	{
		List<String> erroresEncontrados = new LinkedList<>();

		if(this.name.trim().isEmpty())
			erroresEncontrados.add("El campo Name es obligatorio");

		if(this.label.trim().isEmpty())
			erroresEncontrados.add("El campo Label es obligatorio");

		if(this.mainStep.trim().isEmpty())
			erroresEncontrados.add("El campo MainStep es obligatorio");

		if(this.steps == null || this.steps.size() == 0)
			erroresEncontrados.add("La Guia no contiene ningun Step");

		return erroresEncontrados;
	}

	public String validacionIncompleta()
	{
		String retorno = "";

		if(this.name.trim().isEmpty())
			retorno = "El campo Name de la Guia es obligatorio";

		if(this.label.trim().isEmpty())
			retorno = "El campo Label es obligatorio";

		return retorno;
	}
}
