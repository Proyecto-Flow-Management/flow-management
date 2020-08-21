package com.proyecto.flowmanagement.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Rol  extends AbstractEntity {
    private String name;
    private int code;

    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
    private List<Contact> employees = new LinkedList<>();

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

    public List<Contact> getEmployees() {
        return employees;
    }
}
