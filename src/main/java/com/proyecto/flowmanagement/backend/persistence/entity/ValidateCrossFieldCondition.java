package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "validate_cross_field_condition")
public class ValidateCrossFieldCondition extends AbstractEntity{

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "condition_name")
    private String condition;

    @Column(name = "message_error")
    private String messageError;
}
