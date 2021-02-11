package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "guide")
public class Guide extends AbstractEntity implements Serializable {
	
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

	public boolean editing;

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

	public ValidationDTO validarGuia()
	{
		ValidationDTO validationGuide = new ValidationDTO();
		validationGuide.setLabel("Guide-Name: " + this.getName());

		if(this.name.trim().isEmpty())
			validationGuide.addError("El campo Name es obligatorio");

		if(this.label.trim().isEmpty())
			validationGuide.addError("El campo Label es obligatorio");

		if(this.mainStep.trim().isEmpty())
			validationGuide.addError("El campo MainStep es obligatorio");

		if(this.steps == null || this.steps.size() == 0)
		{
			validationGuide.addError("La Guia no contiene ningun Step");
		}
		else
		{
			for (Step step:this.steps)
			{
				ValidationDTO vaalidacionStep = step.validarStep();
				if(vaalidacionStep.getValidationDTOList().size() > 0 || vaalidacionStep.getError().size() >0)
					validationGuide.addList(vaalidacionStep);
			}
		}

		if(operations != null && operations.size() > 0)
		{
			List<String> operationsActuales = this.operations.stream().map(o -> o.getName()).collect(Collectors.toList());

			for (Operation operation : this.operations)
			{
				if(this.operations instanceof TaskOperation)
				{
					ValidationDTO validarOperation = ((TaskOperation )operation).validateOperation(operationsActuales);
					if(validarOperation.getValidationDTOList().size() > 0 && validarOperation.getError().size() >0)
						validationGuide.addListSecond(validarOperation);
				}
				else
				{
					ValidationDTO validarOperation = ((SimpleOperation )operation).validateOperation(operationsActuales);
					if(validarOperation.getValidationDTOList().size() > 0 && validarOperation.getError().size() >0)
						validationGuide.addListSecond(validarOperation);
				}
			}
		}

		validationGuide.addListError(validacionesEspeciales());

		return validationGuide;
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


	public void addGuide(Guide newGuide)
	{
		this.guides.add(newGuide);
	}

	public boolean setGuide(Guide newGuide)
	{
		boolean flag = false;

		if(this.getGuides() != null)
		{
			for (Guide aux : this.getGuides()) {

				if(aux.editing)
				{
					Long id = aux.getId(); 
					int index = this.guides.indexOf(aux);
					newGuide.setId(id);
					this.guides.set(index,newGuide);
					return true;
				}

				if(aux.setGuide(newGuide))
					return true;
			}
		}

		return flag;
	}

	@Override
	public String toString()
	{
		return this.getName();
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}


	public void quitarEdicion()
	{
		this.editing = false;

		if(this.guides != null)
		{
			for (Guide aux : this.guides) {
				aux.quitarEdicion();
			}
		}
	}

	public boolean setearParaEditar(Guide newGuide)
	{
		boolean flag = false;

		if(this.getGuides() != null)
		{
			for (Guide aux : this.getGuides()) {

				if(aux == newGuide)
				{
					aux.editing = true;
					return true;
				}

				if(aux.setGuide(newGuide))
					return true;
			}
		}

		return flag;
	}

	public List<String>  validacionesEspeciales()
	{
		List<String> erroresEncontrados = new LinkedList<>();

		erroresEncontrados = validarMainStep(erroresEncontrados);

		return erroresEncontrados;
	}

	private List<String> validarMainStep(List<String> erroresEncontrados)
	{
		for (Step step:this.steps) {
			if(step.getTextId()==this.mainStep)
			{
				return erroresEncontrados;
			}
		}
		erroresEncontrados.add("La Guia de Name: " + this.name + " Tiene un Main Step el cual no corresponde con ningun Stap");

		return erroresEncontrados;
	}

	public Guide eliminarGuiaRecursivo()
	{
		if(this.getGuides() != null)
		{
			for (Guide aux : this.getGuides()) {

				if(aux.editing)
				{
					return aux;
				}

				if(aux.eliminarGuiaRecursivo() == null)
					return null;
			}
		}
		return  null;
	}

	public void eliminarGuia()
	{
		Guide paraEliminar = this.eliminarGuiaRecursivo();
		if(paraEliminar != null)
			this.guides.remove(paraEliminar);
	}

}
