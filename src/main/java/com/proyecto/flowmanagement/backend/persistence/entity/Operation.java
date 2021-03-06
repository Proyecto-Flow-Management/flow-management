package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "operation")
public class Operation  extends AbstractEntity   implements Serializable {

	@Column(name = "name")
	private String name;

	@Column(name = "label")
	private String label;

	@Column(name = "desconocidos", length = Integer.MAX_VALUE)
	private String tagsDesconocidos;

	@Column(name = "visible")
	private Boolean visible;

	@Column(name = "preExecute")
	private Boolean preExecute;

	@Column(name = "comment")
	private String comment;

	@Column(name = "title")
	private String title;

	@Column(name = "automatic")
	private Boolean automatic;

	@Column(name = "pauseExecution")
	private Boolean pauseExecution;

	@Column(name = "operationOrder", columnDefinition = "integer default 0")
	@Nullable
	private int operationOrder = 0;

	@Column(name = "operationType")
	@JoinColumn(name = "operation_id")
	private OperationType operationType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "operation_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	private List<OperationParameter> inParameters;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "operation_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	private List<OperationParameter> outParameters;

	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name = "operation_id")
	private List<Condition> conditions;

	@Column(name = "notifyAlternative")
	private Boolean notifyAlternative;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "operation_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Alternative> alternativeIds;

	@Column(name = "notifyOperation")
	private Boolean notifyOperation;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "operation_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	private List<OperationNotifyId> operationNotifyIds;

	@Column(name = "notifyOperationDelay")
	private Integer notifyOperationDelay;

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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getPreExecute() {
		return preExecute;
	}

	public void setPreExecute(Boolean preExecute) {
		this.preExecute = preExecute;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getAutomatic() {
		return automatic;
	}

	public void setAutomatic(Boolean automatic) {
		this.automatic = automatic;
	}

	public Boolean getPauseExecution() {
		return pauseExecution;
	}

	public void setPauseExecution(Boolean pauseExecution) {
		this.pauseExecution = pauseExecution;
	}

	public Integer getOperationOrder() {
		return operationOrder;
	}

	public void setOperationOrder(int operationOrder) {
		this.operationOrder = operationOrder;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public List<OperationParameter> getInParameters() {
		return inParameters;
	}

	public void setInParameters(List<OperationParameter> inParameters) {
		this.inParameters = inParameters;
	}

	public List<OperationParameter> getOutParameters() {
		return outParameters;
	}

	public void setOutParameters(List<OperationParameter> outParameters) {
		this.outParameters = outParameters;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public Boolean getNotifyAlternative() {
		return notifyAlternative;
	}

	public void setNotifyAlternative(Boolean notifyAlternative) {
		this.notifyAlternative = notifyAlternative;
	}

	public List<Alternative> getAlternativeIds() {
		return alternativeIds;
	}

	public void setAlternativeIds(List<Alternative> alternativeIds) {
		this.alternativeIds = alternativeIds;
	}

	public Boolean getNotifyOperation() {
		return notifyOperation;
	}

	public void setNotifyOperation(Boolean notifyOperation) {
		this.notifyOperation = notifyOperation;
	}

	public List<OperationNotifyId> getOperationNotifyIds() {
		return operationNotifyIds;
	}

	public void setOperationNotifyIds(List<OperationNotifyId> operationNotifyIds) {
		this.operationNotifyIds = operationNotifyIds;
	}

	public Integer getNotifyOperationDelay() {
		return notifyOperationDelay;
	}

	public void setNotifyOperationDelay(Integer notifyOperationDelay) {
		this.notifyOperationDelay = notifyOperationDelay;
	}

	public String getTagsDesconocidos() {
		return tagsDesconocidos;
	}

	public void setTagsDesconocidos(String tagsDesconocidos) {
		this.tagsDesconocidos = tagsDesconocidos;
	}
}
