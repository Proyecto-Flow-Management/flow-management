package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "guide")
public class Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "guide_id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "label")
	private String label;

	@Column(name = "main_step")
	private String mainStep;

	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(
			name = "guide_steps",
			joinColumns = {@JoinColumn(name = "guide_id")},
			inverseJoinColumns = {@JoinColumn(name = "step_id")}
	)
	Set<Step> steps = new HashSet<>();

	/*@OneToMany(fetch = FetchType.EAGER, mappedBy = "guide", cascade = CascadeType.MERGE)
	private Collection<Step> steps;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "guide_id")
	private Set<Step> steps = new LinkedHashSet<Step>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "guide_id")
	private List<Step> steps;

	@ManyToOne
	@JoinColumn(name = "id_alternative", nullable = false, foreignKey = @ForeignKey(name = "FK_guide_alternative"))
	private Alternative alternative;*/

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

	public String getMainStep() {
		return mainStep;
	}

	public void setMainStep(String mainStep) {
		this.mainStep = mainStep;
	}

	public void setSteps(Set<Step> steps) {
		this.steps = steps;
	}

	public Set<Step> getSteps() {
		return steps;
	}
}