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
import java.util.List;


@org.springframework.stereotype.Component
@CssImport("./styles/operation-grid-form.css")
public class OperationForm extends HorizontalLayout {

    private Operation operation;
    public List<String> alternatives;
    public Boolean isValid = false;
    public Boolean editing;

    private AlternativeIdsForm alternativeIdsForm;
    private CandidatesGrupsForm candidatesGrupsForm = new CandidatesGrupsForm();

    private OperationParameterGridForm inParameterGridForm = new OperationParameterGridForm("Crear IN Operation Parameter");
    private OperationParameterGridForm outParameterGridForm = new OperationParameterGridForm("Crear OUT Operation Parameter");

    private VerticalLayout form = new VerticalLayout();
    private FormLayout elements = new FormLayout();
    private HorizontalLayout alternativeIdsFormLayout = new HorizontalLayout();
    private HorizontalLayout inParameterGridLayout = new HorizontalLayout();
    private HorizontalLayout outParameterGridLayout = new HorizontalLayout();
    private HorizontalLayout actionsLayout = new HorizontalLayout();
    private HorizontalLayout groupLayout = new HorizontalLayout();

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
    private TextField candidateGroups = new TextField("Candidate Groups");
    private TextField mailTemplate = new TextField("Mail Template");
    private TextField mailTo = new TextField("Mail To");
    private TextField mailSubjectPrefix = new TextField("Mail Subject Prefix");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");

    public OperationForm() {
        setSizeFull();

        configureElements();

        configureOperationParameters();

        configureForm();
    }

    public OperationForm(List<String> alternatives) {

        if (!alternatives.isEmpty()){
            setSizeFull();

            this.alternatives = alternatives;

            this.alternativeIdsForm = new AlternativeIdsForm(alternatives);

            configureElements();

            configureAlternatives();

            configureOperationParameters();

            configureForm();
        } else {
            setSizeFull();

            configureElements();

            configureOperationParameters();

            configureForm();
        }
    }

    private void configureElements() {
        addClassName("operationSection");
        this.name.setRequired(true);
        this.name.setErrorMessage("Este campo es obligatorio.");
        this.label.setRequired(true);
        this.label.setErrorMessage("Este campo es obligatorio.");
        this.operationType.setRequired(true);
        this.operationType.setErrorMessage("Debes seleccionar un tipo.");
        this.visible.setItems("True","False");
        this.preExecute.setItems("True","False");
        this.automatic.setItems("True","False");
        this.pauseExecution.setItems("True","False");
        this.notifyAlternative.setItems("True","False");
        this.notifyAlternative.addValueChangeListener(event -> addAlternativeIdsForm(Boolean.valueOf(this.notifyAlternative.getValue())));
        this.notifyOperation.setItems("True","False");
        this.operationOrder.setPlaceholder("Dejar vacío si desea no incluir este tag.");
        this.notifyOperationDelay.setPlaceholder("Dejar vacío si desea no incluir este tag.");
        this.operationType.setItems(OperationType.values());
        this.operationType.addValueChangeListener(event -> addElements(this.operationType.getValue()));

        elements.add(name,label,comment,title,notifyOperationDelay,operationOrder,visible,preExecute,automatic,pauseExecution,notifyAlternative,notifyOperation,operationType);
        actionsLayout.add(createButtonsLayout());
        groupLayout.add(candidatesGrupsForm);
    }

    private void addAlternativeIdsForm(Boolean choice){
        if (choice){
            elements.add(alternativeIdsForm);
        }
    }

    private void addElements(OperationType operationType){
        if (operationType== OperationType.simpleOperation){
            this.simpleOperationType.setRequired(true);
            this.simpleOperationType.setErrorMessage("Debes seleccionar un tipo.");
            this.simpleOperationType.setItems(SimpleOperationType.values());
            this.service.setRequired(true);
            this.service.setErrorMessage("Este campo es obligatorio.");

            elements.remove(taskOperationType,targetSystem,candidateGroups,mailTemplate,mailTo,mailSubjectPrefix,groupLayout);
            elements.add(simpleOperationType,service);
        }
        else if(operationType== OperationType.taskOperation){
            this.taskOperationType.setRequired(true);
            this.taskOperationType.setErrorMessage("Debes seleccionar un tipo.");
            this.taskOperationType.setItems(TaskOperationType.values());

            elements.remove(simpleOperationType,service);
            elements.add(taskOperationType,targetSystem,candidateGroups,mailTemplate,mailTo,mailSubjectPrefix, groupLayout);
        }
    }

    private void configureAlternatives() {
        alternativeIdsFormLayout.setWidthFull();
        alternativeIdsForm = new AlternativeIdsForm(alternatives);
        alternativeIdsFormLayout.add(alternativeIdsForm);
        alternativeIdsFormLayout.setVisible(false);
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

        if(this.alternatives != null && this.alternatives.size() >0){
            form.add(elements,
                    alternativeIdsFormLayout,
                    inParameterGridLayout,
                    outParameterGridLayout,
                    actionsLayout,groupLayout);
        }
        else{form.add(elements,
                inParameterGridLayout,
                outParameterGridLayout,
                actionsLayout,groupLayout);
        }

        add(form);
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> saveOperation());
        close.addClickListener(click -> fireEvent(new OperationForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
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
            if (!candidateGroups.isEmpty()){
                taskOperation.setCandidateGroups(candidateGroups.getValue());
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

    public void setAsDefault(){
        name.clear();
        label.clear();
        visible.clear();
        preExecute.clear();
        automatic.clear();
        pauseExecution.clear();
        notifyAlternative.clear();
        notifyAlternative.clear();
        notifyOperation.clear();
        operationOrder.clear();
        notifyOperationDelay.clear();
        operationType.clear();
        service.clear();
        targetSystem.clear();
        candidateGroups.clear();
        mailTemplate.clear();
        mailTo.clear();
        mailSubjectPrefix.clear();

        elements.remove(taskOperationType,targetSystem,candidateGroups,mailTemplate,mailTo,mailSubjectPrefix);
        elements.remove(simpleOperationType,service);
    }
}
