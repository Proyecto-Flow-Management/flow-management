package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "binary_condition")
public class BinaryCondition extends AbstractEntity{

    @Column(name = "operator_name")
    private String Operator;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="binary_condition_id")
    private List<UnaryCondition> conditions;
}
