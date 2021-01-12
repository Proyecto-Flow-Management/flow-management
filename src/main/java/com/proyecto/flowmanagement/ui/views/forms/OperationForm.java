package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.proyecto.flowmanagement.ui.views.grids.AlternativeGridForm;
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

import static java.lang.Integer.parseInt;


@org.springframework.stereotype.Component
@CssImport("./styles/operation-grid-form.css")
public class OperationForm extends HorizontalLayout {
    private Operation operation;
    public List<String> alteratives;
    AlternativeIdsForm alternativeIdsForm;

    OperationParameterGridForm inParameterGridForm = new OperationParameterGridForm("Crear IN Operation Parameter");
    OperationParameterGridForm outParameterGridForm = new OperationParameterGridForm("Crear OUT Operation Parameter");

    VerticalLayout form = new VerticalLayout();
    FormLayout elements = new FormLayout();
    HorizontalLayout alternativeIdsFormLayout = new HorizontalLayout();
    HorizontalLayout inParameterGridLayout = new HorizontalLayout();
    HorizontalLayout outParameterGridLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();

    TextField name = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");
    ComboBox<String> visible = new ComboBox<>("Visible");
    ComboBox<String> preExecute = new ComboBox<>("Pre Execute");
    TextField comment = new TextField("Comment");
    TextField title = new TextField("Title");
    ComboBox<String> automatic = new ComboBox<>("Automatic");
    ComboBox<String> pauseExecution = new ComboBox<>("Pause Execution");
    IntegerField operationOrder = new IntegerField ("Operation Order");
    ComboBox<OperationType> operationType = new ComboBox<>("Operation Type");
    ComboBox<String> notifyAlternative = new ComboBox<>("Notify Alternative");
    ComboBox<String> notifyOperation = new ComboBox<>("Notify Operation");
    IntegerField notifyOperationDelay = new IntegerField("Notify Operation Delay");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");


    public OperationForm() {

        setSizeFull();

        configureElements();

        configureOperationParameters();

        configureForm();

    }

    public OperationForm(List<String> alteratives) {

        setSizeFull();

        this.alteratives = alteratives;

        this.alternativeIdsForm = new AlternativeIdsForm(alteratives);

        configureElements();

        configureAlternatives();

        configureOperationParameters();

        configureForm();
    }

    private void configureElements() {
        addClassName("operationSection");
        this.name.setRequired(true);
        this.name.setErrorMessage("Este campo es obligatorio.");
        this.label.setRequired(true);
        this.label.setErrorMessage("Este campo es obligatorio.");
        this.operationType.setRequired(true);
        this.operationType.setErrorMessage("Debes seleccionar un tipo.");
        this.visible.setItems("True","False","Null");
        this.visible.setValue("Null");
        this.preExecute.setItems("True","False","Null");
        this.preExecute.setValue("Null");
        this.automatic.setItems("True","False","Null");
        this.automatic.setValue("Null");
        this.pauseExecution.setItems("True","False","Null");
        this.pauseExecution.setValue("Null");
        this.notifyAlternative.setItems("True","False","Null");
        this.notifyAlternative.setValue("Null");
        this.notifyOperation.setItems("True","False","Null");
        this.notifyOperation.setValue("Null");

        this.operationOrder.setPlaceholder("Dejar vacío para no incluir el tag.");
        this.notifyOperationDelay.setPlaceholder("Dejar vacío para no incluir el tag.");
        this.operationType.setItems(OperationType.values());

        elements.add(name,label,comment,title,notifyOperationDelay,operationOrder,operationType,visible,preExecute,automatic,pauseExecution,notifyAlternative,notifyOperation);
        actionsLayout.add(createButtonsLayout());
    }

    private void configureAlternatives() {
        alternativeIdsFormLayout.setWidthFull();
        alternativeIdsFormLayout.setWidthFull();
        alternativeIdsFormLayout.add(inParameterGridForm);
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

        if(this.alteratives != null && this.alteratives.size() >0){
            form.add(elements,
                    alternativeIdsFormLayout,
                    inParameterGridLayout,
                    outParameterGridLayout,
                    actionsLayout);
        }
        else{form.add(elements,
                inParameterGridLayout,
                outParameterGridLayout,
                actionsLayout);
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

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new OperationForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    public Operation getOperation() {
        return this.operation;
    }

    private void validateAndSave() {
        operation = new Operation();

        if(isValid())
        {
            operation.setName(name.getValue());
            operation.setLabel(label.getValue());
            if (this.visible.getValue() == "True"){
                operation.setVisible(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operation.setVisible(Boolean.FALSE);
            }
            if (this.visible.getValue() == "True"){
                operation.setPreExecute(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operation.setPreExecute(Boolean.FALSE);
            }
            operation.setComment(comment.getValue());
            operation.setTitle(title.getValue());
            if (this.visible.getValue() == "True"){
                operation.setAutomatic(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operation.setAutomatic(Boolean.FALSE);
            }
            if (this.visible.getValue() == "True"){
                operation.setPauseExecution(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operation.setPauseExecution(Boolean.FALSE);
            }
            if (!operationOrder.isEmpty()){
                operation.setOperationOrder(true);
            }
            operation.setOperationType(operationType.getValue());
            if (this.visible.getValue() == "True"){
                operation.setNotifyAlternative(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operation.setNotifyAlternative(Boolean.FALSE);
            }
            if (this.visible.getValue() == "True"){
                operation.setNotifyOperation(Boolean.TRUE);
            }
            else if (this.visible.getValue() == "False"){
                operation.setNotifyOperation(Boolean.FALSE);
            }
            if (!notifyOperationDelay.isEmpty()){
                operation.setNotifyOperationDelay(notifyOperationDelay.getValue());
            }
        }
        else {
            Span content = new Span("Algún valor ingresado no es correcto o falta completar campos.");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    public boolean isValid() {
        boolean result = false;

        if(validateFields())
            result = true;

        return result;
    }

    public boolean validateFields(){
        boolean result = false;

        if(!name.getValue().isEmpty() &&
                !label.getValue().isEmpty() &&
                !operationType.isEmpty())
            result = true;

        return result;
    }

    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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
