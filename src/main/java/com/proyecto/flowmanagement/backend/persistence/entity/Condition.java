package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.def.EStatus;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "condition")
public class Condition  extends AbstractEntity{

    @Column(name = "operation_name")
    private String operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TypeOperation type;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name="condition_id")
    private List<ConditionParameter> conditionParameter;

    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Condition hijoDerecho;

    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Condition HijoIzquierdo;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public List<ConditionParameter> getConditionParameter() {
        return conditionParameter;
    }

    public void setConditionParameter(List<ConditionParameter> conditionParameter) {
        this.conditionParameter = conditionParameter;
    }

    public Condition getHijoDerecho() {
        return hijoDerecho;
    }

    public void setHijoDerecho(Condition hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    public Condition getHijoIzquierdo() {
        return HijoIzquierdo;
    }

    public void setHijoIzquierdo(Condition hijoIzquierdo) {
        HijoIzquierdo = hijoIzquierdo;
    }

    public boolean setUnaryCondition(Condition forEdit, Condition newCondition)
    {
        if(this == forEdit)
        {
            return true;
        }
        else if(hijoDerecho != null && hijoDerecho.setUnaryCondition(forEdit,newCondition))
        {
            this.setHijoDerecho(newCondition);
            return true;
        }
        else if(HijoIzquierdo!= null && HijoIzquierdo.setUnaryCondition(forEdit,newCondition))
        {
            this.setHijoIzquierdo(newCondition);
            return true;
        }

        return  false;
    }


    public boolean setBinaryCondition(Condition forEdit, Condition newCondition)
    {
        if(this == forEdit)
        {
            return true;
        }
        else if(hijoDerecho != null && hijoDerecho.setUnaryCondition(forEdit,newCondition))
        {
            this.hijoDerecho.setOperation(newCondition.operation);
            return true;
        }
        else if(HijoIzquierdo!= null && HijoIzquierdo.setUnaryCondition(forEdit,newCondition))
        {
            this.HijoIzquierdo.setOperation(newCondition.operation);
            return true;
        }

        return  false;
    }

    public boolean deleteCondition(Condition eliminar)
    {
        if(hijoDerecho != null)
        {
            if(hijoDerecho == eliminar)
            {
                this.hijoDerecho = null;
            }
            else if(hijoDerecho.deleteCondition(eliminar));
                return true;
        }

        if(HijoIzquierdo != null)
        {
            if(HijoIzquierdo == eliminar)
            {
                this.HijoIzquierdo = null;
            }
            else if(HijoIzquierdo.deleteCondition(eliminar));
            return true;
        }

        return  false;
    }
}
