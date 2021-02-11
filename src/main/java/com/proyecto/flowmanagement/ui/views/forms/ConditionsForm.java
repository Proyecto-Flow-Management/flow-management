package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.def.EStatus;
import com.proyecto.flowmanagement.backend.def.TypeOperation;
import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Condition;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/conditionform.css")
public class ConditionsForm extends HorizontalLayout {

    Alternative alternative;
    Condition actual;

    ComboBox<TypeOperation> listType = new ComboBox<>("Tipo Operacion");
    public Button agregarCondition = new Button("Add");

    // Panel Acciones Binarys
    public Button agregarBinaryEnBinary = new Button("Add Binary");
    public Button agregarUnaryABinary = new Button("Add Unary");
    public Button editarBinary = new Button("Editar");
    public Button eliminarBinary = new Button("Eliminar");

    // Panel Acciones Binarys
    public Button editarUnary = new Button("Editar");
    public Button eliminarUnary = new Button("Eliminar");

    public UnaryConditionForm unaryConditionForm = new UnaryConditionForm();
    public BinaryConditionForm binaryConditionForm;
    ConditionTreeForm conditionTreeForm = new ConditionTreeForm();

    Condition editando;

    public HorizontalLayout agregarLayout = new HorizontalLayout();
    HorizontalLayout opcionesUnary = new HorizontalLayout();
    HorizontalLayout opcionesBinary = new HorizontalLayout();
    HorizontalLayout unaryLayout = new HorizontalLayout();
    HorizontalLayout treeLayout = new HorizontalLayout();

    VerticalLayout form = new VerticalLayout();


    public ConditionsForm(){

        this.alternative = new Alternative();

        setSizeFull();

        configureTree();

        configureElements();

        configureForm();
    }

    public ConditionsForm(Alternative alternative){

        this.alternative = alternative;

        setSizeFull();

        configureTree();

        configureElements();

        configureForm();
    }

    private void configureTree() {

        List<Condition> conditions = new LinkedList<>();

        if(alternative != null && alternative.getConditions() != null && alternative.getConditions().size()>0)
            conditions = alternative.getConditions();

        conditionTreeForm.conditions = conditions;

        conditionTreeForm.setVisible(false);

        treeLayout.setSizeFull();
        treeLayout.add(conditionTreeForm);
    }

    private void configureForm()
    {
        listType.setItems(TypeOperation.values());

        agregarCondition.setClassName("button-add");

        agregarCondition.addClickListener(buttonClickEvent -> mostrarFormulrioCorrespondiente());
        unaryConditionForm.close.addClickListener(buttonClickEvent -> this.unaryConditionForm.setVisible(false));
        binaryConditionForm.close.addClickListener(buttonClickEvent -> this.binaryConditionForm.setVisible(false));
        agregarLayout.setWidthFull();

        agregarLayout.add(listType, agregarCondition);

        form.setSizeFull();

        add(form);
    }

    private void mostrarFormulrioCorrespondiente() {
        if(this.listType.getValue() == TypeOperation.unaryCondition)
        {
            showUnaryForm();
            unaryConditionForm.isValid = false;
        }
        else {
            showBinaryConditionForm();
            binaryConditionForm.isValid = false;
        }
    }

    private void showBinaryConditionForm()
    {
        this.binaryConditionForm.setAsDefault();
        this.binaryConditionForm.setVisible(true);
    }

    private void showUnaryForm()
    {
        this.unaryConditionForm.setAsDefault();
        this.unaryConditionForm.setVisible(true);
        this.unaryLayout.setVisible(true);
    }

