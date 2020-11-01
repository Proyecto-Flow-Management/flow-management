package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;

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
}
