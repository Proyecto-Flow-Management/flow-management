package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name= "convertion")
public class Convertion extends AbstractEntity {

    @Column(name = "condition")
    private String condition;

    @Column(name = "source_unit")
    private String sourceUnit;

    @Column(name = "destination_unit")
    private String destinationUnit;

    @ManyToOne
    @JoinColumn(name = "operation_parameter_id")
    private OperationParameter operationParameter;

//    @ManyToOne
//    @JoinColumn(name = "convertion_type_id")
//    private Convertion_Type ConvertionType;

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
