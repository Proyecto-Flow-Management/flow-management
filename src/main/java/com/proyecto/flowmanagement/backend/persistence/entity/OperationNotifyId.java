package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name= "operation_notify_id")
public class OperationNotifyId extends AbstractEntity  implements Serializable {

    @Column(name = "operation_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
