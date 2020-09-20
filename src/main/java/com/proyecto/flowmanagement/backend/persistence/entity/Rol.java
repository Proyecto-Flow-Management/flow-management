package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rol")
public class Rol  extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private int code;

    public Rol() {
    }

    public Rol(String name, String code) {
        this.name = name;
        this.code = Integer.parseInt(code);
    }
    public Rol(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
