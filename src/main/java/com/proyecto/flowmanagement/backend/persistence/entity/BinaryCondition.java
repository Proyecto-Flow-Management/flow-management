package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "binary_condition")
public class BinaryCondition extends AbstractEntity  implements Serializable {

    @Column(name = "operator_name")
    private String operator;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="binary_condition_id")
    private List<UnaryCondition> conditions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="binary_condition_id")
    private List<BinaryCondition> binaryConditions;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<UnaryCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<UnaryCondition> conditions) {
        this.conditions = conditions;
    }
}
