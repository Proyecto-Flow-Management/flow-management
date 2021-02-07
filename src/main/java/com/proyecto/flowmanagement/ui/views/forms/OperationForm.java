package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.def.SimpleOperationType;
import com.proyecto.flowmanagement.backend.def.TaskOperationType;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.SimpleOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.TaskOperation;
import com.proyecto.flowmanagement.ui.views.grids.OperationParameterGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;


@org.springframework.stereotype.Component
@CssImport("./styles/operation-grid-form.css")
public class OperationForm extends VerticalLayout {

    private Operation operation;
    public List<String> alternatives;
    public List<String> operations;
    public Boolean isValid;
    public Boolean editing;

    public ConditionsForm conditionForm = new ConditionsForm();

    Accordion conditionFormAccordion = new Accordion();
    VerticalLayout conditionFormLayout = new VerticalLayout();

    FormLayout elementsForm = new FormLayout();

    Accordion accordionBasicInformation = new Accordion();
    VerticalLayout basicInformation = new VerticalLayout();

    Accordion inParameterAccordion = new Accordion();
    VerticalLayout inParametersLayout = new VerticalLayout();

    Accordion outParameterAccordion = new Accordion();
    VerticalLayout outParametersLayout = new VerticalLayout();

    Accordion groupsAccordion = new Accordion();
    public Accordion alternativesIdsAccordion = new Accordion();
    public AlternativeIdsForm alternativeIdsForm;

    public Accordion operationNotifyIdsFormAccordion = new Accordion();
    public OperationNotifyIdsForm operationNotifyIdsForm;

    private CandidatesGrupsForm candidatesGrupsForm = new CandidatesGrupsForm();

    private OperationParameterGridForm inParameterGridForm = new OperationParameterGridForm("Crear IN Operation Parameter");
    private OperationParameterGridForm outParameterGridForm = new OperationParameterGridForm("Crear OUT Operation Parameter");

    private VerticalLayout elements = new VerticalLayout();
    private HorizontalLayout alternativeIdsFormLayout = new HorizontalLayout();
    private HorizontalLayout inParameterGridLayout = new HorizontalLayout();
    private HorizontalLayout outParameterGridLayout = new HorizontalLayout();
    private HorizontalLayout actionsLayout = new HorizontalLayout();
    private FormLayout groupLayout = new FormLayout();

    private TextField name = new TextField("Name");
    private TextField label = new TextField("Label");
    private  ComboBox<String> visible = new ComboBox<>("Visible");
    private ComboBox<String> preExecute = new ComboBox<>("Pre Execute");
    private TextField comment = new TextField("Comment");
    private TextField title = new TextField("Title");
    private ComboBox<String> automatic = new ComboBox<>("Automatic");
    private ComboBox<String> pauseExecution = new ComboBox<>("Pause Execution");
    private IntegerField operationOrder = new IntegerField ("Operation Order");
    private ComboBox<OperationType> operationType = new ComboBox<>("Operation Type");
    private ComboBox<String> notifyAlternative = new ComboBox<>("Notify Alternative");
    private ComboBox<String> notifyOperation = new ComboBox<>("Notify Operation");
    private IntegerField notifyOperationDelay = new IntegerField("Notify Operation Delay");
    private ComboBox<SimpleOperationType> simpleOperationType = new ComboBox<>("Type");
    private TextField service = new TextField("Service");
    private ComboBox<TaskOperationType> taskOperationType = new ComboBox<>("Type");
    private TextField targetSystem = new TextField("Target System");
    private TextField optionValues = new TextField("Option Values");
    private TextField convertConditions = new TextField("Convert Conditions");
    private TextField properties = new TextField("Properties");
    private TextField mailTemplate = new TextField("Mail Template");
    private TextField mailTo = new TextField("Mail To");
    private TextField mailSubjectPrefix = new TextField("Mail Subject Prefix");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    public OperationForm() {
        this.editing = false;
        this.isValid = false;

        setClassName("operationSection");

        setSizeFull();

        configureAlternatives();

        configureOperations();

        configureElements();

        configureOperationParameters();

        configureConditions();

        configureForm();
    }

    private void configureOperations() {
        if(this.operations == null)
            operations = new LinkedList<>();
        operationNotifyIdsForm = new OperationNotifyIdsForm(operations);
    }

    private void configureConditions() {
    }

