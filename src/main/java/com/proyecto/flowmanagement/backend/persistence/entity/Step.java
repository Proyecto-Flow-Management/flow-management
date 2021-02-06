package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import springfox.documentation.spring.web.readers.operation.OperationReader;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "step")
public class Step extends AbstractEntity{

	@Column(name = "label")
	private String label;
	
	@Column(name = "text")
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

	public List<String> validarStep()
	{
		List<String> erroresEncontrados = new LinkedList<>();

		if(this.textId.trim().isEmpty())
			erroresEncontrados.add("El campo StepID es obligatorio");

		if(this.label.trim().isEmpty())
			erroresEncontrados.add("El campo Label es obligatorio");

		if(this.text.trim().isEmpty())
			erroresEncontrados.add("El campo Text es obligatorio");

		if(this.getAlternatives() == null || this.getAlternatives().size() == 0)
			erroresEncontrados.add("El Step no contiene ningun Alternative");
		else{
			for (Alternative alternative: this.alternatives) {
				erroresEncontrados.addAll(alternative.validarAltarnative());
			}
		}

		if(this.getOperations() == null || this.getOperations().size() == 0)
			erroresEncontrados.add("El Step no contiene ningun Operation");
		else{
			for (Operation operation: this.operations) {
				//erroresEncontrados.addAll(operation.());
			}
		}

		return erroresEncontrados;
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
