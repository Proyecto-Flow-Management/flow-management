package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.SimpleOperationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "simple_operation")
public class SimpleOperation extends Operation{

    @Column(name = "type")
    private SimpleOperationType type;

    @Column(name = "service")
    private String service;

    public SimpleOperationType getType() {
        return type;
    }

    public void setType(SimpleOperationType type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> validateOperation() {

        List<String> encounteredErrors = new LinkedList<>();

        if(this.getName().isEmpty())
            encounteredErrors.add("El campo Name es obligatorio");

        if (this.getLabel().isEmpty())
            encounteredErrors.add("El campo Label es obligatorio");

        if (this.getOperationType().toString() == null)
            encounteredErrors.add("El campo OperationType es obligatorio");

        if (this.getOperationOrder() != null){
            if (this.getOperationOrder() <= 0)
                encounteredErrors.add("El campo OperationOrder debe ser un entero positivo");
        }

        if (this.getNotifyOperationDelay() != null){
            if (this.getNotifyOperationDelay() <= 0)
                encounteredErrors.add("El campo NotifyOperationDelay debe ser un entero positivo");
        }

        if (this.type.toString().isEmpty())
            encounteredErrors.add("El campo Type es obligatorio");

        if (this.service.isEmpty())
            encounteredErrors.add("El campo service es obligatorio");

        return encounteredErrors;
    }

    public String incompleteValidation(){

        if (this.getName().isEmpty())
            return "El campo Name es obligatorio";

        if (this.getLabel().isEmpty())
            return "El campo Label es obligatorio";

        if (this.getOperationType() == null)
            return "El campo OperationType es obligatorio";

        if (this.getOperationOrder() != null){
            if (this.getOperationOrder() <= 0)
                return "El campo OperationOrder debe ser un entero positivo";
        }

        if (this.getNotifyOperationDelay() != null){
            if (this.getNotifyOperationDelay() <= 0)
                return "El campo NotifyOperationDelay debe ser un entero positivo";
        }

        if (this.type == null)
            return "El campo Type es obligatorio";

        if (this.service.isEmpty())
            return "El campo service es obligatorio";

        return "";
    }
}
