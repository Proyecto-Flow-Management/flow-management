package com.proyecto.flowmanagement.backend.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "binary_condition")
public class BinaryCondition extends AbstractEntity{

    @Column(name = "operator_name")
    private String Operator;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="binary_condition_id")
    private List<UnaryCondition> conditions;

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public List<UnaryCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<UnaryCondition> conditions) {
        this.conditions = conditions;
    }
}
