package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "condition_type")
public class ConditionType  extends AbstractEntity{
	
	@Column(name = "name")
    private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="condition_type_id")
	private List<ConditionParameter> parameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
