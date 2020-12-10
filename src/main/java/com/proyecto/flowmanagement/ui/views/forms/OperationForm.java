package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.OperationType;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.ui.views.grids.AlternativeGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import static java.lang.Integer.parseInt;


@org.springframework.stereotype.Component
@CssImport("./styles/operation-grid-form.css")
public class OperationForm extends HorizontalLayout {
    private Operation operation;

    AlternativeGridForm alternativeGridForm = new AlternativeGridForm();

    VerticalLayout form = new VerticalLayout();
    FormLayout elements = new FormLayout();
    HorizontalLayout alternativeGridLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();

    TextField name = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");
    Checkbox visible = new Checkbox("Visible");
    Checkbox preExecute = new Checkbox("Pre Execute");
    TextField comment = new TextField("Comment");
    TextField title = new TextField("Title");
    Checkbox automatic = new Checkbox("Automatic");
    Checkbox pauseExecution = new Checkbox("Pause Execution");
    Checkbox operationOrder = new Checkbox("Operation Order");
    ComboBox<OperationType> operationType = new ComboBox<>("Operation Type");
    Checkbox notifyAlternative = new Checkbox("Notify Alternative");
    Checkbox notifyOperation = new Checkbox("Notify Operation");
    TextField notifyOperationDelay = new TextField("Notify Operation Delay");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");


    public OperationForm() {
        setSizeFull();

        configureElements();

        configureAlternatives();

        configureForm();

    }

    private void configureElements() {
        addClassName("operationSection");
        this.name.setValue("");
        this.label.setValue("");
        this.visible.setValue(false);
        this.preExecute.setValue(false);
        this.comment.setValue("");
        this.title.setValue("");
        this.automatic.setValue(false);
        this.pauseExecution.setValue(false);
        this.operationOrder.setValue(false);
        this.operationType.setItems(OperationType.values());
        this.notifyAlternative.setValue(false);
        this.notifyOperation.setValue(false);
        this.notifyOperationDelay.setValue("");

        elements.add(name,label,comment,title,notifyOperationDelay,operationType,visible,preExecute,automatic,pauseExecution,operationOrder,notifyAlternative,notifyOperation);
        actionsLayout.add(createButtonsLayout());
    }

    private void configureAlternatives() {
        alternativeGridForm.setWidthFull();
        alternativeGridLayout.setWidthFull();
        alternativeGridLayout.add(alternativeGridForm);
    }

    private void configureForm() {
        form.add(elements,
                alternativeGridLayout,
                actionsLayout);

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
        if(isValid())
        {
            operation.setName(name.getValue());
            operation.setLabel(label.getValue());
            operation.setVisible(visible.getValue());
            operation.setPreExecute(preExecute.getValue());
            operation.setComment(comment.getValue());
            operation.setTitle(title.getValue());
            operation.setAutomatic(automatic.getValue());
            operation.setPauseExecution(pauseExecution.getValue());
            operation.setOperationOrder(operationOrder.getValue());
            operation.setOperationType(operationType.getValue());
            operation.setNotifyAlternative(notifyAlternative.getValue());
            operation.setNotifyOperation(notifyOperation.getValue());
            operation.setNotifyOperationDelay(parseInt(notifyOperationDelay.getValue()));
        }
    }

    private boolean isValid() {
        boolean result = false;

        if(!name.getValue().isEmpty() &&
                !label.getValue().isEmpty() &&
                !comment.getValue().isEmpty() &&
                !title.getValue().isEmpty() &&
                operationType != null &&
                !notifyOperationDelay.getValue().isEmpty() &&
                isInteger(notifyOperationDelay.getValue()))
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
