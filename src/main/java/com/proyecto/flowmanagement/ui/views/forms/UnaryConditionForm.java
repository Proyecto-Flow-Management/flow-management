package com.proyecto.flowmanagement.ui.views.forms;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.Condition;
import com.proyecto.flowmanagement.backend.persistence.entity.ConditionParameter;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.grids.ParametersGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/unary-form.css")
public class UnaryConditionForm extends VerticalLayout {
    public boolean editing;

    Condition unaryCondition = new Condition();
    ParametersGridForm parametersGridForm = new ParametersGridForm();

    TextField operationNameText = new TextField("Operation Name");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");

    HorizontalLayout camposLayout = new HorizontalLayout();
    HorizontalLayout gridParametersLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();

    public UnaryConditionForm() {
        setWidthFull();
        editing = false;
        configureElements();
        configureGridForm();
        configureActions();
        configureForms();
    }

    private void configureGridForm() {
        gridParametersLayout.setWidthFull();
        gridParametersLayout.add(parametersGridForm);
    }

    private void configureActions() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> save());
        actionsLayout.add(save,close);
    }

    private void configureForms() {
        camposLayout.add(operationNameText.getValue());
        add(camposLayout, gridParametersLayout,actionsLayout);
    }

    private void configureElements() {
        setSizeFull();
        addClassName("unaryForm");
        this.operationNameText.setValue("");
        camposLayout.add(operationNameText);
    }

    public void save()
    {
        unaryCondition = new Condition();
        unaryCondition.setType(TypeOperation.unaryCondition);
        unaryCondition.setOperation(operationNameText.getValue());
        unaryCondition.setConditionParameter(parametersGridForm.getConditionParameterList());
    }


    public Condition getUnaryCondition()
    {
        return this.unaryCondition;
    }

    private List<String> isValid() {

        List<String> valores = new LinkedList<>();

        if(operationNameText.getValue().isEmpty())
        {
            valores.add("El campo OperatorName es obligatorio");
        }

        return valores;
    }

    public void setAsDefault() {
        this.operationNameText.setValue("");
        this.parametersGridForm.setasDefault();
    }

    public void setUnaryCondition(Condition condition){
        this.operationNameText.setValue(condition.getOperation());
        this.parametersGridForm.setConditionParameterList(condition.getConditionParameter());
        this.parametersGridForm.updateGrid();
    }
}
