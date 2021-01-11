package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.BinaryCondition;
import com.proyecto.flowmanagement.ui.views.grids.UnaryGridCondition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/binary-form.css")
public class BinaryConditionForm extends HorizontalLayout {

    public boolean editing;
    BinaryCondition binaryCondition = new BinaryCondition();
    TextField operationNameText = new TextField("Operation Name");
    
    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    VerticalLayout form = new VerticalLayout();

    HorizontalLayout fields = new HorizontalLayout();
    HorizontalLayout unaryGridLayout = new HorizontalLayout();
    HorizontalLayout actions = new HorizontalLayout();

    UnaryGridCondition unaryGridCondition;

    Binder<BinaryCondition> binder = new BeanValidationBinder<>(BinaryCondition.class);

    public BinaryConditionForm() {
        editing = false;

        setWidthFull();

        configureElements();
        
        configureUnaryCondition();

        configureForm();
    }

    private void configureForm() {
        form.add(fields, unaryGridLayout, actions);
        add(form);
    }

    private void configureElements() {
        editing = false;
        fields.add(operationNameText);
        addClassName("binaryForm");
        createButtonsLayout();
        this.operationNameText.setValue("");
    }
    private void createButtonsLayout() {
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.setVisible(false);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(click -> validateAndSave());
        actions.add(save, close, delete);
    }

    private void validateAndSave() {
        if(isValid())
        {
            binaryCondition = new BinaryCondition();
            binaryCondition.setOperator(operationNameText.getValue());
        }

    }

    public void setBinaryCondition(BinaryCondition binaryCondition) {
        if(binaryCondition != null){
            this.editing = true;
            this.operationNameText.setValue(binaryCondition.getOperator());
            this.unaryGridCondition.setUnaryCondition(binaryCondition.getConditions());
            this.delete.setVisible(true);
        }
        else{
            this.operationNameText.setValue("");
            unaryGridCondition.setAsDefault();
            this.editing=false;
        }
    }


    public BinaryCondition getBinaryCondition()
    {
        binaryCondition.setConditions(unaryGridCondition.getUnaryConditionList());
        return this.binaryCondition;
    }

    private boolean isValid() {
        boolean result = false;

        if(!operationNameText.getValue().isEmpty())
            result = true;

        return result;
    }

    // Events
    public static abstract class BinaryConditionFormEvent extends ComponentEvent<BinaryConditionForm> {
        private BinaryCondition binaryCondition;

        protected BinaryConditionFormEvent(BinaryConditionForm source, BinaryCondition binaryCondition) {
            super(source, false);
            this.binaryCondition = binaryCondition;
        }

        public BinaryCondition getBinaryCondition() {
            return binaryCondition;
        }
    }

    private void configureUnaryCondition() {
        unaryGridLayout.setWidthFull();
        unaryGridLayout.setWidthFull();
        unaryGridCondition = new UnaryGridCondition();
        unaryGridLayout.add(unaryGridCondition);
    }
}
