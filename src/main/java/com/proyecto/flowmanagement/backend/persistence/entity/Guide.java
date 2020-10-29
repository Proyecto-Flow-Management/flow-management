package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "guide")
public class Guide extends AbstractEntity implements Cloneable {

	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;*/

	@Column(name = "name")
	private String name;

	@Column(name = "label")
	private String label;

	@Column(name = "main_step")
	private String mainStep;

	/*@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "guide_id")
	private List<Step> steps;*/

	/*@Column(name = "description")
	private String description;*/

	/*@ManyToOne
	@JoinColumn(name = "id_alternative", nullable = false, foreignKey = @ForeignKey(name = "FK_guide_alternative"))
	private Alternative alternative;*/

	/*@ManyToOne
	@JoinColumn(name = "id_step", nullable = false, foreignKey = @ForeignKey(name = "FK_guide_step"))
	private Step step;*/

	/*public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}*/

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

	/*public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}*/
/*public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}*/

	/*public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Alternative getAlternative() {
		return alternative;
	}

	public void setAlternative(Alternative alternative) {
		this.alternative = alternative;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}*/

}