    private void configureElements() {

        this.unaryConditionForm.setVisible(false);

        this.unaryLayout = new HorizontalLayout();
        this.unaryLayout.setWidthFull();
        this.unaryLayout.add(unaryConditionForm);
        unaryConditionForm.save.addClickListener(buttonClickEvent -> guardarUnary());

        HorizontalLayout binaryLayout = new HorizontalLayout();
        binaryLayout.setSizeFull();
        binaryConditionForm = new BinaryConditionForm();
        this.binaryConditionForm.save.addClickListener(buttonClickEvent -> guardarBinary());
        binaryConditionForm.setVisible(false);
        binaryLayout.add(binaryConditionForm);

        treeLayout = new HorizontalLayout();
        treeLayout.setSizeFull();
        conditionTreeForm.setVisible(false);
        conditionTreeForm.arbolCondition.asSingleSelect().addValueChangeListener(evt -> cargarOperaciones(evt.getValue()));
        treeLayout.add(conditionTreeForm);

        eliminarUnary.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminarBinary.addThemeVariants(ButtonVariant.LUMO_ERROR);

        eliminarUnary.addClickListener(buttonClickEvent -> deleteCondition());
        eliminarBinary.addClickListener(buttonClickEvent -> deleteCondition());

        this.opcionesBinary = new HorizontalLayout();
        this.opcionesBinary.setWidthFull();
        this.opcionesBinary.add(agregarBinaryEnBinary,
                                agregarUnaryABinary,
                                editarBinary,eliminarBinary);

        agregarBinaryEnBinary.addClickListener(buttonClickEvent -> showBinaryConditionForm());
        agregarUnaryABinary.addClickListener(buttonClickEvent -> showUnaryForm());
        editarBinary.addClickListener(buttonClickEvent -> editarBinary());

        this.opcionesBinary.setVisible(false);

        this.opcionesUnary = new HorizontalLayout();
        this.opcionesUnary.setWidthFull();
        this.opcionesUnary.add(editarUnary, eliminarUnary);
        this.opcionesUnary.setVisible(false);
        editarUnary.addClickListener(buttonClickEvent -> editarUnary());

        form.add(opcionesBinary,opcionesUnary,agregarLayout,unaryLayout,binaryLayout,treeLayout);
    }

    private void deleteCondition()
    {
        alternative.deleteCondition(actual);
        unaryConditionForm.setVisible(false);
        binaryConditionForm.setVisible(false);
        conditionTreeForm.conditions = alternative.getConditions();
        conditionTreeForm.updateGrid();
        this.opcionesBinary.setVisible(false);
        this.opcionesUnary.setVisible(false);
        actual = null;
        if(alternative.getConditions().size()==0)
            agregarLayout.setVisible(true);
        editando = null;
    }

    private void editarUnary() {
        editando = actual;
        showUnaryForm();
        unaryConditionForm.setUnaryCondition(actual);
    }

    private void editarBinary() {
        editando = actual;
        showBinaryConditionForm();
        binaryConditionForm.setBinaryCondition(actual);
    }

    private void cargarOperaciones(Condition value) {
        if(value != null)
        {
            actual = value;
            if(value.getType() == TypeOperation.binaryCondition)
                mostrarOpcionesBinary(actual);
            else
                mostrarOpcionesUnary();
        }
        else
        {
            this.opcionesUnary.setVisible(false);
            this.opcionesBinary.setVisible(false);
        }
    }

    private void mostrarOpcionesUnary() {
        this.opcionesUnary.setVisible(true);
        this.opcionesBinary.setVisible(false);
        this.agregarLayout.setVisible(false);
    }

    private void mostrarOpcionesBinary(Condition condition) {
        this.opcionesUnary.setVisible(false);
        this.opcionesBinary.setVisible(true);
        this.agregarLayout.setVisible(false);

        boolean mostrarAgregar = condition.getHijoDerecho() == null || condition.getHijoIzquierdo() == null;
        this.agregarBinaryEnBinary.setVisible(mostrarAgregar);
        this.agregarUnaryABinary.setVisible(mostrarAgregar);
    }

