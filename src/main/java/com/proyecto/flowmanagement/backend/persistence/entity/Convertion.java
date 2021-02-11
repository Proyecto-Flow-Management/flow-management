package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "convertion")
public class Convertion extends AbstractEntity  implements Serializable {

    @Column(name = "condition_value")
    private String condition;

    @Column(name = "source_unit")
    private String sourceUnit;

    @Column(name = "destination_unit")
    private String destinationUnit;

    @ManyToOne
    @JoinColumn(name = "operation_parameter_id")
    private OperationParameter operationParameter;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    public String getDestinationUnit() {
        return destinationUnit;
    }

    public void setDestinationUnit(String destinationUnit) {
        this.destinationUnit = destinationUnit;
    }

    public OperationParameter getOperationParameter() {
        return operationParameter;
    }

    public void setOperationParameter(OperationParameter operationParameter) {
        this.operationParameter = operationParameter;
    }

//    public Convertion_Type getConvertionType() {
//        return ConvertionType;
//    }
//
//    public void setConvertionType(Convertion_Type convertionType) {
//        ConvertionType = convertionType;
//    }
}
