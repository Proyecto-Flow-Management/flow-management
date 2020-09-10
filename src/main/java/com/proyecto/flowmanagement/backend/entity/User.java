package com.proyecto.flowmanagement.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity implements Cloneable {

    public enum Status {
        Status1, Status2
    }

    @NotNull
    @NotEmpty
    private String firstName = "";

    @NotNull
    @NotEmpty
    private String lastName = "";

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @NotNull
    private User.Status status;

    @Email
    @NotNull
    @NotEmpty
    private String email = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Rol getRol(){
        return rol;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}