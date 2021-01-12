package com.proyecto.flowmanagement.ui.views.forms;
import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/unary-form.css")
public class UnaryConditionForm extends FormLayout {
    public boolean editing;

    UnaryCondition unaryCondition = new UnaryCondition();

    TextField operationNameText = new TextField("Operation Name");
    TextField fieldText = new TextField("Field Text");
    TextField fieldTypeText = new TextField("Field Type Text");
    TextField operatorText = new TextField("Operator Text");
    TextField valueText = new TextField("Value Text");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    Binder<UnaryCondition> binder = new BeanValidationBinder<>(UnaryCondition.class);

    public UnaryConditionForm() {
        editing = false;
        configureElements();
    }

    private void configureElements() {
        addClassName("unaryForm");
        this.operationNameText.setValue("");
        this.operationNameText.setRequired(true);
        this.operationNameText.setErrorMessage("Este campo es obligatorio.");
        this.fieldText.setValue("");
        this.fieldText.setRequired(true);
        this.fieldText.setErrorMessage("Este campo es obligatorio.");
        this.fieldTypeText.setValue("");
        this.fieldTypeText.setRequired(true);
        this.fieldTypeText.setErrorMessage("Este campo es obligatorio.");
        this.operatorText.setValue("");
        this.operatorText.setRequired(true);
        this.operatorText.setErrorMessage("Este campo es obligatorio.");;
        this.valueText.setValue("");
        this.valueText.setRequired(true);
        this.valueText.setErrorMessage("Este campo es obligatorio.");;
        add(operationNameText, fieldText,fieldTypeText,operatorText,valueText,createButtonsLayout());
    }


    private Component createButtonsLayout() {
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.setVisible(false);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(click -> validateAndSave());
        return new HorizontalLayout(save, close, delete);
    }

    private void validateAndSave() {
        if(isValid())
        {
            unaryCondition = new UnaryCondition();
            ConditionParameter conditionParameter = new ConditionParameter();
            conditionParameter.setValue(valueText.getValue());
            conditionParameter.setField(fieldText.getValue());
            conditionParameter.setFieldType(fieldTypeText.getValue());
            conditionParameter.setOperator(operatorText.getValue());

            unaryCondition.setOperationName(operationNameText.getValue());
            unaryCondition.setConditionParameter(conditionParameter);
        }
        else {
            Span content = new Span("Algún valor ingresado no es correcto o falta completar campos.");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    public void setUnaryCondition(UnaryCondition unaryCondition) {

        if(unaryCondition != null){
            this.operationNameText.setValue(unaryCondition.getOperationName());
            this.fieldText.setValue(unaryCondition.getConditionParameter().getField());
            this.fieldTypeText.setValue(unaryCondition.getConditionParameter().getFieldType());
            this.operatorText.setValue(unaryCondition.getConditionParameter().getOperator());
            this.valueText.setValue(unaryCondition.getConditionParameter().getValue());
            this.editing = true;
            this.delete.setVisible(true);
        }
        else{
            this.operationNameText.setValue("");
            this.fieldText.setValue("");
            this.fieldTypeText.setValue("");
            this.operatorText.setValue("");
            this.valueText.setValue("");
            this.editing=false;
            this.delete.setVisible(false);
        }
    }


    public UnaryCondition getUnaryCondition()
    {
        return this.unaryCondition;
    }

    public boolean isValid() {
        boolean result = false;

        if (validateFields())
            result = true;

        return result;
    }

    public boolean validateFields(){
        boolean result = false;

        if( !fieldText.getValue().isEmpty() &&
                !fieldTypeText.getValue().isEmpty() &&
                !operatorText.getValue().isEmpty() &&
                !valueText.getValue().isEmpty() &&
                !operationNameText.getValue().isEmpty())
            result = true;

        return result;
    }

    public void setBinaryCondition(UnaryCondition unaryCondition) {
        if(unaryCondition != null){
            this.editing = true;
            this.operationNameText.setValue(unaryCondition.getOperationName());
            this.delete.setVisible(true);
        }
        else{
            this.operationNameText.setValue("");
            this.editing=false;
        }
    }

    // Events
    public static abstract class UnaryConditionFormEvent extends ComponentEvent<UnaryConditionForm> {
        private UnaryCondition unaryCondition;

        protected UnaryConditionFormEvent(UnaryConditionForm source, UnaryCondition unaryCondition) {
            super(source, false);
            this.unaryCondition = unaryCondition;
        }

        public UnaryCondition getUnaryCondition() {
            return unaryCondition;
        }
    }

    public static class SaveEvent extends UnaryConditionForm.UnaryConditionFormEvent {
        SaveEvent(UnaryConditionForm source, UnaryCondition unaryCondition) {
            super(source, unaryCondition);
        }
    }

    public static class DeleteEvent extends UnaryConditionForm.UnaryConditionFormEvent {
        DeleteEvent(UnaryConditionForm source, UnaryCondition unaryCondition) {
            super(source, unaryCondition);
        }

    }

    public static class CloseEvent extends UnaryConditionForm.UnaryConditionFormEvent {
        CloseEvent(UnaryConditionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
