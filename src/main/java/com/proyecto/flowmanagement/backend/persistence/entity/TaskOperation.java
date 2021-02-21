package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.commun.XMLValidaations;
import com.proyecto.flowmanagement.backend.def.SimpleOperationType;
import com.proyecto.flowmanagement.backend.def.TaskOperationType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "task_operation")
public class TaskOperation extends Operation  implements Serializable {

    @Column(name = "type")
    private TaskOperationType type;

    @Column(name = "targetSystem")
    private String targetSystem;

    @Column(name = "candidateGroups")
    private String candidateGroups;

    @Column(name = "mailTemplate")
    private String mailTemplate;

    @Column(name = "mailTo")
    private String mailTo;

    @Column(name = "mailSubjectPrefix")
    private String mailSubjectPrefix;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_operation_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private List<Groups> groupsNames;

    public TaskOperationType getType() {
        return type;
    }

    public void setType(TaskOperationType type) {
        this.type = type;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    public String getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(String candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public String getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(String mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailSubjectPrefix() {
        return mailSubjectPrefix;
    }

    public void setMailSubjectPrefix(String mailSubjectPrefix) {
        this.mailSubjectPrefix = mailSubjectPrefix;
    }

    public List<Groups> getGroupsIds() {
        return groupsNames;
    }

    public void setGroupsIds(List<Groups> groupsNames) {
        this.groupsNames = groupsNames;
    }

    public ValidationDTO validateOperation( List<String> operationsActuales ) {

        ValidationDTO validationGuide = new ValidationDTO();
        validationGuide.setLabel("Op-Label: " + getLabel() );

        if(this.getName().isEmpty())
            validationGuide.addError("El campo Name es obligatorio");

        if (this.getLabel().isEmpty())
            validationGuide.addError("El campo Label es obligatorio");

        if (this.getOperationType().toString() == null)
            validationGuide.addError("El campo OperationType es obligatorio");

        if (this.getOperationOrder() != null){
            if (this.getOperationOrder() <= 0)
                validationGuide.addError("El campo OperationOrder debe ser un entero positivo");
        }

        if (this.getNotifyOperationDelay() != null){
            if (this.getNotifyOperationDelay() <= 0)
                validationGuide.addError("El campo NotifyOperationDelay debe ser un entero positivo");
        }

        if (this.type.toString().isEmpty())
            validationGuide.addError("El campo Type es obligatorio");

        if(this.getOperationNotifyIds() != null && this.getOperationNotifyIds().size()>0)
        {
            List<String> operationsErrors= this.getOperationNotifyIds().stream().filter(on -> !operationsActuales.contains(on.getName())).map(r -> r.getName()).collect(Collectors.toList());
            for (String notifyError: operationsErrors ) {
                validationGuide.addError("Hay un OperationNotify ("+ notifyError +") haciendo referencia a un Operation que no existe");
            }
        }

        if(getTagsDesconocidos() != null && !getTagsDesconocidos().isEmpty())
        {
            XMLValidaations validations = new XMLValidaations();
            String error = validations.validarXDSMessage(getTagsDesconocidos());
            if(!error.isEmpty())
                validationGuide.addError("Error Tags Deconocidos: " + error);
        }



        return validationGuide;
    }

    public String incompleteValidation() {

        if (this.getName().isEmpty())
            return "El campo Name es obligatorio";

        if (this.getLabel().isEmpty())
            return "El campo Label es obligatorio";

        if (this.getOperationType() == null)
            return "El campo OperationType es obligatorio";

//        if (this.getOperationOrder() != null){
//            if (this.getOperationOrder() <= 0)
//                return "El campo OperationOrder debe ser un entero positivo";
//        }

        if (this.getNotifyOperationDelay() != null){
            if (this.getNotifyOperationDelay() <= 0)
                return "El campo NotifyOperationDelay debe ser un entero positivo";
        }

        if (this.type == null)
            return "El campo Type es obligatorio";

        return "";
    }
}
