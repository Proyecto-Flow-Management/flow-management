package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "validate_cross_field_condition")
public class ValidateCrossFieldCondition extends AbstractEntity{

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "condition")
    private String condition;

    @Column(name = "message_error")
    private String messageError;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
