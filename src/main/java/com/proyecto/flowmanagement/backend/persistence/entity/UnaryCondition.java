package com.proyecto.flowmanagement.backend.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "unary_condition")
public class UnaryCondition  extends AbstractEntity{

    @Column(name = "operation_name")
    private String operationName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="binary_condition_id")
    private List<ConditionParameter> conditionParameter;

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public List<ConditionParameter> getConditionParameter() {
        return conditionParameter;
    }

    public void setConditionParameter(List<ConditionParameter> conditionParameter) {
        this.conditionParameter = conditionParameter;
    }
}
