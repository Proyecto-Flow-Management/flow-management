package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "condition_table")
public class Condition  extends AbstractEntity  implements Serializable {

    @Column(name = "operation_names")
    private String operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private TypeOperation type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="condition_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ConditionParameter> conditionParameter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="condition_id_derecho")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Condition hijoDerecho;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="condition_id_izquierdo")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Condition hijoIzquierdo;

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
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(Condition hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
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
        else if(hijoIzquierdo!= null && hijoIzquierdo.setUnaryCondition(forEdit,newCondition))
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
        else if(hijoIzquierdo!= null && hijoIzquierdo.setUnaryCondition(forEdit,newCondition))
        {
            this.hijoIzquierdo.setOperation(newCondition.operation);
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
            else{
                boolean aux = hijoDerecho.deleteCondition(eliminar);
                if(aux)
                 return true;
            }

        }

        if(hijoIzquierdo != null)
        {
            if(hijoIzquierdo == eliminar)
            {
                this.hijoIzquierdo = null;
            }
            else if(hijoIzquierdo.deleteCondition(eliminar));
            return true;
        }

        return  false;
    }

    public String validarUnaryIncompleto(){

        String retorno = "";

        if(this.operation.trim().isEmpty())
            retorno = "El campo Operation es obligatorio";

        return retorno;
    }


    public String validarBinaryIncompleto(){

        String retorno = "";

        if(this.operation.trim().isEmpty())
            retorno = "El campo Operation es obligatorio";

        return retorno;
    }

    public ValidationDTO validarUnaryCompleto(){

        ValidationDTO validationGuide = new ValidationDTO();
        validationGuide.setLabel("UnCond-Operator: " + this.getOperation());

        if(this.operation.trim().isEmpty())
            validationGuide.addError("El campo Operation es obligatorio");

        return validationGuide;
    }


    public ValidationDTO validarBinaryCompleto(){

        ValidationDTO validationGuide = new ValidationDTO();
        validationGuide.setLabel("BinCond-Operator: " + this.getOperation());

        if(this.operation.trim().isEmpty())
            validationGuide.addError("El campo Operation es obligatorio");

        if(this.hijoDerecho ==null || this.getHijoIzquierdo() == null)
        {
            validationGuide.addError("Las condiciones de tipo 'BinaryCondition', deben tener 2 hijos asociados.");
        }
        else
        {
            if(hijoDerecho != null)
            {
                if(hijoDerecho.getType() == TypeOperation.binaryCondition)
                {
                    ValidationDTO validacionBinary = hijoDerecho.validarUnaryCompleto();
                    if(validacionBinary.getError().size() > 0 || validacionBinary.getValidationDTOList().size() > 0)
                        validationGuide.addList(validacionBinary);
                }
                else
                {
                    ValidationDTO validacionUnary = hijoDerecho.validarUnaryCompleto();
                    if(validacionUnary.getError().size() > 0 || validacionUnary.getValidationDTOList().size() > 0)
                        validationGuide.addList(validacionUnary);
                }
            }

            if(hijoIzquierdo !=null)
            {
                if(hijoIzquierdo.getType() == TypeOperation.binaryCondition)
                {
                    ValidationDTO validacionBinary = hijoIzquierdo.validarUnaryCompleto();
                    if(validacionBinary.getError().size() > 0 || validacionBinary.getValidationDTOList().size() > 0)
                        validationGuide.addList(validacionBinary);
                }
                else
                {
                    ValidationDTO validacionUnary = hijoIzquierdo.validarUnaryCompleto();
                    if(validacionUnary.getError().size() > 0 || validacionUnary.getValidationDTOList().size() > 0)
                        validationGuide.addList(validacionUnary);
                }
            }
        }

        return validationGuide;
    }
}
