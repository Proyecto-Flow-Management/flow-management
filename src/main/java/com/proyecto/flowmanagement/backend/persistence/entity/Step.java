package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import springfox.documentation.spring.web.readers.operation.OperationReader;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "step")
public class Step extends AbstractEntity  implements Serializable {

	@Column(name = "label")
	private String label;
	
	@Column(name = "text", length = 999)
	private String text;

	@Column(name = "text_id")
	private String textId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Operation> operations;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Alternative> alternatives;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="step_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<StepDocument> stepDocuments;

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

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

	public void addAlternative(Alternative alternative){this.alternatives.add(alternative);}

	public void setStepDocuments(List<StepDocument> stepDocuments) {
		this.stepDocuments = stepDocuments;
	}

	public ValidationDTO validarStep()
	{
		ValidationDTO validationGuide = new ValidationDTO();
		validationGuide.setLabel("Step-Label: " + getLabel() );

		if(this.textId.trim().isEmpty())
			validationGuide.addError("El campo StepID es obligatorio");

		if(this.label.trim().isEmpty())
			validationGuide.addError("El campo Label es obligatorio");

		if(this.text.trim().isEmpty())
			validationGuide.addError("El campo Text es obligatorio");

		if(this.getAlternatives() == null || this.getAlternatives().size() == 0)
			validationGuide.addError("El Step no contiene ningun Alternative");
		else{
			for (Alternative alternative: this.alternatives) {
				ValidationDTO validacionAlternative = alternative.validarAltarnative();
				if(validacionAlternative.getError().size()>0 || validacionAlternative.getValidationDTOList().size() > 0)
					validationGuide.addList(alternative.validarAltarnative());
			}
		}

		if(this.getOperations() == null || this.getOperations().size() == 0)
			validationGuide.addError("El Step no contiene ningun Operation");
		else{

			List<String> operationsActuales = this.operations.stream().map(o -> o.getName()).collect(Collectors.toList());

			for (Operation operation: this.operations) {

				if(operation instanceof TaskOperation)
				{
					validationGuide.addList(((TaskOperation)  operation).validateOperation(operationsActuales));
				}
			}
		}

		return validationGuide;
	}

	public String validacionIncompleta()
	{
		String retorno = "";

		if(this.textId.trim().isEmpty())
			retorno = "El campo TextId del Step es obligatorio";

		if(this.label.trim().isEmpty())
			retorno = "El campo Label es obligatorio";

		if(this.text.trim().isEmpty())
			retorno = "El campo Text del Step es obligatorio";

		return retorno;
	}

	@Override
	public String toString(){
		return this.getText();
	}

}
