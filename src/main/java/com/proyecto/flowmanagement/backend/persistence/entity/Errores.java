package com.proyecto.flowmanagement.backend.persistence.entity;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name= "errores")
public class Errores extends AbstractEntity  implements Serializable {

    @Column(name = "mensaje_error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        String fecha  = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        this.error = fecha + "   |    " + error;
    }
}