    public OperationForm(List<String> alternatives) {

        if (!alternatives.isEmpty()){
            setSizeFull();

            this.alternatives = alternatives;

            this.alternativeIdsForm = new AlternativeIdsForm(alternatives);

            configureAlternatives();

            configureElements();

            configureOperationParameters();

            configureForm();
        } else {
            setSizeFull();

            configureAlternatives();

            configureElements();

            configureOperationParameters();

            configureForm();
        }
    }

    private void configureElements() {
        addClassName("operationSection");
        delete.setVisible(false);
        this.name.setRequired(true);
        name.addClassName("vaadin-text-field-container");
        this.name.setErrorMessage("Este campo es obligatorio.");
        this.label.setRequired(true);
        label.addClassName("vaadin-text-field-container");
        this.label.setErrorMessage("Este campo es obligatorio.");
        this.operationType.setRequired(true);
        comment.addClassName("vaadin-text-field-container");
        title.addClassName("vaadin-text-field-container");
        operationType.addClassName("vaadin-text-field-container");
        this.operationType.setErrorMessage("Debes seleccionar un tipo.");
        this.visible.setItems("True","False");
        visible.addClassName("vaadin-text-field-container");
        this.preExecute.setItems("True","False");
        preExecute.addClassName("vaadin-text-field-container");
        this.automatic.setItems("True","False");
        automatic.addClassName("vaadin-text-field-container");
        this.pauseExecution.setItems("True","False");
        this.notifyAlternative.setItems("True","False");
        this.notifyAlternative.addValueChangeListener(event -> addAlternativeIdsForm(Boolean.valueOf(this.notifyAlternative.getValue())));
        this.notifyOperation.setItems("True","False");
        notifyOperation.addClassName("vaadin-text-field-container");
        this.operationOrder.setPlaceholder("Dejar vacío si desea no incluir este tag.");
        this.notifyOperationDelay.setPlaceholder("Dejar vacío si desea no incluir este tag.");
        this.operationType.setItems(OperationType.values());
        this.operationType.addValueChangeListener(event -> addElements(this.operationType.getValue()));

        FormLayout basicInformation = new FormLayout();
        basicInformation.setWidthFull();
        basicInformation.add(candidatesGrupsForm);
        groupsAccordion.add("GroupsLayout", basicInformation);
        groupsAccordion.close();

        FormLayout alternativesIdsFormLaayout = new FormLayout();
        alternativesIdsFormLaayout.setWidthFull();
        alternativesIdsFormLaayout.add(alternativeIdsForm);
        alternativesIdsAccordion.add("AlternativesIds", alternativesIdsFormLaayout);
        alternativesIdsAccordion.close();
        alternativesIdsAccordion.setVisible(false);


        FormLayout operationIdsFormLaayout = new FormLayout();
        operationIdsFormLaayout.setWidthFull();
        operationIdsFormLaayout.add(operationNotifyIdsForm);
        operationNotifyIdsFormAccordion.add("OperationNotifyId", operationIdsFormLaayout);
        operationNotifyIdsFormAccordion.close();

        elements.setWidthFull();

        elementsForm.add(name,label,comment,title,notifyOperationDelay,operationOrder,visible,preExecute,automatic,pauseExecution,notifyAlternative,notifyOperation,operationType);
        elementsForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3),
                new FormLayout.ResponsiveStep("40em", 4));
        elements.add(elementsForm, groupsAccordion, alternativesIdsAccordion);

        actionsLayout.add(createButtonsLayout());
    }

    private void addAlternativeIdsForm(Boolean choice){
//        if (choice){
//            elements.add(alternativeIdsForm);
//        }
        alternativesIdsAccordion.setVisible(choice);
    }

    private void addElements(OperationType operationType){
        if (operationType== OperationType.simpleOperation){
            this.simpleOperationType.setRequired(true);
            this.simpleOperationType.setErrorMessage("Debes seleccionar un tipo.");
            this.simpleOperationType.setItems(SimpleOperationType.values());
            this.service.setRequired(true);
            this.service.setErrorMessage("Este campo es obligatorio.");

            elementsForm.remove(taskOperationType,targetSystem,mailTemplate,mailTo,mailSubjectPrefix);
            elementsForm.add(simpleOperationType,service);
            groupsAccordion.setVisible(false);
//            elements.remove(groupsAccordion);
        }
        else if(operationType== OperationType.taskOperation){
            this.taskOperationType.setRequired(true);
            this.taskOperationType.setErrorMessage("Debes seleccionar un tipo.");
            this.taskOperationType.setItems(TaskOperationType.values());

            elementsForm.remove(simpleOperationType,service);
            elementsForm.add(taskOperationType,targetSystem,mailTemplate,mailTo,mailSubjectPrefix);
            groupsAccordion.setVisible(true);
//            elements.add(groupsAccordion);
        }
    }

    private void configureAlternatives() {
        if(this.alternatives == null)
            alternatives = new LinkedList<>();
        alternativeIdsForm = new AlternativeIdsForm(alternatives);
    }

    private void configureOperationParameters() {
        inParameterGridForm.setWidthFull();
        inParameterGridLayout.setWidthFull();
        inParameterGridLayout.add(inParameterGridForm);

        outParameterGridForm.setWidthFull();
        outParameterGridLayout.setWidthFull();
        outParameterGridLayout.add(outParameterGridForm);
    }

    private void configureForm() {

        basicInformation.setWidthFull();
        accordionBasicInformation.setWidthFull();
        accordionBasicInformation.setId("step-Layout");
        setWidthFull();
        basicInformation.add(elements, groupLayout);
        accordionBasicInformation.close();
        accordionBasicInformation.add("Elements", basicInformation);

        inParametersLayout.setWidthFull();
        inParameterAccordion.setWidthFull();
        inParameterAccordion.setId("step-Layout");
        setWidthFull();
        inParametersLayout.add(inParameterGridLayout);
        inParameterAccordion.close();
        inParameterAccordion.add("InOperation", inParametersLayout);

        outParametersLayout.setWidthFull();
        outParameterAccordion.setWidthFull();
        outParameterAccordion.setId("step-Layout");
        setWidthFull();
        outParametersLayout.add(outParameterGridLayout);
        outParameterAccordion.close();
        outParameterAccordion.add("OutOperation", outParametersLayout);

        conditionFormLayout.setWidthFull();
        conditionFormAccordion.setWidthFull();
        outParameterAccordion.setId("step-Layout");
        setWidthFull();
        conditionFormLayout.add(conditionForm);
        conditionFormAccordion.close();
        conditionFormAccordion.add("Conditions", conditionFormLayout);

        add(accordionBasicInformation,inParameterAccordion,outParameterAccordion,conditionFormAccordion,operationNotifyIdsFormAccordion,actionsLayout);
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
        this.name.setValue(operation.getName());
        this.label.setValue(operation.getLabel());

        if (operation.getOperationType() != null){
            addElements(operation.getOperationType());
        }
        if (operation.getVisible()!=null) {
            this.visible.setValue(operation.getVisible().toString());
        }
        else{
            this.visible.clear();
        }
        if (operation.getPreExecute()!=null) {
            this.preExecute.setValue(operation.getPreExecute().toString());
        }
        else{
            this.preExecute.clear();
        }
        if (operation.getComment()!=null) {
            this.comment.setValue(operation.getComment());
        }
        else {
            this.comment.clear();
        }
        if (operation.getTitle()!=null) {
            this.title.setValue(operation.getTitle());
        }
        else {
            this.title.clear();
        }
        if (operation.getAutomatic()!=null) {
            this.automatic.setValue(operation.getAutomatic().toString());
        }
        else{
            this.automatic.clear();
        }
        if (operation.getPauseExecution()!=null) {
            this.pauseExecution.setValue(operation.getPauseExecution().toString());
        }
        else{
            this.pauseExecution.clear();
        }
        if(operation.getOperationOrder() != 0)
             this.operationOrder.setValue(operation.getOperationOrder());

        this.operationType.setValue(operation.getOperationType());
        if (operation.getNotifyAlternative()!=null) {
            this.notifyAlternative.setValue(operation.getNotifyAlternative().toString());
            addAlternativeIdsForm(operation.getNotifyAlternative());
        }
        else {
            this.notifyAlternative.clear();
            addAlternativeIdsForm(false);
        }
        if (operation.getNotifyOperation()!=null) {
            this.notifyOperation.setValue(operation.getNotifyOperation().toString());
        }
        else{
            this.notifyOperation.clear();
        }
        this.notifyOperationDelay.setValue(operation.getNotifyOperationDelay());

        if(operation.getOperationType()!=null){
            this.operationType.setValue(operation.getOperationType());
            if (operationType.getValue() == OperationType.simpleOperation) {
                SimpleOperation simpleOperation = (SimpleOperation) operation;
                if(simpleOperation.getType() != null){
                    SimpleOperationType test = simpleOperation.getType();
                    this.simpleOperationType.setValue(test);
                    this.simpleOperationType.setValue(simpleOperation.getType());
                }
                else {
                    this.simpleOperationType.clear();
                }
                if(simpleOperation.getService() != null) {
                    this.service.setValue(simpleOperation.getService());
                }
                else{
                    this.service.clear();
                }
                groupsAccordion.setVisible(false);
            }
            if (operationType.getValue() == OperationType.taskOperation) {
                TaskOperation taskOperation = (TaskOperation) operation;
                if (taskOperation.getType() != null){
                    this.taskOperationType.setValue(taskOperation.getType());
                }
                else {
                    this.taskOperationType.clear();
                }
                if (taskOperation.getMailTemplate() != null) {
                    this.mailTemplate.setValue(taskOperation.getMailTemplate());
                }
                else{
                    this.mailTemplate.clear();
                }
                if (taskOperation.getMailTo() != null) {
                    this.mailTo.setValue(taskOperation.getMailTo());
                }
                else {
                    this.mailTo.clear();
                }
                if (taskOperation.getMailSubjectPrefix() != null) {
                    this.mailSubjectPrefix.setValue(taskOperation.getMailSubjectPrefix());
                }
                else {
                    this.mailSubjectPrefix.clear();
                }
                if (taskOperation.getTargetSystem() != null) {
                    this.targetSystem.setValue(taskOperation.getTargetSystem());
                }
                else {
                    this.targetSystem.clear();
                }
                this.candidatesGrupsForm.setGroupsNames(taskOperation.getGroupsIds());
                groupsAccordion.setVisible(true);

            }
            addElements(this.operationType.getValue());
        }
        else{
            this.operationType.clear();
            this.simpleOperationType.clear();
            this.taskOperationType.clear();
            this.candidatesGrupsForm.setAsDefault();
            groupsAccordion.setVisible(false);
        }

        this.inParameterGridForm.setOperationsParameters(operation.getInParameters());
        this.outParameterGridForm.setOperationsParameters(operation.getOutParameters());
        this.inParameterAccordion.close();
        this.outParameterAccordion.close();
        this.alternativesIdsAccordion.close();
        this.groupsAccordion.close();
        if (operation.getConditions() != null){
            this.conditionForm.setConditions(operation.getConditions());
        }
        this.conditionFormAccordion.close();
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> saveOperation());
        close.addClickListener(click -> fireEvent(new OperationForm.CloseEvent(this)));

        return new HorizontalLayout(save, close, delete);
    }

    public Operation getOperation() {
        return this.operation;
    }

    private void saveOperation(){
        String incompleteValidation = "El campo OperationType es obligatorio";

        if (operationType.getValue() == OperationType.simpleOperation) {
            SimpleOperation simpleOperation = new SimpleOperation();
            simpleOperation.setType(simpleOperationType.getValue());
            simpleOperation.setService(service.getValue());
            auxSaveOperation(simpleOperation);
            incompleteValidation = simpleOperation.incompleteValidation();
            simpleOperation.setGroups(this.candidatesGrupsForm.getGroupsNames());
            operation = simpleOperation;
        }
        if (operationType.getValue() == OperationType.taskOperation){
            TaskOperation taskOperation = new TaskOperation();
            taskOperation.setType(taskOperationType.getValue());
            if (!targetSystem.isEmpty()){
                taskOperation.setTargetSystem(targetSystem.getValue());
            }
            if (!mailTemplate.isEmpty()){
                taskOperation.setMailTemplate(mailTemplate.getValue());
            }
            if (!mailTo.isEmpty()){
                taskOperation.setMailTo(mailTo.getValue());
            }
            if (!mailSubjectPrefix.isEmpty()){
                taskOperation.setMailSubjectPrefix(mailSubjectPrefix.getValue());
            }
            auxSaveOperation(taskOperation);
            incompleteValidation = taskOperation.incompleteValidation();
            operation = taskOperation;
        }

        operation.setConditions(conditionForm.getConditions());

        if (!incompleteValidation.isEmpty()){
            showErrorMessage(incompleteValidation);
        }

        this.isValid = incompleteValidation.isEmpty();
    }

    private void auxSaveOperation(Operation auxOperation){
        auxOperation.setName(name.getValue());
        auxOperation.setLabel(label.getValue());
        auxOperation.setOperationType(operationType.getValue());
        auxOperation.setInParameters(inParameterGridForm.getOperationsParameter());
        auxOperation.setOutParameters(outParameterGridForm.getOperationsParameter());

        if (!visible.isEmpty()){
            auxOperation.setVisible(Boolean.valueOf(visible.getValue()));
        }
        if (!preExecute.isEmpty()){
            auxOperation.setPreExecute(Boolean.valueOf(preExecute.getValue()));
        }
        if (!comment.isEmpty()){
            auxOperation.setComment(comment.getValue());
        }
        if (!title.isEmpty()){
            auxOperation.setTitle(title.getValue());
        }
        if (!automatic.isEmpty()){
            auxOperation.setAutomatic(Boolean.valueOf(automatic.getValue()));
        }
        if (!pauseExecution.isEmpty()){
            auxOperation.setPauseExecution(Boolean.valueOf(pauseExecution.getValue()));
        }
        if (!operationOrder.isEmpty()){
            auxOperation.setOperationOrder(operationOrder.getValue());
        }
        if (!notifyAlternative.isEmpty()){
            auxOperation.setNotifyAlternative(Boolean.valueOf(notifyAlternative.getValue()));
        }
        if (!notifyOperation.isEmpty()){
            auxOperation.setNotifyOperation(Boolean.valueOf(notifyOperation.getValue()));
        }
        if (!notifyOperationDelay.isEmpty()){
            auxOperation.setNotifyOperationDelay(notifyOperationDelay.getValue());
        }
    }

    private void showErrorMessage(String incompleteValidation){
        Span message = new Span(incompleteValidation);
        Notification notification = new Notification(message);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public void setAsDefault(){
        name.clear();
        label.clear();
        comment.clear();
        title.clear();
        visible.clear();
        preExecute.clear();
        automatic.clear();
        pauseExecution.clear();
        notifyAlternative.clear();
        notifyAlternative.clear();
        notifyOperation.clear();
        operationOrder.clear();
        notifyOperationDelay.clear();
        if (!operationType.isEmpty()){
            addElements(operationType.getValue());
        }
        operationType.clear();
        service.clear();
        targetSystem.clear();
        mailTemplate.clear();
        mailTo.clear();
        mailSubjectPrefix.clear();
        inParameterGridForm.setAsDefault();
        outParameterGridForm.setAsDefault();
        candidatesGrupsForm.setAsDefault();
        groupsAccordion.setVisible(false);
        conditionForm.setAsDefault();
        conditionFormAccordion.close();
        alternativeIdsForm.setAsDefault();
        addAlternativeIdsForm(false);
        alternativesIdsAccordion.close();
        groupsAccordion.close();


//        elements.remove(taskOperationType,targetSystem,candidateGroups,mailTemplate,mailTo,mailSubjectPrefix);
//        elements.remove(simpleOperationType,service);
    }

    public boolean isValid() {
        boolean result = false;

        if(validateFields())
            result = true;

        return result;
    }

    private boolean validateFields(){
        boolean requiredFields = false;
        boolean positiveInts = true;

        if(!name.getValue().isEmpty() && !label.getValue().isEmpty() && !operationType.isEmpty()){
            if((!simpleOperationType.isEmpty() && !service.getValue().isEmpty()) || !taskOperationType.isEmpty()) {
                requiredFields = true;
            }
        }
        if(!operationOrder.isEmpty()){
            if (operationOrder.getValue() < 0){
                positiveInts = false;
            }
        }
        if(!notifyOperationDelay.isEmpty()){
            if (notifyOperationDelay.getValue() < 0){
                positiveInts = false;
            }
        }

        return (requiredFields && positiveInts);
    }

    // Events
    public static abstract class OperationFormEvent extends ComponentEvent<OperationForm> {
        private Operation operation;

        protected OperationFormEvent(OperationForm source, Operation operation) {
            super(source, false);
            this.operation = operation;
        }

        public Operation getOperation() {
            return operation;
        }
    }

    public static class SaveEvent extends OperationForm.OperationFormEvent {
        SaveEvent(OperationForm source, Operation operation) {
            super(source, operation);
        }
    }

    public static class DeleteEvent extends OperationForm.OperationFormEvent {
        DeleteEvent(OperationForm source, Operation operation) {
            super(source, operation);
        }

    }

    public static class CloseEvent extends OperationForm.OperationFormEvent {
        CloseEvent(OperationForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