    private void guardarUnary() {
        if(!unaryConditionForm.isValid)
            return;

        Condition unaryCondition = unaryConditionForm.getUnaryCondition();

        if(editando == null)
        {
            if(actual == null)
                this.alternative.addCondition(unaryCondition);
            else if(actual.getHijoDerecho() == null)
                actual.setHijoDerecho(unaryCondition);
            else if(actual.getHijoIzquierdo() == null)
                actual.setHijoIzquierdo(unaryCondition);
            else
                this.alternative.addCondition(unaryCondition);
            this.unaryConditionForm.setVisible(false);
        }
        else
        {
            if(actual.getType() == TypeOperation.unaryCondition)
            {
                alternative.setUnaryCondition(editando, unaryCondition);
                unaryConditionForm.setVisible(false);
            }

            actual = null;
            editando = null;
        }

        conditionTreeForm.setVisible(true);
        conditionTreeForm.conditions = alternative.getConditions();
        this.agregarUnaryABinary.setVisible(false);
        this.agregarBinaryEnBinary.setVisible(false);
        conditionTreeForm.updateGrid();
        agregarLayout.setVisible(false);
    }

    private void modificarUnaryCondicion(Condition actual, Condition editado) {
        for (Condition condition : this.alternative.getConditions() ) {
            if(actual == condition)
            {
                condition = editado;
            }
            else if(modificarUnaryRecursivo(condition, actual ,editado))
                break;
        }
    }

    private boolean modificarUnaryRecursivo(Condition condition, Condition actual, Condition editado) {

        boolean flag = false;

        if(condition == actual)
        {
            condition = editado;
            flag = true;
        }
        else if(condition.getHijoIzquierdo() != null && modificarUnaryRecursivo(condition.getHijoIzquierdo(),actual,editado))
        {
            flag = true;
        }
        else if(condition.getHijoDerecho() != null && modificarUnaryRecursivo(condition.getHijoDerecho(),actual,editado))
        {
            flag = true;
        }

        return flag;
    }

    private void guardarBinary(){
        if(!binaryConditionForm.isValid)
            return;

        Condition binaryCondition = binaryConditionForm.getBinaryCondition();

        if(editando == null)
        {
            if(actual == null)
                this.alternative.addCondition(binaryCondition);
            else if(actual.getHijoDerecho() == null)
                actual.setHijoDerecho(binaryCondition);
            else if(actual.getHijoIzquierdo() == null)
                actual.setHijoIzquierdo(binaryCondition);
        }
        else
        {
            alternative.setBinaryCondition(editando, binaryCondition);
        }

        this.binaryConditionForm.setVisible(false);
        conditionTreeForm.setVisible(true);
        conditionTreeForm.conditions = alternative.getConditions();
        conditionTreeForm.updateGrid();
        agregarLayout.setVisible(false);
    }

    public List<Condition> getConditions(){
        return this.alternative.getConditions();
    }

    public void setAsDefault() {
        this.alternative = new Alternative();
        binaryConditionForm.setAsDefault();
        unaryConditionForm.setAsDefault();
        conditionTreeForm.conditions = new LinkedList<>();
        conditionTreeForm.updateGrid();
        conditionTreeForm.arbolCondition.deselectAll();
        conditionTreeForm.setVisible(false);
    }

    public void setConditions(List<Condition> conditions) {
        this.alternative = new Alternative();
        binaryConditionForm.setAsDefault();
        unaryConditionForm.setAsDefault();
        conditionTreeForm.conditions = conditions;
        conditionTreeForm.updateGrid();
        conditionTreeForm.arbolCondition.deselectAll();
//        conditionTreeForm.setVisible(false);
    }

    public void updateForm(Alternative alternative) {

        this.alternative = alternative;

        if(alternative.getConditions() != null && this.alternative.getConditions().size() >0)
        {
            this.conditionTreeForm.conditions = alternative.getConditions();
            this.conditionTreeForm.updateGrid();
            this.conditionTreeForm.setVisible(true);
        }
        else
        {
            this.conditionTreeForm.setVisible(false);
        }

        this.agregarUnaryABinary.setVisible(false);
        this.agregarBinaryEnBinary.setVisible(false);
        this.agregarCondition.setVisible(true);
    }
}

