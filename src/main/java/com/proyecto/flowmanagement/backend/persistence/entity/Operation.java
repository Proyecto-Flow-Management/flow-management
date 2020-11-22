package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "operation")
public class Operation  extends AbstractEntity{
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "label")
	private String label;

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

	@Column(name = "operationOrder")
	private Boolean operationOrder;

	@Column(name = "operationType")
	private OperationType operationType;

	@Column(name = "inParameters")
	private InParameters inParameters;

	@Column(name = "outParameters")
	private OutParameters outParameters;

	@Column(name = "condition")
	private Condition condition;

	@Column(name = "notifyAlternative")
	private Boolean notifyAlternative;

	@Column(name = "alternativeIds")
	private String alternativeIds;

	@Column(name = "notifyOperation")
	private Boolean notifyOperation;

	@Column(name = "operationNotifyIds")
	private String operationNotifyIds;

	@Column(name = "notifyOperationDelay")
	private Integer notifyOperationDelay;

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

	public String getLabel() {return label;}

	public void setLabel(String label) {this.label = label;}

	public Boolean getVisible() {return visible;}

	public void setVisible(Boolean visible) {this.visible = visible;}

	public Boolean getPreExecute() {return preExecute;}

	public void setPreExecute(Boolean preExecute) {this.preExecute = preExecute;}

	public String getComment() {return comment;}

	public void setComment(String comment) {this.comment = comment;}

	public String getTitle() {return title;}

	public void setTitle(String title) {this.title = title;}

	public Boolean getAutomatic() {return automatic;}

	public void setAutomatic(Boolean automatic) {this.automatic = automatic;}

	public Boolean getPauseExecution() {return pauseExecution;}

	public void setPauseExecution(Boolean pauseExecution) {this.pauseExecution = pauseExecution;}

	public Boolean getOperationOrder() {return operationOrder;}

	public void setOperationOrder(Boolean operationOrder) {this.operationOrder = operationOrder;}

	public OperationType getOperationType() {return operationType;}

	public void setOperationType(OperationType operationType) {this.operationType = operationType;}

	public InParameters getInParameters() {return inParameters;}

	public void setInParameters(InParameters inParameters) {this.inParameters = inParameters;}

	public OutParameters getOutParameters() {return outParameters;}

	public void setOutParameters(OutParameters outParameters) {this.outParameters = outParameters;}

	public Condition getCondition() {return condition;}

	public void setCondition(Condition condition) {this.condition = condition;}

	public Boolean getNotifyAlternative() {return notifyAlternative;}

	public void setNotifyAlternative(Boolean notifyAlternative) {this.notifyAlternative = notifyAlternative;}

	public String getAlternativeIds() {return alternativeIds;}

	public void setAlternativeIds(String alternativeIds) {this.alternativeIds = alternativeIds;}

	public Boolean getNotifyOperation() {return notifyOperation;}

	public void setNotifyOperation(Boolean notifyOperation) {this.notifyOperation = notifyOperation;}

	public String getOperationNotifyIds() {return operationNotifyIds;}

	public void setOperationNotifyIds(String operationNotifyIds) {this.operationNotifyIds = operationNotifyIds;}

	public Integer getNotifyOperationDelay() {return notifyOperationDelay;}

	public void setNotifyOperationDelay(Integer notifyOperationDelay) {this.notifyOperationDelay = notifyOperationDelay;}
}
