package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.def.SimpleOperationType;
import com.proyecto.flowmanagement.backend.def.TaskOperationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "task_operation")
public class TaskOperation extends Operation{

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
}
