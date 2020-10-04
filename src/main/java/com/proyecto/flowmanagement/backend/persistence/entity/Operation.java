package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name= "operation")
public class Operation extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;

    @Column(name = "visible")
    private boolean visible;

    @Column(name = "pre_execute")
    private boolean preExecute;

    @Column(name = "comment")
    private String comment;

    @Column(name = "title")
    private String title;

    @Column(name = "automatic")
    private boolean automatic;

    @Column(name = "order")
    private int order;

    @Column(name = "notify_alternative")
    private boolean notifyAlternative;

    @Column(name = "alternative_ids")
    private String alternativeIds;

    @Column(name = "notify_operation")
    private boolean notifyOperation;

    @Column(name = "operation_notify_ids")
    private String operationNotifyIds;

//    @ManyToOne
//    @JoinColumn(name = "guide_id")
//    private Guide guideId;

    @ManyToOne
    @JoinColumn(name = "operation_type_id")
    private Operation_Type operationType;

    public Operation_Type getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation_Type operationType) {
        this.operationType = operationType;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isPreExecute() {
        return preExecute;
    }

    public void setPreExecute(boolean preExecute) {
        this.preExecute = preExecute;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isNotifyAlternative() {
        return notifyAlternative;
    }

    public void setNotifyAlternative(boolean notifyAlternative) {
        this.notifyAlternative = notifyAlternative;
    }

    public String getAlternativeIds() {
        return alternativeIds;
    }

    public void setAlternativeIds(String alternativeIds) {
        this.alternativeIds = alternativeIds;
    }

    public boolean isNotifyOperation() {
        return notifyOperation;
    }

    public void setNotifyOperation(boolean notifyOperation) {
        this.notifyOperation = notifyOperation;
    }

    public String getOperationNotifyIds() {
        return operationNotifyIds;
    }

    public void setOperationNotifyIds(String operationNotifyIds) {
        this.operationNotifyIds = operationNotifyIds;
    }

//    public Guide getGuideId() {
//        return guideId;
//    }
//
//    public void setGuideId(Guide guideId) {
//        this.guideId = guideId;
//    }
}
