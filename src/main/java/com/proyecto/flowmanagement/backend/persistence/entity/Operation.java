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
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "label")
	private String label;
	
	@ManyToOne
	@JoinColumn(name = "id_operation_type", nullable = false, foreignKey = @ForeignKey(name = "FK_operation_operation_type"))
	private OperationType operationType;
	
	@ManyToOne
	@JoinColumn(name = "id_guide", nullable = false, foreignKey = @ForeignKey(name = "FK_guide_operation_type"))
	private Guide guide;
	
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

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		operationType = operationType;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		guide = guide;
	}

}
